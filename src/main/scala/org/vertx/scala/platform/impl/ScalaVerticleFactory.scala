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
import org.vertx.java.core.{Vertx => JVertx}
import org.vertx.java.platform.{Container => JContainer}
import org.vertx.java.platform.{Verticle => JVerticle}
import org.vertx.java.platform.VerticleFactory
import org.vertx.scala.platform.Verticle

/**
 * @author swilliams
 * 
 */
class ScalaVerticleFactory extends VerticleFactory {

  protected val SUFFIX: String = ".scala"

  private val settings = new Settings()

  private var jvertx: JVertx = null

  private var jcontainer: JContainer = null

  private var loader: ClassLoader = null

  private var interpreter: IMain = null

  private val classLoader = classOf[ScalaVerticleFactory].getClassLoader

  private val classCache = mutable.Map[String, java.lang.Class[_]]()

  override def init(jvertx: JVertx, jcontainer: JContainer, aloader: ClassLoader): Unit = {
    this.jvertx = jvertx
    this.jcontainer = jcontainer
    this.loader = aloader

    println("HELLO WORLD")

    initializeScalaInterpreter()
  }

  @throws(classOf[Exception])
  override def createVerticle(main: String): JVerticle = {
    val rawClass = if (!main.endsWith(SUFFIX)) loader.loadClass(main) else loadScript(main)
    val delegate = rawClass.newInstance().asInstanceOf[Verticle]
    ScalaVerticle.newVerticle(delegate, jvertx, jcontainer)
  }

  override def reportException(logger: Logger, t: Throwable): Unit = {
    logger.error("Scala verticle threw exception", t)
  }

  def close(): Unit = {
    interpreter.close()
  }

  @throws(classOf[Exception])
  private def loadScript(main: String): Class[_] = {
    val resolved = loader.getResource(main).toExternalForm
    val className = main.replaceFirst(".scala$", "").replaceAll("/", ".")
    var cls = classCache.get(className).getOrElse(null)

    if (cls == null) {
      interpreter.compileSources(new BatchSourceFile(PlainFile.fromPath(resolved.replaceFirst("file:", ""))))
      cls = interpreter.classLoader.loadClass(className)
    }

    classCache += className -> cls

    cls
  }

  private def initializeScalaInterpreter(): Unit = {
    val scalaLibrary = classLoader.getResource("./lib/scala-library-2.10.2.jar").toExternalForm
    val scalaReflectLibrary = classLoader.getResource("./lib/scala-reflect-2.10.2.jar").toExternalForm
    val modLangScala = classLoader.getResource("./").toExternalForm

    settings.bootclasspath.append(scalaLibrary.replaceFirst("file:", ""))
    settings.bootclasspath.append(scalaReflectLibrary.replaceFirst("file:", ""))
    settings.bootclasspath.append(modLangScala.replaceFirst("file:", ""))

    settings.usejavacp.value = true
    settings.verbose.value = true
    interpreter = new IMain(settings)
    interpreter.classLoader
    interpreter.setContextClassLoader()
  }

}