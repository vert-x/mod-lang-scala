package org.vertx.scala.deploy

import java.io.File
import java.net.URL
import org.vertx.java.deploy.impl.Deployment
import org.vertx.java.core.Handler
import org.vertx.java.core.json.JsonObject
import org.vertx.java.core.logging.Logger

class Container(delegate: org.vertx.java.deploy.Container) {

  def deployModule(name: String):Unit = {
    delegate.deployModule(name);
  }

  def deployModule(name: String, instances: Int):Unit = {
    delegate.deployModule(name, instances);
  }

  def deployModule(name: String, config: JsonObject):Unit = {
    delegate.deployModule(name, config);
  }

  def deployModule(name: String, config: JsonObject, instances: Int):Unit = {
    delegate.deployModule(name, config, instances);
  }

  def deployModule(name: String, config: JsonObject, instances: Int, handler: (String) => Unit):Unit = {
    delegate.deployModule(name, config, instances, new FunctionHandler1(handler));
  }

  def deployVerticle(name: String):Unit = {
    delegate.deployVerticle(name);
  }

  def deployVerticle(name: String, instances: Int):Unit = {
    delegate.deployVerticle(name, instances);
  }

  def deployVerticle(name: String, config: JsonObject):Unit = {
    delegate.deployVerticle(name, config);
  }

  def deployVerticle(name: String, config: JsonObject, instances: Int):Unit = {
    delegate.deployVerticle(name, config, instances);
  }

  def deployVerticle(name: String, config: JsonObject, instances: Int, handler: (String) => Unit):Unit = {
    delegate.deployVerticle(name, config, instances, new FunctionHandler1(handler));
  }

  def deployWorkerVerticle(name: String):Unit = {
    delegate.deployWorkerVerticle(name);
  }

  def deployWorkerVerticle(name: String, config: JsonObject):Unit = {
    delegate.deployWorkerVerticle(name, config);
  }

  def deployWorkerVerticle(name: String, instances: Int):Unit = {
    delegate.deployWorkerVerticle(name, instances);
  }

  def deployWorkerVerticle(name: String, config: JsonObject, instances: Int):Unit = {
    delegate.deployWorkerVerticle(name, config, instances);
  }

  def deployWorkerVerticle(name: String, config: JsonObject, instances: Int, handler: (String) => Unit):Unit = {
    delegate.deployWorkerVerticle(name, config, instances, new FunctionHandler1(handler));
  }

  def config():JsonObject = delegate.getConfig()

  def env():java.util.Map[String, String] = { 
    var map: java.util.Map[String, String] = delegate.getEnv()
    map // TODO return a scala Map
  }

  def exit():Unit = delegate.exit()

  def logger():Logger = delegate.getLogger()

  def undeployModule(deploymentID: String):Unit = {
    delegate.undeployModule(deploymentID)
  }

  def undeployModule(deploymentID: String, handler: () => Unit):Unit = {
    delegate.undeployModule(deploymentID, new FunctionHandler0(handler))
  }

  def undeployVerticle(deploymentID: String):Unit = {
    delegate.undeployVerticle(deploymentID)
  }

  def undeployVerticle(deploymentID: String, handler: () => Unit):Unit = {
    delegate.undeployVerticle(deploymentID, new FunctionHandler0(handler))
  }

}