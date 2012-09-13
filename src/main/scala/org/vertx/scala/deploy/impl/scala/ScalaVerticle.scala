package org.vertx.scala.deploy.impl.scala

import org.vertx.scala.deploy.Container
import org.vertx.scala.deploy.Verticle
import org.vertx.scala.core.Vertx

final class ScalaVerticle(delegate: => Verticle) extends org.vertx.java.deploy.Verticle {

  override def setContainer(container: org.vertx.java.deploy.Container) {
    super.setContainer(container)
    delegate.container_=(new Container(container))
  }

  override def setVertx(vertx: org.vertx.java.core.Vertx) {
    super.setVertx(vertx)
    delegate.vertx_=(new Vertx(vertx))
  }

  @throws(classOf[Exception])
  override def start(): Unit = {
    delegate.start()
  }

  @throws(classOf[Exception])
  override def stop(): Unit = {
    delegate.stop()
  }

}