package org.vertx.scala.examples.eventbus

import org.vertx.scala.platform.Verticle
import org.vertx.java.core.Future
import org.vertx.scala.core.eventbus.Message
import org.vertx.java.core.AsyncResult

class SimpleEventBusReplyHandlerVerticle extends Verticle {

  override def start(future: Future[Void]):Unit = {
    start()
    vertx.eventBus.registerHandler("echo")(
      { msg: Message[String] =>
        // FIXME should the following also work? msg.reply("foo") { r: Message[String] => /* etc */ }
        msg.reply(msg.body, { reply: Message[String] =>
          reply.reply(reply.body)
        })
      },
      { result: AsyncResult[Unit] =>
          if (result.succeeded()) {
            future.setResult(null)
          }
          else {
            future.setFailure(result.cause())
          }
      })
  }

}