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

import scala.concurrent.Promise
import org.vertx.scala.core.{ Vertx, VertxExecutionContext }
import scala.concurrent.Future
import org.vertx.scala.core.logging.Logger
import org.vertx.scala.core.VertxAccess

/**
 * A verticle is the unit of execution in the Vert.x platform<p>
 * Vert.x code is packaged into Verticle's and then deployed and executed by the Vert.x platform.<p>
 * Verticles can be written in different languages.<p>
 *
 * @author <a href="http://tfox.org">Tim Fox</a>
 * @author swilliams
 * @author <a href="http://www.campudus.com/">Joern Bernhardt</a>
 */
trait Verticle extends VertxExecutionContext with VertxAccess {

  private var _vertx: Vertx = null
  private var _container: Container = null

  /**
   * A reference to the Vert.x runtime.
   * @return A reference to a Vertx.
   */
  lazy val vertx: Vertx = _vertx

  /**
   * A reference to the Vert.x container.
   * @return A reference to the Container.
   */
  lazy val container: Container = _container

  /**
   * 
   */
  lazy val logger: Logger = container.logger

  /**
   * Injects the vertx.
   */
  private[platform] def setVertx(newVertx: Vertx): Unit = _vertx = newVertx

  /**
   * Injects the container.
   */
  private[platform] def setContainer(newContainer: Container): Unit = _container = newContainer

  /**
   * Vert.x calls the start method when the verticle is deployed.
   */
  def start(): Unit = {}

  /**
   * Override this method to signify that start is complete sometime _after_ the start() method has returned.
   * This is useful if your verticle deploys other verticles or modules and you don't want this verticle to
   * be considered started until the other modules and verticles have been started.
   *
   * @param promise When you are happy your verticle is started set the result.
   */
  def start(promise: Promise[Unit]): Unit = promise.success()

  /**
   * Vert.x calls the stop method when the verticle is undeployed.
   * Put any cleanup code for your verticle in here
   */
  def stop(): Unit = {}

}