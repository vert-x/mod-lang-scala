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

package org.vertx.scala.platform

import scala.language.implicitConversions
import scala.collection.JavaConverters._
import org.vertx.java.platform.{ Container => JContainer }
import org.vertx.scala.core.AsyncResult
import org.vertx.scala.core.json._
import org.vertx.scala.core.FunctionConverters._
import org.vertx.scala.core.logging.Logger
import org.vertx.scala.VertxWrapper

/**
 * @author swilliams
 */
object Container {
  def apply(actual: JContainer) = new Container(actual)
}

class Container(val internal: JContainer) extends VertxWrapper {
  override type InternalType = JContainer

  private val defaultJsConfig = Json.emptyObj()

  def deployModule(name: String, config: JsonObject = defaultJsConfig, instances: Int = 1, handler: AsyncResult[String] => Unit = { ar: AsyncResult[String] => }): Unit = internal.deployModule(name, config, instances, handler)

  def deployVerticle(name: String, config: JsonObject = defaultJsConfig, instances: Int = 1, handler: AsyncResult[String] => Unit = { ar: AsyncResult[String] => }): Unit = internal.deployVerticle(name, config, instances, handler)

  def deployWorkerVerticle(name: String, config: JsonObject = defaultJsConfig, instances: Int = 1, multithreaded: Boolean = false, handler: AsyncResult[String] => Unit = { ar: AsyncResult[String] => }): Unit = internal.deployWorkerVerticle(name, config, instances, multithreaded, handler)

  def config(): JsonObject = internal.config()

  def env(): Map[String, String] = mapAsScalaMapConverter(internal.env()).asScala.toMap

  def exit(): Unit = internal.exit

  def logger(): Logger = new Logger(internal.logger())

  def undeployModule(deploymentID: String, handler: () => Unit): Unit = internal.undeployModule(deploymentID, handler)

  def undeployVerticle(deploymentID: String, handler: () => Unit): Unit = internal.undeployVerticle(deploymentID, handler)

}