package org.vertx.scala.tests.core.http

import org.junit.Test
import org.vertx.scala.core.AsyncResult
import org.vertx.scala.core.http.{ HttpClient, HttpClientResponse, HttpServer, HttpServerRequest }
import org.vertx.scala.tests.util.TestUtils.completeWithArFailed
import org.vertx.scala.testtools.TestVerticle
import org.vertx.testtools.VertxAssert.{ assertEquals, fail, testComplete }

class HttpTestForSimpleApi extends TestVerticle {
  val testPort = 8844

  val html = <html>
               <head>
                 <title>test</title>
               </head>
               <body>
                 <h1>Hello world!</h1>
               </body>
             </html>.toString()

  @Test def createHttpServer() {
    vertx.createHttpServer.requestHandler(regularRequestHandler).listen(testPort, checkServer({ c =>
      c.getNow("/", correctHeadAndBodyHandler(c, testComplete))
    }))
  }

  @Test def httpsServer(): Unit = {
    val server = vertx.createHttpServer.setSSL(true)

    server.setKeyStorePath("./src/test/keystores/server-keystore.jks").setKeyStorePassword("wibble")
    server.setTrustStorePath("./src/test/keystores/server-truststore.jks").setTrustStorePassword("wibble")

    server.requestHandler(regularRequestHandler).listen(testPort, checkServer({ c =>
      c.setSSL(true)
      c.setKeyStorePath("./src/test/keystores/client-keystore.jks").setKeyStorePassword("wibble")
      c.setTrustStorePath("./src/test/keystores/client-truststore.jks").setTrustStorePassword("wibble")
      c.exceptionHandler(ex => fail("Should not get exception but got " + ex)) getNow ("/", correctHeadAndBodyHandler(c, testComplete))
    }))
  }

  @Test def invalidPort(): Unit = {
    vertx.createHttpServer.requestHandler({ r => }).listen(1128371831, completeWithArFailed[HttpServer])
  }

  @Test def invalidHost(): Unit = {
    vertx.createHttpServer.requestHandler({ r => }) listen (testPort, "iqwjdoqiwjdoiqwdiojwd", completeWithArFailed)
  }

  @Test def postMethod(): Unit = {
    vertx.createHttpServer.requestHandler(regularRequestHandler).listen(testPort, checkServer({ c =>
      c.post("/", correctHeadAndBodyHandler(c, testComplete)).end()
    }))
  }

  @Test def getMethod(): Unit = {
    vertx.createHttpServer.requestHandler(regularRequestHandler).listen(testPort, checkServer({ c =>
      c.get("/", correctHeadAndBodyHandler(c, testComplete)).end()
    }))
  }

  @Test def headMethod(): Unit = {
    vertx.createHttpServer.requestHandler(regularRequestHandler).listen(testPort, checkServer({ c =>
      c.head("/", correctHeadAndEmptyBodyHandler(c, testComplete)).end()
    }))
  }

  @Test def connectMethod(): Unit = {
    vertx.createHttpServer.requestHandler(regularRequestHandler).listen(testPort, checkServer({ c =>
      c.connect("/", correctHeadAndEmptyBodyHandler(c, testComplete)).end()
    }))
  }

  @Test def getRequestMethod(): Unit = headAndBodyRequest("GET")
  @Test def postRequestMethod(): Unit = headAndBodyRequest("POST")
  @Test def putRequestMethod(): Unit = headAndBodyRequest("PUT")
  @Test def deleteRequestMethod(): Unit = headAndBodyRequest("DELETE")
  @Test def headRequestMethod(): Unit = headOnlyRequest("HEAD")
  @Test def traceRequestMethod(): Unit = headAndBodyRequest("TRACE")
  @Test def connectRequestMethod(): Unit = headOnlyRequest("CONNECT")
  @Test def optionsRequestMethod(): Unit = headAndBodyRequest("OPTIONS")
  @Test def patchRequestMethod(): Unit = headAndBodyRequest("PATCH")

  private def simpleRequest(fn: (HttpClient, () => Unit) => HttpClientResponse => Unit)(name: String): Unit = {
    vertx.createHttpServer.requestHandler(regularRequestHandler).listen(testPort, checkServer({ c =>
      c.request(name, "/", fn(c, testComplete)).end()
    }))
  }

  private def headAndBodyRequest(name: String): Unit = simpleRequest(correctHeadAndBodyHandler)(name)
  private def headOnlyRequest(name: String): Unit = simpleRequest(correctHeadAndEmptyBodyHandler)(name)

  private def regularRequestHandler: HttpServerRequest => Unit = { req =>
    req.response.end(html)
  }

  private def checkCorrectHeader(c: HttpClient, fn: () => Unit) = { resp: HttpClientResponse =>
    assertEquals(assertMessage(c), 200, resp.statusCode)
    assertEquals(assertMessage(c), "OK", resp.statusMessage)
    fn()
  }

  private def correctHeadAndBodyHandler(c: HttpClient, fn: () => Unit) = { resp: HttpClientResponse =>
    checkCorrectHeader(c, { () =>
      resp.bodyHandler({ buf =>
        assertEquals(assertMessage(c), html, buf.toString)
        fn()
      })
    }).apply(resp): Unit
  }

  private def correctHeadAndEmptyBodyHandler(c: HttpClient, fn: () => Unit) = { resp: HttpClientResponse =>
    checkCorrectHeader(c, { () =>
      resp.bodyHandler { buf =>
        assertEquals(assertMessage(c), 0, buf.length)
        fn()
      }
    }).apply(resp): Unit
  }

  private def checkServer(clientFn: HttpClient => Unit) = { ar: AsyncResult[HttpServer] =>
    if (ar.succeeded()) {
      val clients = List(
        vertx.createHttpClient().setPort(testPort).setTryUseCompression(tryUseCompression = false),
        vertx.createHttpClient().setPort(testPort).setTryUseCompression(tryUseCompression = true))

      clients.foreach(clientFn)
    } else {
      fail("listening did not succeed: " + ar.cause().getMessage)
    }
  }

  private def assertMessage(c: HttpClient) =
    s"Assert failed for client(compression=$c.getTryUseCompression())"

}
