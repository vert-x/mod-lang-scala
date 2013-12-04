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

/**
 * This class represents a Verticle's view of the container in which it is running.<p>
 * An instance of this class will be created by the system and made available to
 * a running Verticle.<p>
 * It contains methods to programmatically deploy other verticles, undeploy
 * verticles, deploy modules, get the configuration for a verticle and get the logger for a
 * verticle, amongst other things.<p>
 *
 * @author <a href="http://tfox.org">Tim Fox</a>
 * @author swilliams
 * @author <a href="http://www.campudus.com/">Joern Bernhardt</a>
 * @author Galder Zamarreño
 */
// constructor is private because users should use apply in companion
// extends AnyVal to avoid object allocation and improve performance
final class Container private[scala] (val asJava: JContainer) extends AnyVal {

  /**
   * Deploy a module programmatically
   * @param name The main of the module to deploy
   * @param config JSON config to provide to the module
   * @param instances The number of instances to deploy (defaults to 1)
   * @param handler The handler will be called passing in the unique deployment id when deployment is complete
   */
  def deployModule(name: String, config: JsonObject = Json.emptyObj(), instances: Int = 1, handler: AsyncResult[String] => Unit = { ar: AsyncResult[String] => }): Unit =
    asJava.deployModule(name, config, instances, handler)

  /**
   * Deploy a verticle programmatically
   * @param name The main of the verticle
   * @param config JSON config to provide to the verticle
   * @param instances The number of instances to deploy (defaults to 1)
   * @param handler The handler will be called passing in the unique deployment id when  deployment is complete
   */
  def deployVerticle(name: String, config: JsonObject = Json.emptyObj(), instances: Int = 1, handler: AsyncResult[String] => Unit = { ar: AsyncResult[String] => }): Unit =
    asJava.deployVerticle(name, config, instances, handler)

  /**
   * Deploy a worker verticle programmatically
   * @param name The main of the verticle
   * @param config JSON config to provide to the verticle (defaults to empty JSON)
   * @param instances The number of instances to deploy (defaults to 1)
   * @param multiThreaded if true then the verticle will be deployed as a multi-threaded worker (default is false)
   * @param handler The handler will be called passing in the unique deployment id when deployment is complete
   */
  def deployWorkerVerticle(name: String, config: JsonObject = Json.emptyObj(), instances: Int = 1, multiThreaded: Boolean = false, handler: AsyncResult[String] => Unit = { ar: AsyncResult[String] => }): Unit =
    asJava.deployWorkerVerticle(name, config, instances, multiThreaded, handler)

  /**
   * Get the verticle configuration
   * @return a JSON object representing the configuration
   */
  def config(): JsonObject = asJava.config()

  /**
   * Get an umodifiable map of system, environment variables.
   * @return The map
   */
  def env(): Map[String, String] = mapAsScalaMapConverter(asJava.env()).asScala.toMap

  /**
   * Cause the container to exit
   */
  def exit(): Unit = asJava.exit()

  /**
   * Get the verticle logger
   * @return The logger
   */
  def logger(): Logger = Logger(asJava.logger())

  /**
   * Undeploy a module
   * @param deploymentID The deployment ID
   * @param handler The handler will be called when undeployment is complete
   */
  def undeployModule(deploymentID: String, handler: () => Unit): Unit = asJava.undeployModule(deploymentID, handler)

  /**
   * Undeploy a module
   * @param deploymentID The deployment ID
   * @param handler The handler will be called when undeployment is complete
   */
  def undeployVerticle(deploymentID: String, handler: () => Unit): Unit = asJava.undeployVerticle(deploymentID, handler)

}

/** Factory for [[org.vertx.scala.platform.Container]] instances. */
object Container {
  def apply(actual: JContainer) = new Container(actual)
}
