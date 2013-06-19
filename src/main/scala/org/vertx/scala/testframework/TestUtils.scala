/*
 * Copyright 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.vertx.scala.testframework

import org.vertx.java.core.buffer.Buffer

/**
 * Port of some of the original TestUtils Helper methods
 * @author Edgar Chan
 */
object TestUtils {


  def generateRandomBuffer(length:Int):Buffer={
    generateRandomBuffer(length, false, 0.toByte)
  }

  def generateRandomBuffer(length:Int, avoid:Boolean, avoidByte:Byte):Buffer={
    val line = generateRandomByteArray(length, avoid, avoidByte)
    new Buffer(line)
  }


  def generateRandomByteArray(length:Int):Array[Byte]={
    generateRandomByteArray(length, false,  0.toByte )
  }

  def generateRandomByteArray(length:Int, avoid:Boolean, avoidByte:Byte):Array[Byte]={
    val line  = new Array[Byte](length)
    var i = 0
    do {
     var rand:Byte = 0.toByte
      do{
        rand = (((Math.random() * 255) - 128).asInstanceOf[Int]).toByte
      }while(avoid && rand == avoidByte)
      line(i) = rand
     i+=1
    }while(i < length)
    line
  }


  def bufferEquals(b1:Buffer, b2:Buffer):Boolean={
    if(b1.length != b2.length) false

    var i=0
    do{
       if (b1.getByte(i) != b2.getByte(i)){ return false}
      i+=1
    }while( i < b1.length)

     true
  }


}