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
import org.vertx.scala.core.FunctionConverters.fnToHandler

class Message[+T](internal: JMessage[T]) extends JMessage[T] {

  /**
   * The body of the message.
   */
  def body[X <: MessageData] = anyToMessageData(internal.body())

  /**
   * The reply address (if any)
   */
  def replyAddress: Option[String] = Option(internal.replyAddress())

  def reply(value: MessageData) = value.reply(internal)

  def reply[B](value: MessageData, handler: JMessage[B] => Unit) = value.reply(internal, fnToHandler(handler))
}

/**
 * @author pidster
 * @author <a href="http://www.campudus.com/">Joern Bernhardt</a>
 */
object Message {
  def apply[X](jmessage: JMessage[X]): Message[X] = new Message(jmessage)
}
