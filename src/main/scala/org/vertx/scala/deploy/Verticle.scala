package org.vertx.scala.deploy

import org.vertx.scala.core.Vertx

trait Verticle {

  var vertx: Vertx = null

  var container: Container = null

  @throws(classOf[Exception])
  def start(): Unit

  @throws(classOf[Exception])
  def stop(): Unit = {
    // NO-OP
  }

}