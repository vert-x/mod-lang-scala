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
package org.vertx.scala.core.eventbus

import org.vertx.java.core.eventbus.{ Message => JMessage }
import org.vertx.scala.core.FunctionConverters._
import org.vertx.scala.AsJava
import org.vertx.scala.core.AsyncResult
import org.vertx.scala.core.Handler

/**
 * Represents a message on the event bus.<p>
 *
 * Instances of this class are not thread-safe<p>
 *
 * @author <a href="http://tfox.org">Tim Fox</a>
 * @author <a href="http://www.campudus.com/">Joern Bernhardt</a>
 * @author Galder Zamarreño
 */
final class Message[T <% MessageData] private[scala] (val asJava: JMessage[T]) {

  /**
   * The address the message was sent to
   */
  def address(): String = asJava.address()

  /**
   * The body of the message.
   */
  def body(): T = anyToMessageData(asJava.body()).data.asInstanceOf[T]

  /**
   * The reply address (if any).
   *
   * @return An optional String containing the reply address.
   */
  def replyAddress(): Option[String] = Option(asJava.replyAddress())

  /**
   * Reply to this message. If the message was sent specifying a reply handler, that handler will be
   * called when it has received a reply. If the message wasn't sent specifying a receipt handler
   * this method does nothing.
   */
  def reply(): Unit = asJava.reply()

  /**
   * Reply to this message. If the message was sent specifying a reply handler, that handler will be
   * called when it has received a reply. If the message wasn't sent specifying a receipt handler
   * this method does nothing.
   *
   * @param value The data to send with the reply.
   */
  def reply(value: MessageData): Unit = value.reply(asJava)

  /**
   * The same as {@code reply(MessageData)} but you can specify handler for the reply - i.e.
   * to receive the reply to the reply.
   *
   * @param value The value to send.
   * @param handler Handling the reply.
   */
  def reply[B <% MessageData](value: MessageData, handler: Message[B] => Unit): Unit = value.reply(asJava, fnToHandler(handler.compose(Message.apply)))

  /**
   * The same as {@code reply()} but you can specify handler for the reply - i.e.
   * to receive the reply to the reply.
   *
   * @param handler Handling the reply.
   */
  def reply[B <% MessageData](handler: Message[B] => Unit): Unit = asJava.reply(messageFnToJMessageHandler(handler))

  /**
   * Reply to this message. Specifying a timeout and a reply handler.
   *
   * @param timeout The timeout in ms to wait for an answer.
   * @param handler Handling the reply (success) or the timeout (failed).
   */
  def replyWithTimeout[T <% MessageData](timeout: Long, replyHandler: AsyncResult[Message[T]] => Unit): Unit =
    asJava.replyWithTimeout(timeout, asyncResultConverter({ x: JMessage[T] => Message.apply(x) })(replyHandler))

  /**
   * Reply to this message with data. Specifying a timeout and a reply handler.
   *
   * @param value The value to send.
   * @param timeout The timeout in ms to wait for an answer.
   * @param handler Handling the reply (success) or the timeout (failed).
   */
  def replyWithTimeout[T <% MessageData](value: MessageData, timeout: Long, replyHandler: AsyncResult[Message[T]] => Unit): Unit =
    value.replyWithTimeout(asJava, timeout, convertArHandler(replyHandler))

  /**
   * Signal that processing of this message failed. If the message was sent specifying a result handler
   * the handler will be called with a failure corresponding to the failure code and message specified here
   * @param failureCode A failure code to pass back to the sender
   * @param message A message to pass back to the sender
   */
  def fail(failureCode: Int, message: String): Unit = asJava.fail(failureCode, message)

  private def convertArHandler[T <% MessageData](handler: AsyncResult[Message[T]] => Unit): Handler[AsyncResult[JMessage[T]]] = {
    asyncResultConverter({x: JMessage[T] => Message.apply(x)})(handler)
  }
}

/**
 * Companion object for Message.
 *
 * @author pidster
 * @author <a href="http://www.campudus.com/">Joern Bernhardt</a>
 */
object Message {
  def apply[X <% MessageData](jmessage: JMessage[X]): Message[X] = new Message(jmessage)
}
