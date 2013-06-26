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

import org.vertx.scala.core.parsetools.RecordParser
import org.vertx.java.core.buffer.Buffer
import org.junit.Test
import org.vertx.testtools.TestVerticle
import org.vertx.testtools.VertxAssert.testComplete
import org.vertx.testtools.VertxAssert.assertEquals
import org.vertx.scala.testframework.TestUtils


/**
 * @author Edgar Chan
 */
class RecordParserTest extends TestVerticle{

  import org.vertx.scala.core.buffer.Buffer._

  @Test
  def delimitedTest(){
    var lineCount = 0
    val numLines = 3

    val rp  = RecordParser.newDelimited("\n", line => {
      lineCount+=1
       if (lineCount == numLines){
         testComplete()
       }
    })

    val input = "qwdqwdline1\nijijiline2\njline3\n"
    val  buffer = new Buffer(input)

    rp.handle(buffer)

  }


  @Test
  def testFixed(){

    var chunkCount = 0
    val numChunks = 3
    val chunkSize = 100

    val rp = RecordParser.newFixed(chunkSize, chunk =>{
      assertEquals(chunkSize, chunk.length)
      chunkCount+=1
      if(chunkCount == numChunks){
        testComplete()
      }
    })

    val input = new Buffer(0)

    (1 to numChunks).foreach{ ch =>
      input.append( TestUtils.generateRandomBuffer(chunkSize)  )
    }

    rp.handle(input)

  }

}
