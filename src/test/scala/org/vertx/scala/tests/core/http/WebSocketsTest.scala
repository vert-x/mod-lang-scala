package org.vertx.scala.tests.core.http

import org.vertx.scala.testtools.TestVerticle
import org.vertx.testtools.VertxAssert._
import org.junit.Test
import org.vertx.scala.core.AsyncResult
import org.vertx.scala.core.http.HttpServer
import org.vertx.scala.core.http.HttpClient
import org.vertx.scala.core.http.HttpClientResponse
import org.vertx.scala.core.http.ServerWebSocket
import org.vertx.scala.core.http.WebSocket
import org.vertx.java.core.buffer.Buffer
import org.vertx.scala.tests.util.TestUtils._

class WebSocketsTest extends TestVerticle {
  val testPort = 8844
  
  val html = <html>
               <head>
                 <title>test</title>
               </head>
               <body>
                 <h1>Hello world!</h1>
               </body>
             </html>.toString()

  @Test def websocketServer(): Unit = {
    vertx.createHttpServer.websocketHandler(regularRequestHandler).listen(testPort, checkServer({ c =>
      c.connectWebsocket("/", correctBodyHandler(testComplete))
    }))
  }

  @Test def secureWebsocketServer(): Unit = {
    val server = vertx.createHttpServer.setSSL(true)
  
    server.setKeyStorePath("./src/test/keystores/server-keystore.jks").setKeyStorePassword("wibble")
    server.setTrustStorePath("./src/test/keystores/server-truststore.jks").setTrustStorePassword("wibble")

    server.websocketHandler(regularRequestHandler).listen(testPort, checkServer({ c =>
      c.setSSL(true)
      c.setKeyStorePath("./src/test/keystores/client-keystore.jks").setKeyStorePassword("wibble")
      c.setTrustStorePath("./src/test/keystores/client-truststore.jks").setTrustStorePassword("wibble")
      c.exceptionHandler(ex => fail("Should not get exception but got " + ex)) connectWebsocket ("/", correctBodyHandler(testComplete))
    }))
  }

  private def regularRequestHandler: ServerWebSocket => Unit = { ws =>
    ws.write(new Buffer(html))
  }
  
  private def correctBodyHandler(fn: () => Unit) = { resp: WebSocket =>
    resp.dataHandler({ buf =>
      assertEquals(html, buf.toString)
      fn()
    }): Unit
  }
  
  private def checkServer(clientFn: HttpClient => Unit) = { ar: AsyncResult[HttpServer] =>
    if (ar.succeeded()) {
      val httpClient = vertx.createHttpClient.setPort(testPort)
      clientFn(httpClient)
    } else {
      fail("listening did not succeed: " + ar.cause().getMessage())
    }
  }
}