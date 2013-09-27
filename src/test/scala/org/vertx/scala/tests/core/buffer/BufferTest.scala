package org.vertx.scala.tests.core.buffer

import org.junit.Test
import org.junit.Assert._
import org.vertx.scala.core.buffer._
import org.vertx.scala.core.buffer.BufferTypes._
import org.vertx.java.core.buffer.{ Buffer => JBuffer }
import scala.util.matching.Regex
import org.vertx.scala.testframework.TestUtils
import java.util.Arrays

class BufferTest {

  private def appendTest[T: BufferType](value: T) {
    val buffer = new JBuffer
    buffer.append(value)

    val computedValue = value match {
      case _: Int => buffer.getInt(0)
      case _: Float => buffer.getFloat(0)
      case x: String => buffer.getString(0, x.length)
      case _: Buffer => Buffer(buffer.getBuffer(0, value.toString.length))
      case _: Long => buffer.getLong(0)
      case _: Byte => buffer.getByte(0)
      case _: Double => buffer.getDouble(0)
      case _: Short => buffer.getShort(0)
      case y: Array[Byte] => buffer.getBytes()
      case (a: String, b: String) => (buffer.getString(0, buffer.length, b), b)
    }

    val realValue = value match {
      case x: Array[Byte] => x
      case _ => value
    }

    (realValue, computedValue) match {
      case (x: Array[Byte], y: Array[Byte]) => (0 to x.length - 1) map (i => assertEquals(x(i), y(i)))
      case ((a, b), (x, y)) => assertEquals(a + " should match " + x, a, x)
      case (a, b) => assertEquals(a + " should match " + b, a, b)
    }
  }

  @Test def testAppendBuffer(): Unit = appendTest(Buffer(new JBuffer("test")))
  @Test def testAppendByte(): Unit = appendTest(TestUtils.generateRandomByteArray(3)(1))
  @Test def testAppendByteArray(): Unit = appendTest(TestUtils.generateRandomByteArray(5))
  @Test def testAppendDouble(): Unit = appendTest(123.4)
  @Test def testAppendFloat(): Unit = appendTest(123.4f)
  @Test def testAppendInt(): Unit = appendTest(123)
  @Test def testAppendLong(): Unit = appendTest(123L)
  @Test def testAppendShort(): Unit = appendTest(Short.MaxValue)
  @Test def testAppendString(): Unit = appendTest("hello")
  @Test def testAppendStringWithEncoding(): Unit = appendTest("hellöäü", "UTF-8")

}