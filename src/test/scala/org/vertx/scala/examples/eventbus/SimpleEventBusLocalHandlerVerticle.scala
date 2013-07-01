package org.vertx.scala.examples.eventbus

import org.vertx.scala.platform.Verticle
import org.vertx.java.core.Future
import org.vertx.scala.core.eventbus.Message

class SimpleEventBusLocalHandlerVerticle extends Verticle {

  override def start(future: Future[Void]):Unit = {
    start()
    vertx.eventBus.registerLocalHandler("echo")((msg: Message[String]) => {
        msg.reply(msg.body)
      })

    future.setResult(null)
  }

}