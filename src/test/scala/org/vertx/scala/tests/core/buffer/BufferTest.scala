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
    val realValue = value.toString().getBytes().mkString(",")
    val computedValue = buffer.getBytes().mkString(",")
    assertEquals(realValue + " should match " + computedValue, realValue, computedValue)
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
  @Test def testAppendStringWithEncoding(): Unit = appendTest("hellö", "UTF-8")
}