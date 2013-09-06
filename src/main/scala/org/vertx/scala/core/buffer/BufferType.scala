package org.vertx.scala.core.buffer

import org.vertx.java.core.buffer.{ Buffer => JBuffer }

trait BufferType[T] {
  def appendToBuffer(buffer: JBuffer, value: T): Buffer
}

object BufferTypes {
  implicit object BufferElem extends BufferType[Buffer] {
    def appendToBuffer(buffer: JBuffer, value: Buffer): Buffer = buffer.appendBuffer(value.toJava)
  }
  implicit object ByteElem extends BufferType[Byte] {
    def appendToBuffer(buffer: JBuffer, value: Byte): Buffer = buffer.appendByte(value)
  }
  implicit object BytesElem extends BufferType[Array[Byte]] {
    def appendToBuffer(buffer: JBuffer, value: Array[Byte]): Buffer = buffer.appendBytes(value)
  }
  implicit object DoubleElem extends BufferType[Double] {
    def appendToBuffer(buffer: JBuffer, value: Double): Buffer = buffer.appendDouble(value)
  }
  implicit object FloatElem extends BufferType[Float] {
    def appendToBuffer(buffer: JBuffer, value: Float): Buffer = buffer.appendFloat(value)
  }
  implicit object IntElem extends BufferType[Int] {
    def appendToBuffer(buffer: JBuffer, value: Int): Buffer = buffer.appendInt(value)
  }
  implicit object LongElem extends BufferType[Long] {
    def appendToBuffer(buffer: JBuffer, value: Long): Buffer = buffer.appendLong(value)
  }
  implicit object ShortElem extends BufferType[Short] {
    def appendToBuffer(buffer: JBuffer, value: Short): Buffer = buffer.appendShort(value)
  }
  implicit object StringElem extends BufferType[String] {
    def appendToBuffer(buffer: JBuffer, value: String): Buffer = buffer.appendString(value)
  }
  implicit object StringWithEncodingElem extends BufferType[(String, String)] {
    def appendToBuffer(buffer: JBuffer, value: (String, String)): Buffer = buffer.appendString(value._1, value._2)
  }
}