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

import org.vertx.java.core.{Vertx => JVertx}
import org.vertx.java.platform.{Verticle => JVerticle}
import org.vertx.java.platform.{Container => JContainer}
import org.vertx.java.platform.VerticleFactory
import org.vertx.scala.core.Vertx
import scala.reflect.internal.util.BatchSourceFile
import scala.reflect.io.Path.string2path
import scala.reflect.io.PlainFile
import scala.tools.nsc.interpreter.IMain
import scala.tools.nsc.Settings
import org.vertx.java.core.logging.Logger
import org.vertx.java.core.{Vertx => JVertx}
import org.vertx.java.platform.{Container => JContainer}
import org.vertx.java.platform.{Verticle => JVerticle}
import org.vertx.scala.platform.Verticle
import org.vertx.scala.platform.impl.ScalaVerticle

/**
 * @author swilliams
 * 
 */
class ScalaVerticleFactory extends VerticleFactory {

  protected val PREFIX: String = "scala:"

  private val settings = new Settings()

  private var vertx: Vertx = null

  private var container: JContainer = null

  private var loader: ClassLoader = null

  private var interpreter: IMain = null

  override def init(jvertx: JVertx, jcontainer: JContainer, aloader: ClassLoader): Unit = {
    this.vertx = new Vertx(jvertx)
    this.container = jcontainer
    this.loader = aloader
    settings.embeddedDefaults(aloader)
    settings.usejavacp.value = true
    // settings.verbose.value = true
    interpreter = new IMain(settings)
    interpreter.setContextClassLoader()
  }

  @throws(classOf[Exception])
  override def createVerticle(main: String): JVerticle = {
    val rawClass = if (main.startsWith(PREFIX)) loader.loadClass(main.replaceFirst(PREFIX, "")) else loadScript(main)
    val verticle = rawClass.newInstance().asInstanceOf[Verticle]
    ScalaVerticle(verticle) // this is required, to get the Scala flavoured Container
  }

  override def reportException(logger: Logger, t: Throwable): Unit = {
    logger.error("Scala verticle threw exception", t)
  }

  def close(): Unit = {
    interpreter.close()
  }

  @throws(classOf[Exception])
  private def loadScript(main: String):Class[_] = {
    val resolved = loader.getResource(main).toExternalForm()
    interpreter.compileSources(new BatchSourceFile(PlainFile.fromPath(resolved.replaceFirst("file:", ""))))
    val className = main.replaceFirst(".scala$", "").replaceAll("/", ".")
    interpreter.classLoader.loadClass(className)
  }

}