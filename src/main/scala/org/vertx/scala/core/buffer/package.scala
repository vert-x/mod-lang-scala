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
package org.vertx.scala.core

import org.vertx.java.core.buffer.{ Buffer => JBuffer }
import org.vertx.scala.core.FunctionConverters._

/**
 * @author swilliams
 * @author Edgar Chan
 * @author <a href="http://www.campudus.com/">Joern Bernhardt</a>
 */
package object buffer {

  trait BufferType[T] {
    def appendToBuffer(buffer: JBuffer, value: T): JBuffer
  }
  implicit object BufferElem extends BufferType[Buffer] {
    override def appendToBuffer(buffer: JBuffer, value: Buffer) = buffer.appendBuffer(value.asJava)
  }
  implicit object ByteElem extends BufferType[Byte] {
    override def appendToBuffer(buffer: JBuffer, value: Byte) = buffer.appendByte(value)
  }
  implicit object BytesElem extends BufferType[Array[Byte]] {
    override def appendToBuffer(buffer: JBuffer, value: Array[Byte]) = buffer.appendBytes(value)
  }
  implicit object DoubleElem extends BufferType[Double] {
    override def appendToBuffer(buffer: JBuffer, value: Double) = buffer.appendDouble(value)
  }
  implicit object FloatElem extends BufferType[Float] {
    override def appendToBuffer(buffer: JBuffer, value: Float) = buffer.appendFloat(value)
  }
  implicit object IntElem extends BufferType[Int] {
    override def appendToBuffer(buffer: JBuffer, value: Int) = buffer.appendInt(value)
  }
  implicit object LongElem extends BufferType[Long] {
    override def appendToBuffer(buffer: JBuffer, value: Long) = buffer.appendLong(value)
  }
  implicit object ShortElem extends BufferType[Short] {
    override def appendToBuffer(buffer: JBuffer, value: Short) = buffer.appendShort(value)
  }
  implicit object StringElem extends BufferType[String] {
    override def appendToBuffer(buffer: JBuffer, value: String) = buffer.appendString(value)
  }
  implicit object StringWithEncodingElem extends BufferType[(String, String)] {
    override def appendToBuffer(buffer: JBuffer, value: (String, String)) = buffer.appendString(value._1, value._2)
  }

  def bufferHandlerToJava(handler: Buffer => Unit) = fnToHandler(handler.compose { jbuffer: JBuffer => Buffer.apply(jbuffer) })
}