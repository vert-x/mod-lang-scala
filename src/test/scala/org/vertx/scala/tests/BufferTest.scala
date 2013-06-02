package org.vertx.scala.tests

import org.junit.Test
import org.junit.Assert.assertEquals
import org.vertx.java.core.buffer.Buffer
import org.vertx.scala.testframework.TestUtils

/**
 * Date: 6/1/13
 * @author Edgar Chan
 */
class BufferTest {
  import org.vertx.scala.core.buffer.Buffer_x._

  private val size = 100

  @Test
  def appendBufferTest(){
    val buff1 = TestUtils.generateRandomBuffer(size)
    val buff2 = new Buffer()

    buff2.append(buff1)
    assertEquals(size, buff2.length)

  }

  @Test
  def appendByteTest(){
    val buff = appendValue(size, Byte.MaxValue )
    assertEquals(size+1, buff.length)
  }


  @Test
  def appendIntTest(){
    val buff = appendValue(size, Int.MaxValue )
    assertEquals(size+4, buff.length)
  }

  @Test
  def appendLongTest(){
    val buff = appendValue(size, Long.MaxValue )
    assertEquals(size+8, buff.length)
  }

  @Test
  def appendFloatTest(){
    val buff = appendValue(size,  Float.MaxValue )
    assertEquals(size+4, buff.length)
  }


  private def appendValue[T](s:Int, v:T):Buffer={
    val buff = TestUtils.generateRandomBuffer(s)
    buff.append(v)
    buff
  }



}