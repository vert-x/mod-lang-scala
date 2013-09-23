package org.vertx.scala.tests.core.http

import org.junit.Test
import org.vertx.scala.core.AsyncResult
import org.vertx.scala.core.http.{HttpClient, HttpClientResponse, HttpServer, HttpServerRequest}
import org.vertx.scala.tests.util.TestUtils.completeWithArFailed
import org.vertx.scala.testtools.TestVerticle
import org.vertx.testtools.VertxAssert.{assertEquals, fail, testComplete}

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

  @Test
  def createHttpServer() {
    vertx.createHttpServer.requestHandler(regularRequestHandler).listen(testPort, checkServer({ c =>
      c.getNow("/", correctBodyHandler(testComplete))
    }))
  }

  @Test
  def httpsServer(): Unit = {
    val server = vertx.createHttpServer.setSSL(true)

    server.setKeyStorePath("./src/test/keystores/server-keystore.jks").setKeyStorePassword("wibble")
    server.setTrustStorePath("./src/test/keystores/server-truststore.jks").setTrustStorePassword("wibble")

    server.requestHandler(regularRequestHandler).listen(testPort, checkServer({ c =>
      c.setSSL(true)
      c.setKeyStorePath("./src/test/keystores/client-keystore.jks").setKeyStorePassword("wibble")
      c.setTrustStorePath("./src/test/keystores/client-truststore.jks").setTrustStorePassword("wibble")
      c.exceptionHandler(ex => fail("Should not get exception but got " + ex)) getNow ("/", correctBodyHandler(testComplete))
    }))
  }

  @Test
  def invalidPort(): Unit = {
    vertx.createHttpServer.requestHandler({ r => }).listen(1128371831, completeWithArFailed[HttpServer])
  }

  @Test
  def invalidHost(): Unit = {
    vertx.createHttpServer.requestHandler({ r => }) listen (testPort, "iqwjdoqiwjdoiqwdiojwd", completeWithArFailed)
  }

  @Test def postMethod(): Unit = {
    vertx.createHttpServer.requestHandler(regularRequestHandler).listen(testPort, checkServer({ c =>
      c.post("/", correctBodyHandler(testComplete)).end()
    }))
  }

  @Test def getMethod(): Unit = {
    vertx.createHttpServer.requestHandler(regularRequestHandler).listen(testPort, checkServer({ c =>
      c.get("/", correctBodyHandler(testComplete)).end()
    }))
  }

  @Test def headMethod(): Unit = {
    vertx.createHttpServer.requestHandler(regularRequestHandler).listen(testPort, checkServer({ c =>
      c.head("/", correctBodyHandler(testComplete)).end()
    }))
  }

  @Test def connectMethod(): Unit = {
    vertx.createHttpServer.requestHandler(regularRequestHandler).listen(testPort, checkServer({ c =>
      c.connect("/", correctBodyHandler(testComplete)).end()
    }))
  }

  @Test
  def getRequestMethod(): Unit = {
    simpleRequest("GET")
  }

  @Test
  def postRequestMethod(): Unit = {
    simpleRequest("POST")
  }

  @Test
  def putRequestMethod(): Unit = {
    simpleRequest("PUT")
  }

  @Test
  def deleteRequestMethod(): Unit = {
    simpleRequest("DELETE")
  }

  @Test
  def headRequestMethod(): Unit = {
    simpleRequest("HEAD")
  }

  @Test
  def traceRequestMethod(): Unit = {
    simpleRequest("TRACE")
  }

  @Test
  def connectRequestMethod(): Unit = {
    simpleRequest("CONNECT")
  }

  @Test
  def optionsRequestMethod(): Unit = {
    simpleRequest("OPTIONS")
  }

  @Test
  def patchRequestMethod(): Unit = simpleRequest("PATCH")

  private def simpleRequest(name: String): Unit = {
    vertx.createHttpServer.requestHandler(regularRequestHandler).listen(testPort, checkServer({ c =>
      c.request(name, "/", correctBodyHandler(testComplete)).end()
    }))
  }

  private def regularRequestHandler: HttpServerRequest => Unit = { req =>
    req.response.end(html)
  }

  private def correctBodyHandler(fn: () => Unit) = { resp: HttpClientResponse =>
    resp.bodyHandler({ buf =>
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