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
