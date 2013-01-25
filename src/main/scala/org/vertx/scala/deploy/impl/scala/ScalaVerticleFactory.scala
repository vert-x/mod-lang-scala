/*
 * Copyright 2011-2012 the original author or authors.
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

package org.vertx.scala.deploy.impl.scala

import org.vertx.java.core.impl.VertxInternal
import org.vertx.java.deploy.impl.VerticleManager
import org.vertx.java.deploy.{Verticle => JVerticle}
import org.vertx.java.deploy.impl.VerticleFactory
import org.vertx.java.deploy.impl.VertxLocator
import org.vertx.scala.core.Vertx
import org.vertx.scala.deploy.Container
import org.vertx.scala.deploy.Verticle
import org.vertx.java.deploy.impl.ModuleClassLoader
import scala.reflect.internal.util.BatchSourceFile
import scala.reflect.io.PlainFile
import scala.tools.nsc.interpreter.IMain
import scala.tools.nsc.interpreter.AbstractFileClassLoader
import scala.tools.nsc.Settings


class ScalaVerticleFactory extends VerticleFactory {

  protected val PREFIX: String = "scala:"

  protected val SUFFIX: String = ".scala"

  private var manager: VerticleManager = null

  private var mcl: ModuleClassLoader = null

  def init(amanager: VerticleManager, amcl: ModuleClassLoader): Unit = {
    manager = amanager
    mcl = amcl
  }

  @throws(classOf[Exception])
  def createVerticle(main: String): JVerticle = {

    var verticle: Verticle = null
    if (main.endsWith(SUFFIX)) {
      val abstractFileClassLoader: AbstractFileClassLoader = compileScalaScript(main)
      // TODO parse non-relative URLs
      val fqn = main.substring(main.lastIndexOf('/') + 1, main.lastIndexOf('.'))
      verticle = abstractFileClassLoader.loadClass(fqn).newInstance().asInstanceOf[Verticle]
    }
    else {
      val className = if (main.startsWith(PREFIX)) main.replaceFirst(PREFIX, "") else main
      val rawClass = mcl.loadClass(className)
      verticle = rawClass.newInstance().asInstanceOf[Verticle]
    }

    new ScalaVerticle(verticle)
  }

  def reportException(t: Throwable): Unit = manager.getLogger().error("oops!", t)

  def compileScalaScript(filePath: String):AbstractFileClassLoader = {
    val settings = new Settings()
    settings.usejavacp.value = true

    val interpreter = new IMain(settings)
    interpreter.compileSources(new BatchSourceFile(PlainFile.fromPath(filePath)))
    interpreter.classLoader
  }

}