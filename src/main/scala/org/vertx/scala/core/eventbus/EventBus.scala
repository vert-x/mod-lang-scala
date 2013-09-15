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

import scala.collection.concurrent.Map

import org.vertx.java.core.{ AsyncResult, Handler }
import org.vertx.java.core.eventbus.{ EventBus => JEventBus, Message => JMessage }
import org.vertx.scala.VertxWrapper
import org.vertx.scala.core.FunctionConverters.convertFunctionToParameterisedHandler

/**
 * @author <a href="http://www.campudus.com/">Joern Bernhardt</a>
 */
class EventBus(protected[this] val internal: JEventBus) extends VertxWrapper {
  override type InternalType = JEventBus

  val handlers: Map[Message[MessageData] => Unit, Handler[_ <: JMessage[_]]] = Map.empty()

  sealed private trait SendOrPublish
  private case class Publish(address: String, value: MessageData) extends SendOrPublish
  private case class Send[X](address: String, value: MessageData, replyHandler: Option[Handler[JMessage[X]]]) extends SendOrPublish

  def publish[T <% MessageData](address: String, value: T): EventBus =
    sendOrPublish(Publish(address, value))

  def send[T <% MessageData](address: String, value: T): EventBus =
    sendOrPublish(Send(address, value, None))

  def send[T <% MessageData](address: String, value: T, handler: Message[MessageData] => Unit): EventBus = {
    val mappedHandler = convertFunctionToParameterisedHandler(handler compose { sth: JMessage[T] => Message.apply(sth) })
    handlers.getOrElseUpdate(handler, mappedHandler)
    sendOrPublish(Send(address, value, Some(mappedHandler)))
  }

  def close(x$1: AsyncResult[Void] => Unit): Unit = ???

  //  def registerHandler[T: MessageType, HT <: Message[T]](x$1: String, x$2: HT => Unit): EventBus = wrap(internal.registerHandler(x$1, x$2))
  //  def registerHandler[T: MessageType, HT <: Message[T]](x$1: String, x$2: HT => Unit, x$3: AsyncResult[Void] => Unit): EventBus = ???
  //  def registerLocalHandler[T: MessageType, HT <: Message[T]](x$1: String, x$2: HT => Unit): EventBus = ???
  //  def unregisterHandler[T: MessageType, HT <: Message[T]](x$1: String, x$2: HT => Unit): EventBus = ???
  //  def unregisterHandler[T: MessageType, HT <: Message[T]](x$1: String, x$2: HT => Unit, x$3: AsyncResult[Void] => Unit): EventBus = ???

  private def sendOrPublish(cmd: SendOrPublish): EventBus = {
    cmd match {
      case Publish(address, value) => value.publish(internal, address)
      case Send(address, value, None) => value.send(internal, address)
      case Send(address, value, Some(handler)) => value.send(internal, address, handler)
    }
    this
  }
}

/*
 * @author swilliams
 * @author Edgar Chan
 */
object EventBus {
  def apply(actual: JEventBus) = new EventBus(actual)
}
