package org.vertx.scala.tests.core.buffer

import org.junit.Test
import org.junit.Assert._
import org.vertx.scala.core.buffer._
import org.vertx.scala.tests.util.TestUtils

class BufferTest {

  private def appendTest[T: BufferType](value: T) {
    val buffer = Buffer()
    buffer.append(value)

    val computedValue = value match {
      case _: Int => buffer.getInt(0)
      case _: Float => buffer.getFloat(0)
      case x: String => buffer.getString(0, x.length)
      case x: Buffer => buffer.getBuffer(0, x.length())
      case _: Long => buffer.getLong(0)
      case _: Byte => buffer.getByte(0)
      case _: Double => buffer.getDouble(0)
      case _: Short => buffer.getShort(0)
      case y: Array[Byte] => buffer.getBytes
      case (a: String, b: String) => (buffer.getString(0, buffer.length(), b), b)
    }

    val realValue = value match {
      case x: Array[Byte] => x
      case _ => value
    }

    (realValue, computedValue) match {
      case (x: Array[Byte], y: Array[Byte]) => assertArrayEquals(x, y)
      case ((a, b), (x, y)) => assertEquals(a + " should match " + x, a, x)
      case (a, b) => assertEquals(a + " should match " + b, a, b)
    }
  }

  @Test def appendBuffer(): Unit = appendTest(Buffer("test"))
  @Test def appendByte(): Unit = appendTest(TestUtils.generateRandomByteArray(3)(1))
  @Test def appendByteArray(): Unit = appendTest(TestUtils.generateRandomByteArray(5))
  @Test def appendDouble(): Unit = appendTest(123.4)
  @Test def appendFloat(): Unit = appendTest(123.4f)
  @Test def appendInt(): Unit = appendTest(123)
  @Test def appendLong(): Unit = appendTest(123L)
  @Test def appendShort(): Unit = appendTest(Short.MaxValue)
  @Test def appendString(): Unit = appendTest("hello")
  @Test def appendStringWithEncoding(): Unit = appendTest("hellöäü", "UTF-8")

  @Test def appendBytesWithOffsetAndLen() {
    val bytesLen = 100
    val bytes = TestUtils.generateRandomByteArray(bytesLen)
    val len = bytesLen - 2
    val b = Buffer()
    b.append(bytes, 1, len)
    assertEquals(b.length(), len)
    val copy  = new Array[Byte](len)
    System.arraycopy(bytes, 1, copy, 0, len)
    assertArrayEquals(copy, b.getBytes)
    b.append(bytes, 1, len)
    assertEquals(b.length(), 2 * len)
  }

  @Test def appendBufferWithOffsetAndLen() {
    val bytesLen = 100
    val bytes = TestUtils.generateRandomByteArray(bytesLen)
    val src = Buffer(bytes)
    val len = bytesLen - 2
    val b = Buffer()
    b.append(src, 1, len)
    assertEquals(b.length(), len)
    val copy = new Array[Byte](len)
    System.arraycopy(bytes, 1, copy, 0, len)
    assertArrayEquals(copy, b.getBytes)
    b.append(src, 1, len)
    assertEquals(b.length(), 2 * len)
  }

  @Test def setBytesWithOffsetAndLen() {
    val bytesLen = 100
    val bytes = TestUtils.generateRandomByteArray(bytesLen)
    val len = bytesLen - 2
    val b = Buffer()
    b.setByte(0, '0'.asInstanceOf[Byte])
    b.setBytes(1, bytes, 1, len)
    assertEquals(b.length(), len + 1)
    val copy = new Array[Byte](len)
    System.arraycopy(bytes, 1, copy, 0, len)
    assertArrayEquals(copy, b.getBytes(1, b.length()))
    b.setBytes(b.length(), bytes, 1, len)
    assertEquals(b.length(), 2 * len + 1)
  }

  @Test def setBufferWithOffsetAndLen() {
    val bytesLen = 100
    val bytes = TestUtils.generateRandomByteArray(bytesLen)
    val src = Buffer(bytes)
    val len = bytesLen - 2
    val b = Buffer()
    b.setByte(0, '0'.asInstanceOf[Byte])
    b.setBuffer(1, src, 1, len)
    assertEquals(b.length(), len + 1)
    val copy: Array[Byte] = new Array[Byte](len)
    System.arraycopy(bytes, 1, copy, 0, len)
    assertArrayEquals(copy, b.getBytes(1, b.length()))
    b.setBuffer(b.length(), src, 1, len)
    assertEquals(b.length(), 2 * len + 1)
  }

  @Test(expected = classOf[IndexOutOfBoundsException])
  def appendBytesWithNegativeOffset() {
    val bytesLen = 100
    val bytes = TestUtils.generateRandomByteArray(bytesLen)
    val len = bytesLen - 2
    val b = Buffer()
    b.append(bytes, -1, len)
  }

  @Test(expected = classOf[IllegalArgumentException])
  def appendBytesWithNegativeLen() {
    val bytesLen = 100
    val bytes = TestUtils.generateRandomByteArray(bytesLen)
    val b = Buffer()
    b.append(bytes, 1, -1)
  }

  @Test(expected = classOf[IndexOutOfBoundsException])
  def setBytesWithNegativeOffset() {
    val bytesLen = 100
    val bytes = TestUtils.generateRandomByteArray(bytesLen)
    val len = bytesLen - 2
    val b = Buffer()
    b.setByte(0, '0'.asInstanceOf[Byte])
    b.setBytes(1, bytes, -1, len)
  }

  @Test(expected = classOf[IllegalArgumentException])
  def setBytesWithNegativeLen() {
    val bytesLen = 100
    val bytes = TestUtils.generateRandomByteArray(bytesLen)
    val b = Buffer()
    b.setByte(0, '0'.asInstanceOf[Byte])
    b.setBytes(1, bytes, 1, -1)
  }

}