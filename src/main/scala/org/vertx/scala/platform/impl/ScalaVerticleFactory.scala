/*
 * Copyright 2011-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.vertx.scala.platform.impl

import java.io.{PrintWriter, Writer, File, FilenameFilter}

import scala.tools.nsc.Settings
import scala.util.matching.Regex

import org.vertx.java.core.{ Vertx => JVertx }
import org.vertx.java.core.logging.Logger
import org.vertx.java.platform.{ Container => JContainer, Verticle => JVerticle, VerticleFactory }
import org.vertx.scala.lang.{ClassLoaders, ScalaInterpreter}
import org.vertx.scala.platform.{Container, Verticle}
import java.net.URL
import org.vertx.java.core.logging.impl.LoggerFactory
import java.security.{PrivilegedAction, AccessController}
import scala.util.{Success, Failure, Try}
import scala.annotation.tailrec

/**
 * Scala verticle factory.
 *
 * @author swilliams
 * @author Ranie Jade Ramiso
 * @author Galder Zamarreño
 */
class ScalaVerticleFactory extends VerticleFactory {

  import org.vertx.scala.core._

  import ScalaVerticleFactory._

  private var vertx: Vertx = _

  private var container: Container = _

  private var loader: ClassLoader = null

  //private var interpreter: ScalaInterpreter = null

  override def init(jvertx: JVertx, jcontainer: JContainer, aloader: ClassLoader): Unit = {
    this.loader = aloader

    vertx = Vertx(jvertx)
    container = new Container(jcontainer)
  }

  @throws(classOf[Exception])
  override def createVerticle(main: String): JVerticle = {
    val newVerticle =
      if (!main.endsWith(Suffix)) ClassLoaders.newInstance[Verticle](main, loader)
      else load(main)

    ScalaVerticle.newVerticle(newVerticle.get, vertx, container)
  }

  private def resolveVerticlePath(main: String): Try[URL] = {
    // Check if path exists
    val file = new File(main)
    if (file.exists())
      Success(file.toURI.toURL)
    else {
      Option(loader.getResource(main)) match {
        case None => Failure(new IllegalArgumentException(
            s"Cannot find main script: '$main' on classpath"))
        case Some(res) => Success(res)
      }
    }
  }

  override def reportException(logger: Logger, t: Throwable): Unit = {
    logger.error("Scala verticle threw exception", t)
  }

  def close(): Unit = {
    //interpreter.close()
  }

  private def load(main: String): Try[Verticle] = {

    val settings = interpreterSettings()
    val interpreter = new ScalaInterpreter(
      settings.get, vertx, container, new LogPrintWriter(logger))

    runAsScript(main, interpreter).recoverWith { case _ =>
      // Recover by trying to compile it as a Scala class
      logger.info(s"Script contains compilation errors, or $main is a Scala class (pass -Dvertx.scala.interpreter.verbose=true to find out more)")
      logger.info(s"Compiling as a Scala class")

      val className = extractClassName(main)
      val classFile = getClassFile(main)

      for {
        classLoader <- interpreter.compileClass(classFile)
        verticle <- newVerticleInstance(className, classLoader)
      } yield {
        logger.info(s"Starting $className")
        verticle
      }
    }
  }

  private def runAsScript(main: String, interpreter:ScalaInterpreter): Try[Verticle] = {
    logger.info(s"Compiling $main as Scala script")
    // Try running it as a script
    for {
      url <- resolveVerticlePath(main)
      result <- interpreter.runScript(url)
    } yield {
      logger.info(s"Starting $main")
      DummyVerticle
    }
  }

  private def interpreterSettings(): Try[Settings] = {
    val settings = new Settings()

    for {
      jar <- findAll(loader, "lib", JarFileRegex)
    } yield {
      logger.debug(s"Add $jar to compiler boot classpath")
      settings.bootclasspath.append(jar.getAbsolutePath)
    }

    val moduleLocation = getRootModuleLocation
    moduleLocation match {
      case None =>
        Failure(new IllegalStateException("Unable to resolve mod-lang-scala root location"))
      case Some(loc) =>
        settings.bootclasspath.append(loc.getAbsolutePath)
        settings.usejavacp.value = true
        settings.verbose.value = ScalaInterpreter.isVerbose
        Success(settings)
    }
  }

  private def getRootModuleLocation: Option[File] = {
    AccessController.doPrivileged(new PrivilegedAction[Option[File]]() {
      def run(): Option[File] = {
        for {
          pd <- Option(classOf[ScalaVerticleFactory].getProtectionDomain)
          cs <- Option(pd.getCodeSource)
          loc <- Option(cs.getLocation)
        } yield new File(loc.toURI)
      }
    })
  }

  private def extractClassName(main: String): String =
    main.replaceFirst(".scala$", "").replaceAll("/", ".")

  private def getClassFile(main: String): File = {
    Option(loader.getResource(main)) match {
      case None => new File(main)
      case Some(resource) =>
        new File(resource.toExternalForm.replaceFirst("file:", ""))
    }
  }

  private def newVerticleInstance(className: String, classLoader: ClassLoader): Try[Verticle] = {
    // If class not found, try to deduce a shorter name in case a system path was passed
    // Could have used recoverWith but you lose tail recursion, so using pattern matching
    @tailrec
    def searchVerticleInstance(className: String, classLoader: ClassLoader): Try[Verticle] = {
      val instanceTry = ClassLoaders.newInstance(className, classLoader)
      instanceTry match {
        case Success(verticle) => instanceTry
        case Failure(e: ClassNotFoundException) =>
          val dot = className.indexOf('.')
          if (dot < 0) instanceTry
          else {
            val shorterClassName = className.substring(dot + 1)
            logger.debug(s"Class not found, try with $shorterClassName")
            searchVerticleInstance(shorterClassName, classLoader)
          }
        case Failure(t) => instanceTry
      }
    }

    searchVerticleInstance(className, classLoader).recoverWith { case _ =>
      Failure(new ClassNotFoundException(
        s"Class $className not found, nor any shortened versions"))
    }
  }

  private case object DummyVerticle extends Verticle

}

object ScalaVerticleFactory {

  val logger = LoggerFactory.getLogger(classOf[ScalaVerticleFactory].getName)

  val JarFileRegex = "^(.*\\.jar)$".r

  val Suffix = ".scala"

  /**
   * Find all files matching the given regular expression in the directory.
   * Note that the search is not recursive.
   *
   * @param directory File denoting directory to search files in
   * @param regex regular expression to match
   * @return an [[scala.Array[File]] representing the collection of files matching
   *         the regular expression
   */
  def findAll(directory: File, regex: Regex): Array[File] = {
    // Protect against null return from listing files
    val files: Array[File] = directory.listFiles(new FilenameFilter {
      def accept(dir: File, name: String): Boolean = {
        regex.findFirstIn(name).isDefined
      }
    })
    Option(files).getOrElse(Array[File]())
  }

  /**
   * Find all files matching the given regular expression in a path within a
   * classloader. Note that the search is not recursive.
   *
   * @param classLoader class repository where to look for files
   * @param path String representing the path within the classloader where to look for files
   * @param regex regular expression to match
   * @return an [[scala.Array[File]] representing the collection of files matching
   *         the regular expression
   */
  def findAll(classLoader: ClassLoader, path: String, regex: Regex): Array[File] = {
    if (classLoader.getResources(path).hasMoreElements) {
      findAll(new File(classLoader.getResources(path).nextElement().toURI), regex)
    } else {
      Array[File]()
    }
  }

}

private class LogPrintWriter(logger: Logger)
  extends PrintWriter(new LogWriter(logger), true)

private class LogWriter(logger: Logger) extends Writer {
  override def close(): Unit = {}
  override def flush(): Unit = {}
  override def write(str: String): Unit = logger.info(str)
  override def write(cbuf: Array[Char], off: Int, len: Int): Unit = {
    if (len > 0)
      write(new String(cbuf.slice(off, off+len)))
  }
}