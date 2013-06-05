package org.vertx.scala.tests

import org.vertx.testtools.TestVerticle
import org.vertx.testtools.VertxAssert.testComplete
import org.vertx.testtools.VertxAssert.assertEquals
import org.junit.Test
import org.vertx.java.core.buffer.Buffer
import org.vertx.scala.testframework.TestUtils

/**
 * @author Edgar Chan
 */
class NetServerTest extends TestVerticle{

  import org.vertx.scala.core.Vert_x._
  import org.vertx.scala.core.buffer.Buffer._

  @Test
  def echoNoSSLTest(){

    val server = vertx.newNetServer
    val client = vertx.newNetClient

    server.connectHandler( socket =>
      socket.dataHandler{
        buff => socket.write(buff)
      }
    ).listen(8080, async =>{

      assertEquals(true, async.succeeded)
      assertEquals(server.internal, async.result)

      client.connect(8080, "localhost", async1 =>{
        assertEquals(true, async1.succeeded)

        val socket = async1.result
        val sends = 10
        val size = 100

        val sent = new Buffer()

        val received = new Buffer()

        socket.dataHandler( buffer =>{
          received.append(buffer)

          if(received.length == sends * size){
            TestUtils.bufferEquals(sent, received)
            server.close(() =>{
                client.close()
                testComplete()
            })
          }
        })

        socket.pause()

        socket.resume()

        (1 to sends).foreach{ s =>
          val data = TestUtils.generateRandomBuffer(size)
          sent.append(data)
          socket.write(data)
        }

      })
    })
  }

}