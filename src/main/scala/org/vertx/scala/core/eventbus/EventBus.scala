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

import org.vertx.java.core.eventbus.{ EventBus => JEventBus, Message => JMessage }
import org.vertx.scala.VertxWrapper
import org.vertx.scala.core.{ AsyncResult, Handler }
import org.vertx.scala.core.FunctionConverters.fnToHandler

/**
 * @author <a href="http://www.campudus.com/">Joern Bernhardt</a>
 */
class EventBus(protected[this] val internal: JEventBus) extends VertxWrapper {
  override type InternalType = JEventBus

  private val handlers: Map[Any, Handler[_ <: JMessage[_]]] = Map.empty()

  sealed private trait SendOrPublish
  private case class Publish(address: String, value: MessageData) extends SendOrPublish
  private case class Send[X](address: String, value: MessageData, replyHandler: Option[Handler[JMessage[X]]]) extends SendOrPublish

  def publish[T <% MessageData](address: String, value: T): EventBus =
    sendOrPublish(Publish(address, value))

  def send[T <% MessageData](address: String, value: T): EventBus =
    sendOrPublish(Send(address, value, None))

  def send[T <% MessageData](address: String, value: T, handler: Message[T] => Unit): EventBus = {
    sendOrPublish(Send(address, value, Some(mapHandler(handler))))
  }

  def close(doneHandler: AsyncResult[Void] => Unit): Unit = internal.close(doneHandler)

  def registerUnregisterableHandler[X](address: String, handler: Handler[org.vertx.java.core.eventbus.Message[X]]): EventBus = wrap({
    internal.registerHandler(address, handler)
  })

  def registerUnregisterableHandler[X](address: String, handler: Handler[org.vertx.java.core.eventbus.Message[X]], resultHandler: AsyncResult[Void] => Unit): EventBus = wrap({
    internal.registerHandler(address, handler, resultHandler)
  })

  /**
   * Please bear in mind that you cannot unregister handlers you register with this method.
   */
  def registerHandler[T <% MessageData](address: String, handler: Message[T] => Unit): EventBus = wrap({
    internal.registerHandler(address, handler)
  })

  /**
   * Please bear in mind that you cannot unregister handlers you register with this method.
   */
  def registerHandler[T <% MessageData](address: String, handler: Message[T] => Unit, resultHandler: AsyncResult[Void] => Unit): EventBus = wrap({
    internal.registerHandler(address, handler, resultHandler)
  })

  def registerLocalHandler[T <% MessageData](address: String, handler: Message[T] => Unit): EventBus = wrap({
    internal.registerLocalHandler(address, handler)
  })

  def unregisterHandler[T <% MessageData](address: String, handler: Handler[JMessage[T]]): EventBus = wrap({
    internal.unregisterHandler(address, handler)
  })

  def unregisterHandler[T <% MessageData](address: String, handler: Handler[JMessage[T]], resultHandler: AsyncResult[Void] => Unit): EventBus = wrap({
    internal.unregisterHandler(address, handler, resultHandler)
  })

  private def mapHandler[T <% MessageData](handler: Message[T] => Unit): Handler[JMessage[T]] = {
    fnToHandler(handler compose { (sth: JMessage[T]) => Message.apply(sth) })
  }

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
