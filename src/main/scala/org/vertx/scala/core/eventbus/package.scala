package org.vertx.scala.core

import org.vertx.java.core.buffer.{ Buffer => JBuffer }
import org.vertx.java.core.eventbus.{ EventBus => JEventBus }
import org.vertx.java.core.eventbus.{ Message => JMessage }
import org.vertx.scala.core.json.JsonArray
import org.vertx.scala.core.json.JsonObject
import org.vertx.scala.core.buffer.Buffer

/**
 * @author <a href="http://www.campudus.com/">Joern Bernhardt</a>
 */
package object eventbus {
  sealed trait MessageData {
    type InternalType
    val data: InternalType
    def send(eb: JEventBus, address: String)
    def send[X](eb: JEventBus, address: String, resultHandler: Handler[JMessage[X]])
    def sendWithTimeout[X](eb: JEventBus, address: String, resultHandler: Handler[AsyncResult[JMessage[X]]], timeout: Long)
    def publish(eb: JEventBus, address: String)
    def reply[A](msg: JMessage[A])
    def reply[A, B](msg: JMessage[A], resultHandler: Handler[JMessage[B]])
    def replyWithTimeout[A, B](msg: JMessage[A], timeout: Long, resultHandler: Handler[AsyncResult[JMessage[B]]])
  }

  sealed trait JMessageData extends MessageData {
    def toScalaMessageData(): MessageData
  }

  import scala.language.implicitConversions
  implicit def anyToMessageData(any: Any): MessageData = any match {
    case sth: String => StringData(sth)
    case sth: JsonArray => JsonArrayData(sth)
    case sth: JsonObject => JsonObjectData(sth)
    case sth: Buffer => BufferData(sth)
    case sth: Array[Byte] => ByteArrayData(sth)
    case sth: Boolean => BooleanData(sth)
    case sth: Integer => IntegerData(sth)
    case sth: Long => LongData(sth)
    case sth: Short => ShortData(sth)
    case sth: Float => FloatData(sth)
    case sth: Double => DoubleData(sth)
    case sth: Character => CharacterData(sth)
    case x => throw new IllegalArgumentException("Cannot convert type of " + x + " to MessageData!")
  }

  implicit class StringData(val data: String) extends MessageData {
    type InternalType = String
    def send(eb: JEventBus, address: String) = eb.send(address, data)
    def send[T](eb: JEventBus, address: String, handler: Handler[JMessage[T]]) =
      eb.send(address, data, handler)
    def sendWithTimeout[T](eb: JEventBus, address: String, handler: Handler[AsyncResult[JMessage[T]]], timeout: Long) =
      eb.sendWithTimeout(address, data, timeout, handler)
    def publish(eb: JEventBus, address: String) = eb.publish(address, data)
    def reply[A](msg: JMessage[A]) = msg.reply(data)
    def reply[A, B](msg: JMessage[A], handler: Handler[JMessage[B]]) = msg.reply(data, handler)
    def replyWithTimeout[A, B](msg: JMessage[A], timeout: Long, handler: Handler[AsyncResult[JMessage[B]]]) =
      msg.replyWithTimeout(data, timeout, handler)
  }

  implicit class JsonObjectData(val data: JsonObject) extends MessageData {
    type InternalType = JsonObject
    def send(eb: JEventBus, address: String) = eb.send(address, data)
    def send[T](eb: JEventBus, address: String, handler: Handler[JMessage[T]]) =
      eb.send(address, data, handler)
    def sendWithTimeout[T](eb: JEventBus, address: String, handler: Handler[AsyncResult[JMessage[T]]], timeout: Long) =
      eb.sendWithTimeout(address, data, timeout, handler)
    def publish(eb: JEventBus, address: String) = eb.publish(address, data)
    def reply[A](msg: JMessage[A]) = msg.reply(data)
    def reply[A, B](msg: JMessage[A], handler: Handler[JMessage[B]]) = msg.reply(data, handler)
    def replyWithTimeout[A, B](msg: JMessage[A], timeout: Long, handler: Handler[AsyncResult[JMessage[B]]]) =
      msg.replyWithTimeout(data, timeout, handler)
  }

  implicit class JsonArrayData(val data: JsonArray) extends MessageData {
    type InternalType = JsonArray
    def send(eb: JEventBus, address: String) = eb.send(address, data)
    def send[T](eb: JEventBus, address: String, handler: Handler[JMessage[T]]) =
      eb.send(address, data, handler)
    def sendWithTimeout[T](eb: JEventBus, address: String, handler: Handler[AsyncResult[JMessage[T]]], timeout: Long) =
      eb.sendWithTimeout(address, data, timeout, handler)
    def publish(eb: JEventBus, address: String) = eb.publish(address, data)
    def reply[A](msg: JMessage[A]) = msg.reply(data)
    def reply[A, B](msg: JMessage[A], handler: Handler[JMessage[B]]) = msg.reply(data, handler)
    def replyWithTimeout[A, B](msg: JMessage[A], timeout: Long, handler: Handler[AsyncResult[JMessage[B]]]) =
      msg.replyWithTimeout(data, timeout, handler)
  }

  implicit class BufferData(val data: Buffer) extends MessageData {
    type InternalType = Buffer
    def send(eb: JEventBus, address: String) = eb.send(address, data)
    def send[T](eb: JEventBus, address: String, handler: Handler[JMessage[T]]) =
      eb.send(address, data.toJava, handler)
    def sendWithTimeout[T](eb: JEventBus, address: String, handler: Handler[AsyncResult[JMessage[T]]], timeout: Long) =
      eb.sendWithTimeout(address, data, timeout, handler)
    def publish(eb: JEventBus, address: String) = eb.publish(address, data)
    def reply[A](msg: JMessage[A]) = msg.reply(data)
    def reply[A, B](msg: JMessage[A], handler: Handler[JMessage[B]]) = msg.reply(data, handler)
    def replyWithTimeout[A, B](msg: JMessage[A], timeout: Long, handler: Handler[AsyncResult[JMessage[B]]]) =
      msg.replyWithTimeout(data, timeout, handler)
  }

  implicit class JBufferData(val data: JBuffer) extends JMessageData {
    type InternalType = JBuffer
    def send(eb: JEventBus, address: String) = eb.send(address, data)
    def send[T](eb: JEventBus, address: String, handler: Handler[JMessage[T]]) =
      eb.send(address, data, handler)
    def sendWithTimeout[T](eb: JEventBus, address: String, handler: Handler[AsyncResult[JMessage[T]]], timeout: Long) =
      eb.sendWithTimeout(address, data, timeout, handler)
    def publish(eb: JEventBus, address: String) = eb.publish(address, data)
    def reply[A](msg: JMessage[A]) = msg.reply(data)
    def reply[A, B](msg: JMessage[A], handler: Handler[JMessage[B]]) = msg.reply(data, handler)
    def toScalaMessageData(): BufferData = BufferData(Buffer(data))
    def replyWithTimeout[A, B](msg: JMessage[A], timeout: Long, handler: Handler[AsyncResult[JMessage[B]]]) =
      msg.replyWithTimeout(data, timeout, handler)
  }

  implicit class ByteArrayData(val data: Array[Byte]) extends MessageData {
    type InternalType = Array[Byte]
    def send(eb: JEventBus, address: String) = eb.send(address, data)
    def send[T](eb: JEventBus, address: String, handler: Handler[JMessage[T]]) =
      eb.send(address, data, handler)
    def sendWithTimeout[T](eb: JEventBus, address: String, handler: Handler[AsyncResult[JMessage[T]]], timeout: Long) =
      eb.sendWithTimeout(address, data, timeout, handler)
    def publish(eb: JEventBus, address: String) = eb.publish(address, data)
    def reply[A](msg: JMessage[A]) = msg.reply(data)
    def reply[A, B](msg: JMessage[A], handler: Handler[JMessage[B]]) = msg.reply(data, handler)
    def replyWithTimeout[A, B](msg: JMessage[A], timeout: Long, handler: Handler[AsyncResult[JMessage[B]]]) =
      msg.replyWithTimeout(data, timeout, handler)
  }

  implicit class IntegerData(val data: Int) extends MessageData {
    type InternalType = Int
    def send(eb: JEventBus, address: String) = eb.send(address, data)
    def send[T](eb: JEventBus, address: String, handler: Handler[JMessage[T]]) =
      eb.send(address, Int.box(data), handler)
    def sendWithTimeout[T](eb: JEventBus, address: String, handler: Handler[AsyncResult[JMessage[T]]], timeout: Long) =
      eb.sendWithTimeout(address, data, timeout, handler)
    def publish(eb: JEventBus, address: String) = eb.publish(address, data)
    def reply[A](msg: JMessage[A]) = msg.reply(data)
    def reply[A, B](msg: JMessage[A], handler: Handler[JMessage[B]]) = msg.reply(data, handler)
    def replyWithTimeout[A, B](msg: JMessage[A], timeout: Long, handler: Handler[AsyncResult[JMessage[B]]]) =
      msg.replyWithTimeout(data, timeout, handler)
  }

  implicit class LongData(val data: Long) extends MessageData {
    type InternalType = Long
    def send(eb: JEventBus, address: String) = eb.send(address, data)
    def send[T](eb: JEventBus, address: String, handler: Handler[JMessage[T]]) =
      eb.send(address, Long.box(data), handler)
    def sendWithTimeout[T](eb: JEventBus, address: String, handler: Handler[AsyncResult[JMessage[T]]], timeout: Long) =
      eb.sendWithTimeout(address, data, timeout, handler)
    def publish(eb: JEventBus, address: String) = eb.publish(address, data)
    def reply[A](msg: JMessage[A]) = msg.reply(data)
    def reply[A, B](msg: JMessage[A], handler: Handler[JMessage[B]]) = msg.reply(data, handler)
    def replyWithTimeout[A, B](msg: JMessage[A], timeout: Long, handler: Handler[AsyncResult[JMessage[B]]]) =
      msg.replyWithTimeout(data, timeout, handler)
  }

  implicit class ShortData(val data: Short) extends MessageData {
    type InternalType = Short
    def send(eb: JEventBus, address: String) = eb.send(address, data)
    def send[T](eb: JEventBus, address: String, handler: Handler[JMessage[T]]) =
      eb.send(address, Short.box(data), handler)
    def sendWithTimeout[T](eb: JEventBus, address: String, handler: Handler[AsyncResult[JMessage[T]]], timeout: Long) =
      eb.sendWithTimeout(address, data, timeout, handler)
    def publish(eb: JEventBus, address: String) = eb.publish(address, data)
    def reply[A](msg: JMessage[A]) = msg.reply(data)
    def reply[A, B](msg: JMessage[A], handler: Handler[JMessage[B]]) = msg.reply(data, handler)
    def replyWithTimeout[A, B](msg: JMessage[A], timeout: Long, handler: Handler[AsyncResult[JMessage[B]]]) =
      msg.replyWithTimeout(data, timeout, handler)
  }

  implicit class CharacterData(val data: Character) extends MessageData {
    type InternalType = Character
    def send(eb: JEventBus, address: String) = eb.send(address, data)
    def send[T](eb: JEventBus, address: String, handler: Handler[JMessage[T]]) =
      eb.send(address, data, handler)
    def sendWithTimeout[T](eb: JEventBus, address: String, handler: Handler[AsyncResult[JMessage[T]]], timeout: Long) =
      eb.sendWithTimeout(address, data, timeout, handler)
    def publish(eb: JEventBus, address: String) = eb.publish(address, data)
    def reply[A](msg: JMessage[A]) = msg.reply(data)
    def reply[A, B](msg: JMessage[A], handler: Handler[JMessage[B]]) = msg.reply(data, handler)
    def replyWithTimeout[A, B](msg: JMessage[A], timeout: Long, handler: Handler[AsyncResult[JMessage[B]]]) =
      msg.replyWithTimeout(data, timeout, handler)
  }

  implicit class BooleanData(val data: Boolean) extends MessageData {
    type InternalType = Boolean
    def send(eb: JEventBus, address: String) = eb.send(address, data)
    def send[T](eb: JEventBus, address: String, handler: Handler[JMessage[T]]) =
      eb.send(address, data, handler)
    def sendWithTimeout[T](eb: JEventBus, address: String, handler: Handler[AsyncResult[JMessage[T]]], timeout: Long) =
      eb.sendWithTimeout(address, data, timeout, handler)
    def publish(eb: JEventBus, address: String) = eb.publish(address, data)
    def reply[A](msg: JMessage[A]) = msg.reply(data)
    def reply[A, B](msg: JMessage[A], handler: Handler[JMessage[B]]) = msg.reply(data, handler)
    def replyWithTimeout[A, B](msg: JMessage[A], timeout: Long, handler: Handler[AsyncResult[JMessage[B]]]) =
      msg.replyWithTimeout(data, timeout, handler)
  }

  implicit class FloatData(val data: Float) extends MessageData {
    type InternalType = Float
    def send(eb: JEventBus, address: String) = eb.send(address, data)
    def send[T](eb: JEventBus, address: String, handler: Handler[JMessage[T]]) =
      eb.send(address, Float.box(data), handler)
    def sendWithTimeout[T](eb: JEventBus, address: String, handler: Handler[AsyncResult[JMessage[T]]], timeout: Long) =
      eb.sendWithTimeout(address, data, timeout, handler)
    def publish(eb: JEventBus, address: String) = eb.publish(address, data)
    def reply[A](msg: JMessage[A]) = msg.reply(data)
    def reply[A, B](msg: JMessage[A], handler: Handler[JMessage[B]]) = msg.reply(data, handler)
    def replyWithTimeout[A, B](msg: JMessage[A], timeout: Long, handler: Handler[AsyncResult[JMessage[B]]]) =
      msg.replyWithTimeout(data, timeout, handler)
  }

  implicit class DoubleData(val data: Double) extends MessageData {
    type InternalType = Double
    def send(eb: JEventBus, address: String) = eb.send(address, data)
    def send[T](eb: JEventBus, address: String, handler: Handler[JMessage[T]]) =
      eb.send(address, data, handler)
    def sendWithTimeout[T](eb: JEventBus, address: String, handler: Handler[AsyncResult[JMessage[T]]], timeout: Long) =
      eb.sendWithTimeout(address, data, timeout, handler)
    def publish(eb: JEventBus, address: String) = eb.publish(address, data)
    def reply[A](msg: JMessage[A]) = msg.reply(data)
    def reply[A, B](msg: JMessage[A], handler: Handler[JMessage[B]]) = msg.reply(data, handler)
    def replyWithTimeout[A, B](msg: JMessage[A], timeout: Long, handler: Handler[AsyncResult[JMessage[B]]]) =
      msg.replyWithTimeout(data, timeout, handler)
  }
}