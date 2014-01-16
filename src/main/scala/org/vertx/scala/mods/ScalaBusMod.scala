/*
 * Copyright 2013 the original author or authors.
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
package org.vertx.scala.mods

import org.vertx.scala.core.VertxExecutionContext
import org.vertx.scala.core.eventbus.{ JsonObjectData, Message }
import org.vertx.scala.core.json.{ Json, JsonObject }
import org.vertx.scala.mods.replies._
import org.vertx.scala.core.VertxAccess
import org.vertx.scala.core.AsyncResult

/**
 * Extend this trait to get an easy to use interface for your EventBus module. It relies on the
 * sender sending JSON requests with an `{"action":"<something>"}`. It will reply with either a
 * `{"status":"ok"}` JSON object or an `{"status":"error", "error":"<ERROR_CODE>",
 * "message":"<detailed message of error>"}`. You can provide extra fields in your reply, by adding
 * a JsonObject to your reply (`Ok` or `Error`). If you need to do something asynchronously while
 * processing the message, use `AsyncReply`. You just need to map the `Future` to one of the `Ok` or
 * `Error` replies. Exceptions that you forget to catch will always yield a "MODULE_EXCEPTION" error
 * code.
 */
trait ScalaBusMod extends (Message[JsonObject] => Unit) with VertxExecutionContext with VertxAccess {

  import ScalaBusMod._

  private val noActionMatch: BusModReply = Error("No matching action.", "INVALID_ACTION")
  private val noAction: BusModReply = Error("No action received.", "MISSING_ACTION")

  override final def apply(msg: Message[JsonObject]) = {
    val reply: BusModReceiveEnd = Option(msg.body().getString("action")) match {
      case Some(action) =>
        try {
          receive(msg).applyOrElse(action, { _: String => noActionMatch })
        } catch {
          case ex: Throwable =>
            logger.warn("Uncaught Exception for request " + msg.body().encode(), ex)
            Error("Module threw error: " + ex.getMessage,
              "MODULE_EXCEPTION",
              Json.obj("exception" -> ex.getStackTrace.mkString("\n")))
        }
      case None => noAction
    }

    sendReply(msg, reply)
  }

  /**
   * Sends a reply back to the sender. This handles AsyncReplies, so Futures or errors in Futures
   * will get caught and reply with their reply type when they are done (or an `Error` in case of an
   * uncaught exception/failed `Future`).
   *
   * @param msg The message to reply to.
   * @param reply The BusModReply to send.
   */
  private def sendReply(msg: Message[_], reply: BusModReceiveEnd): Unit = reply match {
    case AsyncReply(future) => future.map(x => sendReply(msg, x)).recover {
      case BusModException(message, null, id) => sendFinalReply(msg, Error(message, id))
      case BusModException(message, cause, id) =>
        sendFinalReply(msg,
          Error(message, id, Json.obj("exception" -> cause.getStackTrace.mkString("\n"))))
      case ex =>
        sendFinalReply(msg,
          Error(ex.getMessage, "MODULE_EXCEPTION", Json.obj("exception" -> ex.getStackTrace.mkString("\n"))))
    }
    case syncReply: SyncReply => sendFinalReply(msg, syncReply)
    case NoReply =>
  }

  private def sendFinalReply(msg: Message[_], reply: SyncReply): Unit = reply.replyHandler match {
    case Some(replyReceiver) =>
      val receiver = new ScalaBusMod {
        override val container = ScalaBusMod.this.container
        override val vertx = ScalaBusMod.this.vertx
        override val logger = ScalaBusMod.this.logger
        override def receive = replyReceiver.handler
      }

      replyReceiver match {
        case ReceiverWithTimeout(handler, timeout, timeoutHandler) =>
          msg.replyWithTimeout(reply.toJson, timeout, (ar: AsyncResult[Message[JsonObject]]) => {
            if (ar.succeeded()) {
              receiver.apply(ar.result())
            } else {
              timeoutHandler()
            }
          })
        case _ => msg.reply(reply.toJson, receiver)
      }
    case None => msg.reply(reply.toJson)
  }

  /**
   * Override this method to handle a message received via the eventbus. This function reads the
   * `action` parameter out of the message and checks if the returned `PartialFunction` is defined
   * on it. With the returned `BusModReply`, you can easily tell whether the message resulted in an
   * error or everything went well by replying `Ok`.
   *
   * @param message The message that was received via the eventbus.
   * @return A partial function consisting of an "action" -> BusModReply.
   */
  def receive: Receive
}

object ScalaBusMod {
  type Receive = (Message[JsonObject]) => PartialFunction[String, BusModReceiveEnd]
}
