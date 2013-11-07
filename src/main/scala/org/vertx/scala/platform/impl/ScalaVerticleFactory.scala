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
import scala.tools.nsc.interpreter.Results.Success
import scala.util.matching.Regex

import org.vertx.java.core.{ Vertx => JVertx }
import org.vertx.java.core.logging.Logger
import org.vertx.java.platform.{ Container => JContainer, Verticle => JVerticle, VerticleFactory }
import org.vertx.scala.lang.ScalaInterpreter
import org.vertx.scala.platform.{Container, Verticle}
import java.net.URL
import org.vertx.java.core.logging.impl.LoggerFactory
import java.security.{PrivilegedAction, AccessController}

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

  private var jvertx: JVertx = null

  private var jcontainer: JContainer = null

  private var loader: ClassLoader = null

  private var interpreter: ScalaInterpreter = null

  override def init(jvertx: JVertx, jcontainer: JContainer, aloader: ClassLoader): Unit = {
    this.jvertx = jvertx
    this.jcontainer = jcontainer
    this.loader = aloader

    val sVertx = Vertx(jvertx)
    val sContainer = Container(jcontainer)
    val settings = interpreterSettings()
    interpreter = new ScalaInterpreter(
        settings, sVertx, sContainer, new LogPrintWriter(logger))
  }

  @throws(classOf[Exception])
  override def createVerticle(main: String): JVerticle = {
    val loadedVerticle = 
      if (!main.endsWith(Suffix)) Some(loader.loadClass(main))
      else load(main)
    
    loadedVerticle match {
      case Some(verticleClass) =>
        val vcInstance = verticleClass.newInstance()
        val delegate = vcInstance.asInstanceOf[Verticle]
        ScalaVerticle.newVerticle(delegate, jvertx, jcontainer)
      case None =>
        DummyVerticle // run directly as script
    }
  }

  private def resolveVerticlePath(main: String): URL = {
    // Check if path exists
    val file = new File(main)
    if (file.exists())
      file.toURI.toURL
    else {
      val url = loader.getResource(main)
      if (url == null)
        throw new IllegalArgumentException(
            s"Cannot find main script: '$main' on classpath")
      url
    }
  }

  override def reportException(logger: Logger, t: Throwable): Unit = {
    logger.error("Scala verticle threw exception", t)
  }

  def close(): Unit = {
    interpreter.close()
  }

  @throws(classOf[Exception])
  private def load(main: String): Option[Class[_]] = {
    logger.info(s"Compiling $main as Scala script")
    // Try running it as a script
    val url = resolveVerticlePath(main)
    val result = interpreter.runScript(url)
    if (result != Success) {
      // Might be a Scala class
      logger.info(s"Script contains compilation errors, or $main is a Scala class (pass -Dvertx.scala.interpreter.verbose=true to find out more)")
      logger.info(s"Compiling as a Scala class")
      val resolved = loader.getResource(main).toExternalForm
      val className = main.replaceFirst(".scala$", "").replaceAll("/", ".")
      val classFile = new File(resolved.replaceFirst("file:", ""))
      val verticleClass = interpreter.compileClass(classFile, className).getOrElse {
        throw new IllegalArgumentException(
            s"Unable to run $main as neither Scala script nor Scala class")
      }
      logger.info(s"Starting $className")
      Some(verticleClass)
    } else {
      logger.info(s"Starting $main")
      None
    }
  }

  private def interpreterSettings(): Settings = {
    val settings = new Settings()

    for {
      jar <- findAll(loader, "lib", JarFileRegex)
    } yield {
      settings.bootclasspath.append(jar.getAbsolutePath)
    }

    val moduleLocation = getRootModuleLocation
    moduleLocation match {
      case None =>
        throw new IllegalStateException("Unable to resolve mod-lang-scala root location")
      case Some(loc) =>
        settings.bootclasspath.append(loc)
        settings.usejavacp.value = true
        settings.verbose.value = ScalaInterpreter.isVerbose
        settings
    }
  }

  private def getRootModuleLocation: Option[String] = {
    AccessController.doPrivileged(new PrivilegedAction[Option[String]]() {
      def run(): Option[String] = {
        for {
          pd <- Option(classOf[ScalaVerticleFactory].getProtectionDomain)
          cs <- Option(pd.getCodeSource)
          loc <- Option(cs.getLocation)
        } yield loc.toExternalForm
      }
    })
  }

  private object DummyVerticle extends JVerticle

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