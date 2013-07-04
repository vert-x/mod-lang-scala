package org.vertx.scala.tests.http

import org.vertx.testtools.TestVerticle
import org.junit.Test
import org.vertx.scala.core.http.WebSocket
import org.vertx.java.core.buffer.Buffer
import org.vertx.java.core.AsyncResult
import org.vertx.java.core.http.HttpServer
import org.vertx.testtools.VertxAssert._

/**
 * Web socket Scala facade test.
 *
 * @author Galder Zamarreño
 */
class ScalaWebSocketTest extends TestVerticle {

  import org.vertx.scala.core._

  @Test
  def testHtmlOverWebSocket() {
    val html = <html><body><h1>Hello from Vert.x Scala Websocket!</h1></body></html>
    val port = 8080

    vertx.newHttpServer.websocketHandler(_.writeXml(html)).listen(port, {
      ar: AsyncResult[HttpServer] =>
        assertTrue(ar.succeeded())
        vertx.newHttpClient.setPort(port).connectWebsocket("/") { w: WebSocket =>
          w.dataHandler { b: Buffer =>
            assertEquals(html.toString(), b.toString)
            testComplete()
          }
        }
    })
  }

}
