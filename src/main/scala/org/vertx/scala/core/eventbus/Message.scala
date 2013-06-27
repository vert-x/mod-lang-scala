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

import org.vertx.java.core.eventbus.{Message => JMessage}
import org.vertx.scala.core.FunctionConverters._


object Message {

  def apply[T](jmessage: JMessage[T]) =
    new Message(jmessage)

  implicit def convertScalaToJava[T](message: Message[T]):JMessage[T] = message.toJavaMessage

  implicit def convertJavaToScala[T](jmessage: JMessage[T]):Message[T] = Message(jmessage)

}

class Message[T](jmessage: JMessage[T]) {

  def toJavaMessage:JMessage[T] = jmessage

  def body:T = jmessage.body()

  def replyAddress:String = jmessage.replyAddress()

  def reply:Unit = jmessage.reply()

  def reply(payload: Any)(handler: JMessage[Any] => Unit):Unit = {
    jmessage.reply(payload, handler)
  }

  def reply[T](payload: T):Unit = {
    jmessage.reply(payload)
  }

}
