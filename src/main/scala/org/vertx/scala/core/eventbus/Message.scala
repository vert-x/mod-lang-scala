package org.vertx.scala.core.eventbus

import scala.language.implicitConversions
import org.vertx.java.core.eventbus.{Message => JMessage}
import org.vertx.java.core.buffer.{Buffer => JBuffer}
import org.vertx.java.core.json.JsonArray
import org.vertx.java.core.json.JsonObject
import org.vertx.scala.core.buffer.Buffer
import org.vertx.scala.core.FunctionConverters._

object Message {

  def apply[T](jmessage: JMessage[T]) =
    new Message(jmessage)

  implicit def convertScalaToJava[T](message: Message[T]):JMessage[T] = message.toJavaMessage()

  implicit def convertJavaToScala[T](jmessage: JMessage[T]):Message[T] = Message(jmessage)

}

class Message[T](jmessage: JMessage[T]) {

  def toJavaMessage():JMessage[T] = jmessage

  def body():T = jmessage.body()

  def replyAddress():String = jmessage.replyAddress()

  def reply():Unit = jmessage.reply()

//  def reply(payload: Any):Unit = jmessage.reply(payload)
//
//  def reply(payload: Array[Byte]):Unit = jmessage.reply(payload)
//
//  def reply(payload: Boolean):Unit = jmessage.reply(payload)
//
//  def reply(payload: Character):Unit = jmessage.reply(payload)
//
//  def reply(payload: Double):Unit = jmessage.reply(payload)
//
//  def reply(payload: Float):Unit = jmessage.reply(payload)
//  
//  def reply(payload: Integer):Unit = jmessage.reply(payload)
//
//  def reply(payload: Long):Unit = jmessage.reply(payload)
//
//  def reply(payload: Buffer):Unit = jmessage.reply(payload)
//
//  def reply(payload: JsonArray):Unit = jmessage.reply(payload)
//
//  def reply(payload: JsonObject):Unit = jmessage.reply(payload)

  def reply(payload: Any)(handler: (Message[Any]) => Unit):Unit = {
    jmessage.reply(payload, handler)
  }

}
