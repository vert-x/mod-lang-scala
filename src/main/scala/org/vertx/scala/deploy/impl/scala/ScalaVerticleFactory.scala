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
import org.vertx.java.deploy.VerticleFactory
import org.vertx.java.deploy.impl.VertxLocator
import org.vertx.scala.core.Vertx
import org.vertx.scala.deploy.Container
import org.vertx.scala.deploy.Verticle

class ScalaVerticleFactory extends VerticleFactory {

  protected val LANGUAGE: String = "scala"

  protected val PREFIX: String = "scala:"

  protected val SUFFIX: String = ".scala"

  private var manager: VerticleManager = null

  def init(amanager: VerticleManager): Unit = {
    manager = amanager
  }

  def getLanguage(): String = { LANGUAGE }

  def isFactoryFor(main: String): Boolean = {
    main.startsWith(PREFIX) || main.endsWith(SUFFIX)
  }

  @throws(classOf[Exception])
  def createVerticle(main: String, parent: ClassLoader): org.vertx.java.deploy.Verticle = {

    if (main.endsWith(SUFFIX)) {
      throw new RuntimeException("scala scripts are not yet supported")
    }

    var className: String = null
    if (main.startsWith(PREFIX)) {
      className = main.replaceFirst(PREFIX, "")
    }
    else {
      className = main
    }

    val rawClass = Class.forName(className, true, parent)
    val verticle = rawClass.newInstance().asInstanceOf[Verticle]

    new ScalaVerticle(verticle)
  }

  def reportException(t: Throwable): Unit = {
    manager.getLogger().error("oops!", t);
  }

}