package org.vertx.test

import org.vertx.scala.deploy.Verticle
import org.vertx.java.core.eventbus.Message
import org.vertx.java.core.http.{ServerWebSocket => JServerWebSocket}

class SimpleVerticle extends Verticle {

  @throws(classOf[Exception])
  def start(): Unit = {

    val closure = () => { Thread.sleep(2000L); print("hello ") }
    vertx.runOnLoop( closure )
    vertx.runOnLoop( () => { println("world") } )

    vertx.eventBus.send("test.echo", "echo!", (msg: Message[String]) => {
      printf("echo received: %s%n", msg.body)
    })

    vertx.sharedData.map("one")

    vertx
      .createHttpServer
      .websocketHandler({s: JServerWebSocket => })
      .listen(9090)

    println("started after hello world!")
  }

  @throws(classOf[Exception])
  override def stop(): Unit = {
    super.stop
    println("stopped!")
  }
}