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
import org.vertx.java.core.Handler
import org.vertx.java.core.buffer.Buffer
import org.vertx.java.core.eventbus.{EventBus => JEventBus}
import org.vertx.java.core.eventbus.{Message => JMessage}
import org.vertx.java.core.json.JsonArray
import org.vertx.java.core.json.JsonObject
import org.vertx.scala.core.FunctionConverters._


/**
 * @author swilliams
 * 
 */
object EventBus {
  def apply(actual: JEventBus) = new EventBus(actual)
}

class EventBus(internal: JEventBus) {

  def registerHandler[T](address: String)(handler: Message[T] => Unit, resultHandler: () => Unit = {() => }):EventBus = {
    internal.registerHandler(address, handler, resultHandler)
    this
  }

  def registerLocalHandler[T](address: String)(handler: Message[T] => Unit):EventBus = {
    internal.registerLocalHandler(address, handler)
    this
  }

  def unregisterHandler(address: String)(handler: Handler[JMessage[_]], resultHandler: () => Unit = {() => }):EventBus = {
    internal.unregisterHandler(address, handler, resultHandler)
    this
  }


  def send[T](address:String, message:T)(handler : Message[T] => Unit):EventBus={
    message match{
      case str:String =>
              internal.send(address, str, handler)
      case boo:Boolean =>
              internal.send(address, boo, handler)
      case bff:Buffer =>
              internal.send(address, bff, handler)
      case byt:Byte =>
              internal.send(address, Byte.box(byt), handler)
      case bya:Array[Byte] =>
              internal.send(address, bya, handler)
      case chr:Char =>
              internal.send(address, Char.box(chr), handler)
      case dbl:Double =>
              internal.send(address, dbl, handler)
      case flt:Float =>
              internal.send(address, Float.box(flt), handler)
      case int:Int =>
              internal.send(address, Int.box(int), handler)
      case jsa:JsonArray =>
              internal.send(address, jsa, handler)
      case jso:JsonObject =>
              internal.send(address, jso, handler)
      case lng:Long =>
              internal.send(address, Long.box(lng), handler)
      case srt:Short =>
              internal.send(address, Short.box(srt), handler)

      case _ => throw new IllegalArgumentException("Invalid message " + message.getClass)
    }

    this
  }


  def publish[T](address:String, message:T):EventBus={
    message match{
      case str:String =>
              internal.publish(address, str)
      case boo:Boolean =>
              internal.publish(address, boo)
      case bff:Buffer =>
              internal.publish(address, bff)
      case byt:Byte =>
              internal.publish(address, Byte.box(byt))
      case bya:Array[Byte] =>
              internal.publish(address, bya)
      case chr:Char =>
              internal.publish(address, Char.box(chr))
      case dbl:Double =>
              internal.publish(address, dbl)
      case flt:Float =>
              internal.publish(address, Float.box(flt))
      case int:Int =>
              internal.publish(address, Int.box(int))
      case jsa:JsonArray =>
              internal.publish(address, jsa)
      case jso:JsonObject =>
              internal.publish(address, jso)
      case lng:Long =>
              internal.publish(address, Long.box(lng))
      case srt:Short =>
              internal.publish(address, Short.box(srt))

      case _ => throw new IllegalArgumentException("Invalid messagec" + message.getClass)
    }

    this
  }

}