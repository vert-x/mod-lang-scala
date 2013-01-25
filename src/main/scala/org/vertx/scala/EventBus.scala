/*
 * Copyright 2011-2012 the original author or authors.
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

package org.vertx.scala

import org.vertx.java.core.eventbus.{EventBus => JEventBus}
import org.vertx.java.core.buffer.{Buffer => VertxBuffer}
import org.vertx.java.core.json.JsonArray
import org.vertx.java.core.json.JsonObject
import org.vertx.java.core.eventbus.Message
import org.vertx.scala.handlers.FunctionHandler1
import org.vertx.scala.handlers.FunctionAsyncResultHandler0
import org.vertx.scala.handlers.FunctionAsyncResultHandler0
import org.vertx.java.core.buffer.{Buffer => VertxBuffer}
import org.vertx.java.core.eventbus.{EventBus => JEventBus}

object EventBus {
  def apply(actual: JEventBus) =
    new EventBus(actual)
}

class EventBus(internal: JEventBus) {

  def publish(address: String, payload: java.lang.Boolean):Unit = internal.publish(address, payload)

  def publish(address: String, payload: VertxBuffer):Unit = internal.publish(address, payload)

  def publish(address: String, payload: java.lang.Byte):Unit = internal.publish(address, payload)

  def publish(address: String, payload: Array[Byte]):Unit = internal.publish(address, payload)

  def publish(address: String, payload: java.lang.Character):Unit = internal.publish(address, payload)

  def publish(address: String, payload: java.lang.Double):Unit = internal.publish(address, payload)

  def publish(address: String, payload: java.lang.Float):Unit = internal.publish(address, payload)

  def publish(address: String, payload: java.lang.Integer):Unit = internal.publish(address, payload)

  def publish(address: String, payload: JsonArray):Unit = internal.publish(address, payload)

  def publish(address: String, payload: JsonObject):Unit = internal.publish(address, payload)

  def publish(address: String, payload: java.lang.Long):Unit = internal.publish(address, payload)

  def publish(address: String, payload: java.lang.Short):Unit = internal.publish(address, payload)

  def publish(address: String, payload: java.lang.String):Unit = internal.publish(address, payload)

  def send(address: String, payload: java.lang.Boolean):Unit = internal.send(address, payload)

  def send(address: String, payload: VertxBuffer):Unit = internal.send(address, payload)

  def send(address: String, payload: java.lang.Byte):Unit = internal.send(address, payload)

  def send(address: String, payload: Array[Byte]):Unit = internal.send(address, payload)

  def send(address: String, payload: java.lang.Character):Unit = internal.send(address, payload)

  def send(address: String, payload: java.lang.Double):Unit = internal.send(address, payload)

  def send(address: String, payload: java.lang.Float):Unit = internal.send(address, payload)

  def send(address: String, payload: java.lang.Integer):Unit = internal.send(address, payload)

  def send(address: String, payload: JsonArray):Unit = internal.send(address, payload)

  def send(address: String, payload: JsonObject):Unit = internal.send(address, payload)

  def send(address: String, payload: java.lang.Long):Unit = internal.send(address, payload)

  def send(address: String, payload: java.lang.Short):Unit = internal.send(address, payload)

  def send(address: String, payload: java.lang.String):Unit = internal.send(address, payload)

  def send(address: String, payload: java.lang.Boolean, handler: (Message[java.lang.Boolean]) => Unit):Unit = {
    internal.send(address, payload, FunctionHandler1(handler))
  }

  def send(address: String, payload: VertxBuffer, handler: (Message[VertxBuffer]) => Unit):Unit = {
    internal.send(address, payload, FunctionHandler1(handler))
  }

  def send(address: String, payload: java.lang.Byte, handler: (Message[java.lang.Byte]) => Unit):Unit = {
    internal.send(address, payload, FunctionHandler1(handler))
  }

  def send(address: String, payload: Array[Byte], handler: (Message[Array[Byte]]) => Unit):Unit = {
    internal.send(address, payload, FunctionHandler1(handler))
  }

  def send(address: String, payload: java.lang.Character, handler: (Message[java.lang.Character]) => Unit):Unit = {
    internal.send(address, payload, FunctionHandler1(handler))
  }

  def send(address: String, payload: java.lang.Double, handler: (Message[java.lang.Double]) => Unit):Unit = {
    internal.send(address, payload, FunctionHandler1(handler))
  }

  def send(address: String, payload: java.lang.Float, handler: (Message[java.lang.Float]) => Unit):Unit = {
    internal.send(address, payload, FunctionHandler1(handler))
  }

  def send(address: String, payload: java.lang.Integer, handler: (Message[java.lang.Integer]) => Unit):Unit = {
    internal.send(address, payload, FunctionHandler1(handler))
  }

  def send(address: String, payload: JsonArray, handler: (Message[JsonArray]) => Unit):Unit = {
    internal.send(address, payload, FunctionHandler1(handler))
  }

  def send(address: String, payload: JsonObject, handler: (Message[JsonObject]) => Unit):Unit = {
    internal.send(address, payload, FunctionHandler1(handler))
  }

  def send(address: String, payload: java.lang.Long, handler: (Message[java.lang.Long]) => Unit):Unit = {
    internal.send(address, payload, FunctionHandler1(handler))
  }

  def send(address: String, payload: java.lang.Short, handler: (Message[java.lang.Short]) => Unit):Unit = {
    internal.send(address, payload, FunctionHandler1(handler))
  }

  def send(address: String, payload: java.lang.String, handler: (Message[java.lang.String]) => Unit):Unit = {
    internal.send(address, payload, FunctionHandler1(handler))
  }

  def registerHandler(address: String, handler: (Message[Any]) => Unit):FunctionHandler1[Message[Any]] = {
    val func = FunctionHandler1(handler)
    internal.registerHandler(address, func)
    func // return the actual function so it can be unregistered later
  }

  def registerHandler(address: String, handler: (Message[Any]) => Unit, resultHandler: () => Unit):FunctionHandler1[Message[Any]] = {
    val func = FunctionHandler1(handler)
    internal.registerHandler(address, func, FunctionAsyncResultHandler0(resultHandler))
    func // return the actual function so it can be unregistered later
  }

  def registerLocalHandler(address: String, handler: (Message[Any]) => Unit):FunctionHandler1[Message[Any]] = {
    val func = FunctionHandler1(handler)
    internal.registerLocalHandler(address, func)
    func // return the actual function so it can be unregistered later
  }

  def unregisterHandler(address: String, func: FunctionHandler1[Message[Any]]):Unit = {
    internal.unregisterHandler(address, func)
  }

  def unregisterHandler(address: String, func: FunctionHandler1[Message[Any]], resultHandler: () => Unit):Unit = {
    internal.unregisterHandler(address, func, FunctionAsyncResultHandler0(resultHandler))
  }

}