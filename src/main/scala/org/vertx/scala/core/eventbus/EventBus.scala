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

import scala.language.implicitConversions
import scala.util.parsing.json.JSONObject
import scala.util.parsing.json.JSONArray
import org.vertx.java.core.Handler
import org.vertx.java.core.buffer.Buffer
import org.vertx.java.core.eventbus.{EventBus => JEventBus}
import org.vertx.java.core.eventbus.{Message => JMessage}
import org.vertx.java.core.json.JsonArray
import org.vertx.java.core.json.JsonObject
import org.vertx.scala.core.FunctionConverters._
import org.vertx.scala.core.JSON._
//import org.vertx.scala.core.eventbus.Message


/**
 * @author swilliams
 * 
 */
object EventBus {
  def apply(actual: JEventBus) = new EventBus(actual)
}

class EventBus(internal: JEventBus) {

  def publish(address: String, payload: Boolean):Unit = internal.publish(address, payload)

  def publish(address: String, payload: Buffer):Unit = internal.publish(address, payload)

  def publish(address: String, payload: java.lang.Byte):Unit = internal.publish(address, payload)

  def publish(address: String, payload: Array[Byte]):Unit = internal.publish(address, payload)

  def publish(address: String, payload: Character):Unit = internal.publish(address, payload)

  def publish(address: String, payload: Double):Unit = internal.publish(address, payload)

  def publish(address: String, payload: Float):Unit = internal.publish(address, new java.lang.Float(payload))

  def publish(address: String, payload: Int):Unit = internal.publish(address, new java.lang.Integer(payload))

  def publish(address: String, payload: JSONArray):Unit = internal.publish(address, payload)

  def publish(address: String, payload: JSONObject):Unit = internal.publish(address, payload)

  def publish(address: String, payload: Long):Unit = internal.publish(address, new java.lang.Long(payload))

  def publish(address: String, payload: Short):Unit = internal.publish(address, new java.lang.Short(payload))

  def publish(address: String, payload: String):Unit = internal.publish(address, payload)

  def sendBoolean(address: String, payload: Boolean)(handler: Message[java.lang.Boolean] => Unit = {msg => }):Unit = {
    internal.send(address, boolean2Boolean(payload), handler.asInstanceOf[Handler[JMessage[java.lang.Boolean]]])
  }

  def sendBuffer(address: String, payload: Buffer)(handler: Message[Buffer] => Unit = {msg => }):Unit = {
    internal.send(address, payload,  handler.asInstanceOf[Handler[JMessage[Buffer]]])
  }

  def sendByte(address: String, payload: java.lang.Byte)(handler: Message[java.lang.Byte] => Unit = {msg => }):Unit = {
    internal.send(address, payload, handler.asInstanceOf[Handler[JMessage[java.lang.Byte]]])
  }

  def sendByteArray(address: String, payload: Array[Byte])(handler: Message[Array[Byte]] => Unit = {msg => }):Unit = {
    internal.send(address, payload, handler.asInstanceOf[Handler[JMessage[Array[Byte]]]])
  }

  def sendChar(address: String, payload: Character)(handler: Message[Character] => Unit = {msg => }):Unit = {
    internal.send(address, payload, handler.asInstanceOf[Handler[JMessage[Character]]])
  }

  def sendDouble(address: String, payload: Double)(handler: Message[java.lang.Double] => Unit = {msg => }):Unit = {
    internal.send(address, payload, handler.asInstanceOf[Handler[JMessage[java.lang.Double]]])
  }

  def sendFloat(address: String, payload: java.lang.Float)(handler: Message[java.lang.Float] => Unit = {msg => }):Unit = {
    internal.send(address, payload, handler.asInstanceOf[Handler[JMessage[java.lang.Float]]])
  }

  def sendInt(address: String, payload: Int)(handler: Message[java.lang.Integer] => Unit = {msg => }):Unit = {
    internal.send(address, int2Integer(payload), handler.asInstanceOf[Handler[JMessage[java.lang.Integer]]])
  }

  def sendJsonArray(address: String, payload: JSONArray)(handler: Message[JsonArray] => Unit = {msg => }):Unit = {
    internal.send(address, payload, handler.asInstanceOf[Handler[JMessage[JsonArray]]])
  }

  def sendJsonObject(address: String, payload: JSONObject)(handler: Message[JsonObject] => Unit = {msg => }):Unit = {
    internal.send(address, payload, handler.asInstanceOf[Handler[JMessage[JsonObject]]])
  }

  def sendLong(address: String, payload: java.lang.Long)(handler: Message[java.lang.Long] => Unit = {msg => }):Unit = {
    internal.send(address, payload, handler.asInstanceOf[Handler[JMessage[java.lang.Long]]])
  }

  def sendShort(address: String, payload: java.lang.Short)(handler: Message[java.lang.Short] => Unit = {msg => }):Unit = {
    internal.send(address, payload, handler.asInstanceOf[Handler[JMessage[java.lang.Short]]])
  }

  def sendString(address: String, payload: String)(handler: Message[String] => Unit = {msg: Message[String] => }):Unit = {
    internal.send(address, payload, handler.asInstanceOf[Handler[JMessage[String]]])
  }

  def registerHandler[T](address: String)(handler: Message[T] => Unit, resultHandler: () => Unit = {() => }):Handler[Message[T]] = {
    internal.registerHandler(address, handler, resultHandler)
    handler // return the actual function so it can be unregistered later
  }

  def registerLocalHandler[T](address: String)(handler: Message[T] => Unit):Handler[Message[T]] = {
    internal.registerLocalHandler(address, handler)
    handler // return the actual function so it can be unregistered later
  }

  def unregisterHandler(address: String)(handler: Handler[JMessage[_]], resultHandler: () => Unit = {() => }):Unit = {
    internal.unregisterHandler(address, handler, resultHandler)
  }

}