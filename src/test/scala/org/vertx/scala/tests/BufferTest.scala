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
 
package org.vertx.scala.tests

import org.junit.Test
import org.junit.Assert.assertEquals
import org.vertx.java.core.buffer.Buffer
import org.vertx.scala.testframework.TestUtils

/**
 * @author Edgar Chan
 */
class BufferTest {
  import org.vertx.scala.core.buffer.Buffer._

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
    assertEquals(buff.getByte(size), Byte.MaxValue )
  }


  @Test
  def appendIntTest(){
    val buff = appendValue(size, Int.MaxValue )
    assertEquals(size+4, buff.length)
    assertEquals(buff.getInt(size), Int.MaxValue)
  }

  @Test
  def appendLongTest(){
    val buff = appendValue(size, Long.MaxValue )
    assertEquals(size+8, buff.length)
    assertEquals(buff.getLong(size), Long.MaxValue)
  }

  @Test
  def appendFloatTest(){
    val buff = appendValue(size,  Float.MaxValue )
    assertEquals(size+4, buff.length)
    assertEquals(buff.getFloat(size), Float.MaxValue, 1e-15)
  }


  @Test
   def appendStringTest(){
     val str = "loremipsum"
     val buff = new Buffer()
     buff.append(str)
     assertEquals(str, buff.toString)
   }

  private def appendValue[T](s:Int, v:T):Buffer={
    val buff = TestUtils.generateRandomBuffer(s)
    buff.append(v)
    buff
  }


}