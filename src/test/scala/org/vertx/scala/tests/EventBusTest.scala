package org.vertx.scala.tests

import org.vertx.testtools.TestVerticle
import org.junit.Test
import org.vertx.testtools.VertxAssert._
import org.vertx.scala.core.eventbus.EventBus
import org.vertx.scala.testframework.TestUtils
import org.vertx.java.core.buffer.Buffer
import org.vertx.java.core.json.{JsonObject, JsonArray}
import scala.util.parsing.json.JSONArray


/**
 * @author Edgar Chan
 */
class EventBusTest extends TestVerticle{

  lazy val eb = EventBus(vertx.eventBus)


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
    eb.registerHandler[T](address){ msg =>
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
