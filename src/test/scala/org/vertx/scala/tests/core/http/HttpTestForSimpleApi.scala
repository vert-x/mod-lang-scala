package org.vertx.scala.tests.core.http

import org.junit.Test
import org.vertx.scala.core.AsyncResult
import org.vertx.scala.core.http.{ HttpClient, HttpClientResponse, HttpServer, HttpServerRequest }
import org.vertx.scala.tests.util.TestUtils.completeWithArFailed
import org.vertx.scala.testtools.TestVerticle
import org.vertx.testtools.VertxAssert.{ assertEquals, fail, testComplete }

trait HttpTestForSimpleApi extends TestVerticle {
  val testPort = 8844

  val html = <html>
               <head>
                 <title>test</title>
               </head>
               <body>
                 <h1>Hello world!</h1>
               </body>
             </html>.toString()

  def createHttpServer() {
    vertx.createHttpServer.requestHandler(regularRequestHandler).listen(testPort, checkServer({ c =>
      c.getNow("/", correctHeadAndBodyHandler(testComplete))
    }))
  }

  def httpsServer(): Unit = {
    val server = vertx.createHttpServer.setSSL(true)

    server.setKeyStorePath("./src/test/keystores/server-keystore.jks").setKeyStorePassword("wibble")
    server.setTrustStorePath("./src/test/keystores/server-truststore.jks").setTrustStorePassword("wibble")

    server.requestHandler(regularRequestHandler).listen(testPort, checkServer({ c =>
      c.setSSL(true)
      c.setKeyStorePath("./src/test/keystores/client-keystore.jks").setKeyStorePassword("wibble")
      c.setTrustStorePath("./src/test/keystores/client-truststore.jks").setTrustStorePassword("wibble")
      c.exceptionHandler(ex => fail("Should not get exception but got " + ex)) getNow ("/", correctHeadAndBodyHandler(testComplete))
    }))
  }

  def invalidPort(): Unit = {
    vertx.createHttpServer.requestHandler({ r => }).listen(1128371831, completeWithArFailed[HttpServer])
  }

  def invalidHost(): Unit = {
    vertx.createHttpServer.requestHandler({ r => }) listen (testPort, "iqwjdoqiwjdoiqwdiojwd", completeWithArFailed)
  }

  def postMethod(): Unit = {
    vertx.createHttpServer.requestHandler(regularRequestHandler).listen(testPort, checkServer({ c =>
      c.post("/", correctHeadAndBodyHandler(testComplete)).end()
    }))
  }

  def getMethod(): Unit = {
    vertx.createHttpServer.requestHandler(regularRequestHandler).listen(testPort, checkServer({ c =>
      c.get("/", correctHeadAndBodyHandler(testComplete)).end()
    }))
  }

  def headMethod(): Unit = {
    vertx.createHttpServer.requestHandler(regularRequestHandler).listen(testPort, checkServer({ c =>
      c.head("/", correctHeadAndEmptyBodyHandler(testComplete)).end()
    }))
  }

  def connectMethod(): Unit = {
    vertx.createHttpServer.requestHandler(regularRequestHandler).listen(testPort, checkServer({ c =>
      c.connect("/", correctHeadAndEmptyBodyHandler(testComplete)).end()
    }))
  }

  def getRequestMethod(): Unit = headAndBodyRequest("GET")
  def postRequestMethod(): Unit = headAndBodyRequest("POST")
  def putRequestMethod(): Unit = headAndBodyRequest("PUT")
  def deleteRequestMethod(): Unit = headAndBodyRequest("DELETE")
  def headRequestMethod(): Unit = headOnlyRequest("HEAD")
  def traceRequestMethod(): Unit = headAndBodyRequest("TRACE")
  def connectRequestMethod(): Unit = headOnlyRequest("CONNECT")
  def optionsRequestMethod(): Unit = headAndBodyRequest("OPTIONS")
  def patchRequestMethod(): Unit = headAndBodyRequest("PATCH")

  private def simpleRequest(fn: (() => Unit) => HttpClientResponse => Unit)(name: String): Unit = {
    vertx.createHttpServer.requestHandler(regularRequestHandler).listen(testPort, checkServer({ c =>
      c.request(name, "/", fn(testComplete)).end()
    }))
  }

  private def headAndBodyRequest(name: String): Unit = simpleRequest(correctHeadAndBodyHandler)(name)
  private def headOnlyRequest(name: String): Unit = simpleRequest(correctHeadAndEmptyBodyHandler)(name)

  private def regularRequestHandler: HttpServerRequest => Unit = { req =>
    req.response.end(html)
  }

  private def checkCorrectHeader(fn: () => Unit) = { resp: HttpClientResponse =>
    assertEquals(200, resp.statusCode)
    assertEquals("OK", resp.statusMessage)
    fn()
  }

  private def correctHeadAndBodyHandler(fn: () => Unit) = { resp: HttpClientResponse =>
    checkCorrectHeader({ () =>
      resp.bodyHandler({ buf =>
        assertEquals(html, buf.toString)
        fn()
      })
    }).apply(resp): Unit
  }

  private def correctHeadAndEmptyBodyHandler(fn: () => Unit) = { resp: HttpClientResponse =>
    checkCorrectHeader({ () =>
      resp.bodyHandler { buf =>
        assertEquals(0, buf.length)
        fn()
      }
    }).apply(resp): Unit
  }

  private def checkServer(clientFn: HttpClient => Unit) = { ar: AsyncResult[HttpServer] =>
    if (ar.succeeded()) {
      val httpClient = vertx.createHttpClient.setPort(testPort)
      httpClient.setTryUseCompression(compression)
      clientFn(httpClient)
    } else {
      fail("listening did not succeed: " + ar.cause().getMessage())
    }
  }

  protected def compression: Boolean
}
