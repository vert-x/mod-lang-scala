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
import org.vertx.scala.core.FunctionConverters._
import org.vertx.java.core.AsyncResultHandler
import org.vertx.java.core.eventbus.ReplyException
import org.vertx.java.core.impl.DefaultFutureResult

/**
 * A distributed lightweight event bus which can encompass multiple vert.x instances.
 * The event bus implements publish / subscribe, point to point messaging and request-response messaging.<p>
 * Messages sent over the event bus are represented by instances of the {@link Message} class.<p>
 * For publish / subscribe, messages can be published to an address using one of the {@link #publish} methods. An
 * address is a simple {@code String} instance.<p>
 * Handlers are registered against an address. There can be multiple handlers registered against each address, and a particular handler can
 * be registered against multiple addresses. The event bus will route a sent message to all handlers which are
 * registered against that address.<p>
 * For point to point messaging, messages can be sent to an address using one of the {@link #send} methods.
 * The messages will be delivered to a single handler, if one is registered on that address. If more than one
 * handler is registered on the same address, Vert.x will choose one and deliver the message to that. Vert.x will
 * aim to fairly distribute messages in a round-robin way, but does not guarantee strict round-robin under all
 * circumstances.<p>
 * All messages sent over the bus are transient. On event of failure of all or part of the event bus messages
 * may be lost. Applications should be coded to cope with lost messages, e.g. by resending them, and making application
 * services idempotent.<p>
 * The order of messages received by any specific handler from a specific sender should match the order of messages
 * sent from that sender.<p>
 * When sending a message, a reply handler can be provided. If so, it will be called when the reply from the receiver
 * has been received. Reply messages can also be replied to, etc, ad infinitum<p>
 * Different event bus instances can be clustered together over a network, to give a single logical event bus.<p>
 * Instances of EventBus are thread-safe.<p>
 * If handlers are registered from an event loop, they will be executed using that same event loop. If they are
 * registered from outside an event loop (i.e. when using Vert.x embedded) then Vert.x will assign an event loop
 * to the handler and use it to deliver messages to that handler.
 *
 * @author <a href="http://tfox.org">Tim Fox</a>
 * @author swilliams
 * @author Edgar Chan
 * @author <a href="http://www.campudus.com/">Joern Bernhardt</a>
 */
class EventBus(protected[this] val internal: JEventBus) extends VertxWrapper {
  override type InternalType = JEventBus

  sealed private trait SendOrPublish
  private case class Publish(address: String, value: MessageData) extends SendOrPublish
  private case class Send[X](address: String, value: MessageData,
    replyHandler: Option[Either[Handler[JMessage[X]], Handler[AsyncResult[JMessage[X]]]]])
    extends SendOrPublish

  /**
   * Publish a message.
   * @param address The address to publish it to
   * @param message The message
   */
  def publish[T <% MessageData](address: String, value: T): EventBus =
    sendOrPublish(Publish(address, value), -1)

  /**
   * Send a message.
   * @param address The address to send it to
   * @param message The message
   */
  def send[T <% MessageData](address: String, value: T): EventBus =
    sendOrPublish(Send(address, value, None), -1)

  /**
   * Send a message.
   * @param address The address to send it to
   * @param message The message
   * @param replyHandler Reply handler will be called when any reply from the recipient is received
   */
  def send[ST <% MessageData, RT <% MessageData](address: String, value: ST, handler: Message[RT] => Unit): EventBus = {
    sendOrPublish(Send(address, value, Some(Left(mapHandler(handler)))), -1)
  }

  /**
   * Send a character as a message
   * @param address The address to send it to
   * @param message The message
   * @param timeout - Timeout in ms. If no reply received within the timeout then the reply handler will be unregistered
   * @param replyHandler Reply handler will be called when any reply from the recipient is received
   */
  def sendWithTimeout[ST <% MessageData, RT <% MessageData](address: String, value: ST, timeout: Long, replyHandler: AsyncResult[Message[RT]] => Unit): EventBus =
    sendOrPublish(Send(address, value, Some(Right(convertArHandler(replyHandler)))), timeout)

  /**
   * Close the EventBus and release all resources.
   *
   * @param doneHandler
   */
  def close(doneHandler: AsyncResult[Void] => Unit): Unit = internal.close(doneHandler)

  /**
   * Registers a handler against the specified address.
   * @param address The address to register it at.
   * @param handler The handler.
   */
  def registerUnregisterableHandler[X](address: String, handler: Handler[org.vertx.java.core.eventbus.Message[X]]): EventBus = wrap({
    internal.registerHandler(address, handler)
  })

  /**
   * Registers a handler against the specified address.
   * @param address The address to register it at.
   * @param handler The handler.
   * @param resultHandler Optional completion handler. If specified, when the register has been
   * propagated to all nodes of the event bus, the handler will be called.
   */
  def registerUnregisterableHandler[X](address: String, handler: Handler[org.vertx.java.core.eventbus.Message[X]], resultHandler: AsyncResult[Void] => Unit): EventBus = wrap({
    internal.registerHandler(address, handler, resultHandler)
  })

  /**
   * Registers a handler against the specified address. Please bear in mind that you cannot
   * unregister handlers you register with this method.
   * @param address The address to register it at.
   * @param handler The handler.
   */
  def registerHandler[T <% MessageData](address: String, handler: Message[T] => Unit): EventBus =
    wrap(internal.registerHandler(address, fnToHandler(handler.compose(Message.apply))))

  /**
   * Registers a handler against the specified address. Please bear in mind that you cannot
   * unregister handlers you register with this method.
   * @param address The address to register it at.
   * @param handler The handler.
   * @param resultHandler Optional completion handler. If specified, when the register has been
   * propagated to all nodes of the event bus, the handler will be called.
   */
  def registerHandler[T <% MessageData](address: String, handler: Message[T] => Unit, resultHandler: AsyncResult[Void] => Unit): EventBus = wrap({
    internal.registerHandler(address, handler.compose(Message.apply), resultHandler)
  })

  /**
   * Registers a local handler against the specified address. The handler info won't
   * be propagated across the cluster. Please bear in mind that you cannot unregister handlers you
   * register with this method.
   * @param address The address to register it at
   * @param handler The handler
   */
  def registerLocalHandler[T <% MessageData](address: String, handler: Message[T] => Unit): EventBus = wrap({
    internal.registerLocalHandler(address, handler.compose(Message.apply))
  })

  /**
   * Unregisters a handler given the address and the handler
   * @param address The address the handler was registered at
   * @param handler The handler
   */
  def unregisterHandler[T <% MessageData](address: String, handler: Handler[JMessage[T]]): EventBus = wrap({
    internal.unregisterHandler(address, handler)
  })

  /**
   * Unregisters a handler given the address and the handler
   * @param address The address the handler was registered at
   * @param handler The handler
   * @param resultHandler Optional completion handler. If specified, when the unregister has been
   * propagated to all nodes of the event bus, the handler will be called.
   */
  def unregisterHandler[T <% MessageData](address: String, handler: Handler[JMessage[T]], resultHandler: AsyncResult[Void] => Unit): EventBus = wrap({
    internal.unregisterHandler(address, handler, resultHandler)
  })

  /**
   * Sets a default timeout, in ms, for replies. If a messages is sent specify a reply handler
   * but without specifying a timeout, then the reply handler is timed out, i.e. it is automatically unregistered
   * if a message hasn't been received before timeout.
   * The default value for default send timeout is -1, which means "never timeout".
   * @param timeoutMs
   */
  def setDefaultReplyTimeout(timeoutMs: Long): EventBus = wrap(internal.setDefaultReplyTimeout(timeoutMs))

  /**
   * Return the value for default send timeout.
   */
  def getDefaultReplyTimeout(): Long = internal.getDefaultReplyTimeout()

  private def mapHandler[T <% MessageData](handler: Message[T] => Unit): Handler[JMessage[T]] = {
    fnToHandler(handler.compose(Message.apply))
  }

  private def convertArHandler[T <% MessageData](handler: AsyncResult[Message[T]] => Unit): Handler[AsyncResult[JMessage[T]]] = {
    asyncResultConverter({x: JMessage[T] => Message.apply(x)})(handler)
  }

  private def sendOrPublish(cmd: SendOrPublish, timeout: Long): EventBus = {
    cmd match {
      case Publish(address, value) => value.publish(internal, address)
      case Send(address, value, None) => value.send(internal, address)
      case Send(address, value, Some(Left(handler))) => value.send(internal, address, handler)
      case Send(address, value, Some(Right(handler))) => value.sendWithTimeout(internal, address, handler, timeout)
    }
    this
  }
}

/**
 * Companion object for EventBus.
 */
object EventBus {
  def apply(actual: JEventBus) = new EventBus(actual)
}
