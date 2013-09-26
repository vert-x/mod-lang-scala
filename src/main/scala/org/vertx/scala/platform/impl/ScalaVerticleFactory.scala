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

import scala.collection.mutable
import scala.reflect.internal.util.BatchSourceFile
import scala.reflect.io.Path.string2path
import scala.reflect.io.PlainFile
import scala.tools.nsc.interpreter.IMain
import scala.tools.nsc.Settings
import org.vertx.java.core.logging.Logger
import org.vertx.java.core.{ Vertx => JVertx }
import org.vertx.java.platform.{ Container => JContainer }
import org.vertx.java.platform.{ Verticle => JVerticle }
import org.vertx.java.platform.VerticleFactory
import org.vertx.scala.platform.Verticle
import java.io.{ PrintWriter, FilenameFilter, File }
import scala.util.matching.Regex
import org.vertx.scala.lang.ScalaInterpreter
import org.vertx.scala.core
import scala.tools.nsc.interpreter.Results.{ Success, Result }

/**
 * @author swilliams
 * @author Ranie Jade Ramiso
 * @author Galder Zamarreño
 */
class ScalaVerticleFactory extends VerticleFactory {

  import org.vertx.scala.core._

  import ScalaVerticleFactory._

  protected val SUFFIX: String = ".scala"

  private var jvertx: JVertx = null

  private var jcontainer: JContainer = null

  private var loader: ClassLoader = null

  private var interpreter: ScalaInterpreter = null

  private val classLoader = classOf[ScalaVerticleFactory].getClassLoader

  // private val classCache = mutable.Map[String, java.lang.Class[_]]()

  override def init(jvertx: JVertx, jcontainer: JContainer, aloader: ClassLoader): Unit = {
    this.jvertx = jvertx
    this.jcontainer = jcontainer
    this.loader = aloader

    val sVertx = Vertx(jvertx)
    val settings = interpreterSettings()
    interpreter = new ScalaInterpreter(settings, sVertx)
  }

  @throws(classOf[Exception])
  override def createVerticle(main: String): JVerticle = {
    val loadedVerticle = if (!main.endsWith(SUFFIX)) Some(loader.loadClass(main)) else load(main)
    loadedVerticle match {
      case Some(verticleClass) =>
        val delegate = verticleClass.newInstance().asInstanceOf[Verticle]
        ScalaVerticle.newVerticle(delegate, jvertx, jcontainer)
      case None =>
        DummyVerticle // run directly as script
    }
  }

  override def reportException(logger: Logger, t: Throwable): Unit = {
    logger.error("Scala verticle threw exception", t)
  }

  def close(): Unit = {
    interpreter.close()
  }

  @throws(classOf[Exception])
  private def load(verticlePath: String): Option[Class[_]] = {
    // Try running it as a script
    val result = interpreter.runScript(new File(verticlePath))
    if (result != Success) {
      // Might be a Scala class
      val resolved = loader.getResource(verticlePath).toExternalForm
      val className = verticlePath.replaceFirst(".scala$", "").replaceAll("/", ".")
      val classFile = new File(resolved.replaceFirst("file:", ""))
      val verticleClass = interpreter.compileClass(classFile, className).getOrElse {
        throw new Exception(s"$verticlePath is neither a Scala script nor a Scala class")
      }
      Some(verticleClass)
    } else {
      None
    }
  }

  private def interpreterSettings(): Settings = {
    val settings = new Settings()

    for {
      jar <- findAll(classLoader, "lib", JarFileRegex)
    } yield {
      settings.bootclasspath.append(jar.getAbsolutePath)
    }

    val modLangScala = classLoader.getResource("./").toExternalForm
    settings.bootclasspath.append(modLangScala.replaceFirst("file:", ""))
    settings.usejavacp.value = true
    settings.verbose.value = ScalaInterpreter.isVerbose
    settings
  }

  private object DummyVerticle extends JVerticle

}

object ScalaVerticleFactory {

  val JarFileRegex = "^(.*\\.jar)$".r

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
    if (classLoader.getResources(path).hasMoreElements()) {
      findAll(new File(classLoader.getResources(path).nextElement().toURI), regex)
    } else {
      Array[File]()
    }
  }

}