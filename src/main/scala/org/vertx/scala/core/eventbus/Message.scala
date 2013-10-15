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

class Message[T <% MessageData](internal: JMessage[T]) {

  /**
   * The body of the message.
   */
  def body(): T = anyToMessageData(internal.body()).data.asInstanceOf[T]

  /**
   * The reply address (if any).
   *
   * @return An optional String containing the reply address.
   */
  def replyAddress(): Option[String] = Option(internal.replyAddress())

  /**
   * Reply to this message. If the message was sent specifying a reply handler, that handler will be
   * called when it has received a reply. If the message wasn't sent specifying a receipt handler
   * this method does nothing.
   */
  def reply() = internal.reply()

  /**
   * Reply to this message. If the message was sent specifying a reply handler, that handler will be
   * called when it has received a reply. If the message wasn't sent specifying a receipt handler
   * this method does nothing.
   *
   * @param value Some data to send with the reply.
   */
  def reply(value: MessageData) = value.reply(internal)

  /**
   * The same as {@code reply(MessageData)} but you can specify handler for the reply - i.e.
   * to receive the reply to the reply.
   */
  def reply[B <% MessageData](value: MessageData, handler: Message[B] => Unit) = value.reply(internal, fnToHandler(handler.compose(Message.apply)))

  /**
   * The same as {@code reply()} but you can specify handler for the reply - i.e.
   * to receive the reply to the reply.
   */
  def reply[B <% MessageData](handler: Message[B] => Unit) = internal.reply(messageFnToJMessageHandler(handler))
}

/**
 * @author pidster
 * @author <a href="http://www.campudus.com/">Joern Bernhardt</a>
 */
object Message {
  def apply[X <% MessageData](jmessage: JMessage[X]): Message[X] = new Message(jmessage)
}
