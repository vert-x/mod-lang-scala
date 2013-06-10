package org.vertx.scala.tests

import org.vertx.testtools.VertxAssert.testComplete
import org.vertx.testtools.VertxAssert.assertEquals
import org.vertx.testtools.TestVerticle
import org.junit.Test

/**
 * Date: 6/1/13
 * @author Edgar Chan
 */
class ScalaHttpTest extends TestVerticle{

  import org.vertx.scala.core._

  @Test
  def testClientDefaults() {

    val html = "<html><body><h1>Hello from vert.x!</h1></body></html>"
    val port = 8080

    vertx.newHttpServer{
      r => r.response.end(html)
    }.listen(port, server =>{
      val client = vertx.newHttpClient.setPort(port)
      client.getNow("/"){
        h => h.bodyHandler{
                data => {
                    assertEquals( html, data.toString )
                    testComplete()
                }
        }
      }
    })

  }

}
