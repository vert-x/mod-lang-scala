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

import org.vertx.java.core.{ AsyncResult, Handler }
import org.vertx.java.core.buffer.Buffer
import org.vertx.java.core.eventbus.{ EventBus => JEventBus }
import org.vertx.java.core.eventbus.{ Message => JMessage }
import org.vertx.scala.core.json.{ JsonObject, JsonArray }
import org.vertx.scala.core.FunctionConverters._
import org.vertx.scala.VertxWrapper
import scala.collection.concurrent.Map
import java.util.concurrent.ConcurrentHashMap

class EventBus(protected[this] val internal: JEventBus) extends VertxWrapper {
  override type InternalType = JEventBus

  val handlers: Map[_ <: Message[_] => Unit, Handler[_]] = Map.empty()

  def publish[T: MessageType](address: String, value: T): EventBus =
    implicitly[MessageType[T]].publish(this, address, value)

  def send[T: MessageType](address: String, value: T): EventBus =
    implicitly[MessageType[T]].send(this, address, value)

  def send[T: MessageType, RT <: Message[RT]](address: String, value: T, handler: RT => Unit): EventBus = {
    val mappedHandler = handlers.getOrElseUpdate(handler, convertFunctionToParameterisedHandler(handler))
    implicitly[MessageType[T]].send(this, address, value, mappedHandler)
  }

  def close(x$1: AsyncResult[Void] => Unit): Unit = ???

  def registerHandler[T: MessageType, HT <: Message[T]](x$1: String, x$2: HT => Unit): EventBus = wrap(internal.registerHandler(x$1, x$2))
  def registerHandler[T: MessageType, HT <: Message[T]](x$1: String, x$2: HT => Unit, x$3: AsyncResult[Void] => Unit): EventBus = ???
  def registerLocalHandler[T: MessageType, HT <: Message[T]](x$1: String, x$2: HT => Unit): EventBus = ???
  def unregisterHandler[T: MessageType, HT <: Message[T]](x$1: String, x$2: HT => Unit): EventBus = ???
  def unregisterHandler[T: MessageType, HT <: Message[T]](x$1: String, x$2: HT => Unit, x$3: AsyncResult[Void] => Unit): EventBus = ???
}

/*
 * @author swilliams
 * @author Edgar Chan
 */
object EventBus {
  def apply(actual: JEventBus) = new EventBus(actual)
}
