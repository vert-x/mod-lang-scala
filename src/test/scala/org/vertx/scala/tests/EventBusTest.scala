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

import org.vertx.testtools.TestVerticle
import org.junit.Test
import org.vertx.testtools.VertxAssert._
import org.vertx.scala.core.eventbus.{Message, EventBus}
import org.vertx.scala.testframework.TestUtils
import org.vertx.java.core.buffer.Buffer
import org.vertx.java.core.json.{JsonObject, JsonArray}
import scala.util.parsing.json.JSONArray
import org.vertx.java.platform.Verticle
import org.vertx.java.core.AsyncResult


/**
 * @author Edgar Chan
 */

trait EventBusTestBase{
  val TEST_ADDRESS = "some-address"
  val SENT = "foo"
}

class LocalTestVerticle extends Verticle with EventBusTestBase{

  import org.vertx.scala.core.eventbus.EventBus._
  lazy val eb = vertx.eventBus

  val hdl: EventBusHandler[String] =
    (msg:Message[String]) => {
      assertEquals(SENT, msg.body)
      eb.unregisterHandler(TEST_ADDRESS)(hdl, rst => {
          if(rst.succeeded)
            testComplete()
          else
            assertTrue(rst.succeeded)
       })
    }

  override def start(){
    eb.registerHandler(TEST_ADDRESS)(hdl, rst => {
        if(rst.succeeded)
          testComplete()
        else
          assertTrue(rst.succeeded)
      }
    )
  }

}


class EventBusTest extends TestVerticle with EventBusTestBase{

  lazy val eb = EventBus(vertx.eventBus)

  @Test
  def pubSubTest(){
    import org.vertx.scala.core.FunctionConverters._
    container.deployVerticle(
      classOf[LocalTestVerticle].getName,
      (_ :AsyncResult[String]) => {
        eb.publish(TEST_ADDRESS, SENT);{}
      }
    )
  }

  @Test
  def echoStringTest(){
    echo("bar")
  }

  @Test
  def echoBooleanTest(){
    echo(false)
  }

  @Test
  def echoBufferTest(){
    echo( TestUtils.generateRandomBuffer(100) )
  }

  @Test
  def echoByteTest(){
    echo( Byte.MaxValue )
  }

  @Test
  def echoByteArrayTest(){
    echo( TestUtils.generateRandomByteArray(100) )
  }

  @Test
  def echoCharTest(){
    echo( Char.MaxValue )
  }

  @Test
  def echoDoubleTest(){
    echo( Double.MaxValue )
  }

  @Test
  def echoFloatTest(){
    echo( Float.MaxValue )
  }

  @Test
  def echoIntTest(){
    echo( Int.MaxValue )
  }

  @Test
  def echoJsonTest(){
    val jsObject = new JsonObject(jsonString)
    echo( jsObject )
  }

  @Test
  def echoJsonArrayTest(){
    val jsObject1 = new JsonObject(jsonString)
    val jsObject2 = new JsonObject(jsonString)
    val jsObject3 = new JsonObject(jsonString)

    import org.vertx.scala.core.JSON._
    val jsArray:JsonArray = JSONArray(List(jsObject1, jsObject2, jsObject3))

    echo( jsArray )
  }


  private def echo[T](sent:T){
    val address = "foo-address"
    eb.registerHandler[T](address){ (msg:Message[T]) =>
      localAssert(sent, msg.body)
      msg.reply[T](msg.body)
    }

    eb.send(address, sent){ reply =>
      localAssert(sent, reply.body)
      testComplete()
    }
  }

  private def localAssert[T](expected:T, actual:T){
    (expected , actual) match {
      case (abe:Array[Byte], aba:Array[Byte]) =>
          assertArrayEquals(abe, aba)
      case (ebuff:Buffer, abuff:Buffer) =>
          assertEquals(true, TestUtils.bufferEquals(ebuff, abuff))
      case _  =>
          assertEquals(expected, actual)
    }

  }

  private def jsonString = {
    """
      |{
      |  "webappconf" : {
      |          "port": 8080,
      |          "ssl": false,
      |          "bridge": true,
      |          "inbound_permitted": [
      |            {
      |              "address" : "acme.service",
      |              "match" : {
      |                "action" : "foo"
      |              }
      |            }
      |          ],
      |
      |          "outbound_permitted": [
      |            {}
      |          ]
      |        }
      |}
    """.stripMargin
  }



}
