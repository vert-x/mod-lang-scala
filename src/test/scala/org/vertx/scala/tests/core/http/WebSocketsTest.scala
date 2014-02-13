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
import org.vertx.scala.core.buffer.Buffer
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
      assertThread()
      c.connectWebsocket("/", correctBodyHandler(testComplete))
    }))
  }

  @Test def secureWebsocketServer(): Unit = {
    val server = vertx.createHttpServer.setSSL(true)

    server.setKeyStorePath("./src/test/keystores/server-keystore.jks").setKeyStorePassword("wibble")
    server.setTrustStorePath("./src/test/keystores/server-truststore.jks").setTrustStorePassword("wibble")

    server.websocketHandler(regularRequestHandler).listen(testPort, checkServer({ c =>
      assertThread()
      c.setSSL(true)
      c.setKeyStorePath("./src/test/keystores/client-keystore.jks").setKeyStorePassword("wibble")
      c.setTrustStorePath("./src/test/keystores/client-truststore.jks").setTrustStorePassword("wibble")
      c.exceptionHandler(ex => fail("Should not get exception but got " + ex)) connectWebsocket ("/", correctBodyHandler(testComplete))
    }))
  }

  @Test def pingPongMessages(): Unit = {
    vertx.createHttpServer.websocketHandler({ ws: ServerWebSocket =>
      ws.dataHandler({ buf =>
        assertThread()
        if (buf.toString() == "ping") {
          ws.write(Buffer("pong"))
        } else if (buf.toString() == "ping2") {
          ws.write(Buffer("pong2"))
        }
      })
    }).listen(testPort, checkServer({ c =>
      assertThread()
      c.connectWebsocket("/", { resp: WebSocket =>
        assertThread()
        resp.dataHandler({ buf =>
          assertThread()
          assertEquals("pong", buf.toString)
          resp.dataHandler({ buf =>
            assertThread()
            assertEquals("pong2", buf.toString)
            testComplete()
          })
          resp.write(Buffer("ping2"))
        })
        resp.write(Buffer("ping"))
      })
    }))
  }

  @Test def websocketAddresses(): Unit = {
    vertx.createHttpServer.websocketHandler({ ws: ServerWebSocket =>
      assertNotNull(ws.remoteAddress())
      assertNotNull(ws.localAddress())
      ws.dataHandler({ buf =>
        assertThread()
        val addresses = buf.toString().split("\n")
        assertEquals(ws.remoteAddress().getAddress().getHostAddress().toString(), addresses(1))
        assertEquals(ws.localAddress().getAddress().getHostAddress().toString(), addresses(0))
        testComplete()
      })
    }).listen(testPort, checkServer({ c =>
      c.connectWebsocket("/", { ws: WebSocket =>
        assertThread()
        ws.write(Buffer(ws.remoteAddress().getAddress().getHostAddress().toString() + "\n"
          + ws.localAddress().getAddress().getHostAddress().toString()))
      })
    }))
  }

  private def regularRequestHandler: ServerWebSocket => Unit = { ws =>
    ws.write(Buffer(html))

    ws.dataHandler({ buf =>
      assertThread()
      assertEquals(html, buf.toString)
      testComplete()
    }): Unit
  }

  private def correctBodyHandler(fn: () => Unit) = { resp: WebSocket =>
    resp.dataHandler({ buf =>
      assertThread()
      assertEquals(html, buf.toString)
      fn()
    }): Unit
  }

  private def checkServer(clientFn: HttpClient => Unit) = { ar: AsyncResult[HttpServer] =>
    assertThread()
    if (ar.succeeded()) {
      val httpClient = vertx.createHttpClient.setPort(testPort)
      clientFn(httpClient)
    } else {
      fail("listening did not succeed: " + ar.cause().getMessage())
    }
  }
}