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

package org.vertx.scala.deploy

import java.io.File
import java.net.URL
import org.vertx.java.deploy.{Container => JContainer}
import org.vertx.java.deploy.impl.Deployment
import org.vertx.java.deploy.impl.VertxLocator
import org.vertx.java.core.logging.Logger
import org.vertx.scala.JSON._
import org.vertx.scala.handlers.FunctionHandler0
import org.vertx.scala.handlers.FunctionHandler1
import scala.util.parsing.json.JSONObject
import scala.collection.JavaConverters._
import org.vertx.java.core.json.JsonObject

/**
 * @author swilliams
 * 
 */
object Container {

  def apply(actual: JContainer) =
    new Container(actual)

  def find() =
    new Container(VertxLocator.container)
}

class Container(internal: JContainer) {

  def deployModule(name: String, config: JSONObject = null, instances: Int = 1, handler: (String) => Unit = {msg: String => }):Unit = 
      internal.deployModule(name, config, instances, new FunctionHandler1(handler))

  def deployVerticle(name: String, config: JSONObject = null, instances: Int = 1, handler: (String) => Unit = {msg: String => }):Unit =
    internal.deployVerticle(name, config, instances, new FunctionHandler1(handler))

  def deployWorkerVerticle(name: String, config: JSONObject = null, instances: Int = 1, handler: (String) => Unit = {msg: String => }):Unit =
    internal.deployWorkerVerticle(name, config, instances, new FunctionHandler1(handler))

  def config():JSONObject = internal.getConfig

  def env():Map[String, String] = { 
    mapAsScalaMapConverter(internal.getEnv()).asScala.toMap
  }

  def exit():Unit = internal.exit

  def logger():Logger = internal.getLogger

  def undeployModule(deploymentID: String):Unit = internal.undeployModule(deploymentID)

  def undeployModule(deploymentID: String, handler: () => Unit):Unit =
    internal.undeployModule(deploymentID, new FunctionHandler0(handler))

  def undeployVerticle(deploymentID: String):Unit = internal.undeployVerticle(deploymentID)

  def undeployVerticle(deploymentID: String, handler: () => Unit):Unit =
    internal.undeployVerticle(deploymentID, new FunctionHandler0(handler))

}