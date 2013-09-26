package org.vertx.scala.tests.core.http

import org.vertx.scala.testtools.TestVerticle
import org.junit.Test
import org.vertx.testtools.VertxAssert._
import org.vertx.scala.core.Vertx
import org.vertx.scala.core.AsyncResult
import org.vertx.scala.core.http.HttpServer
import org.vertx.scala.platform.Verticle
import org.vertx.scala.core.http.HttpServerResponse
import org.vertx.scala.core.http.HttpClientResponse
import scala.concurrent.Promise
import org.vertx.scala.core.http.HttpClient
import org.vertx.scala.tests.util.TestUtils._

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
    vertx.createHttpServer.requestHandler({ req =>
      req.response.end(html)
    }).listen(testPort, checkServer("/", { c =>
      c.getNow("/", correctBodyHandler(testComplete))
    }))
  }

  @Test
  def httpsServer(): Unit = {
    val server = vertx.createHttpServer.setSSL(true)

    server.setKeyStorePath("./src/test/keystores/server-keystore.jks").setKeyStorePassword("wibble")
    server.setTrustStorePath("./src/test/keystores/server-truststore.jks").setTrustStorePassword("wibble")

    server.requestHandler({ req =>
      req.response.end(html)
    }).listen(testPort, checkServer("/", { c =>
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

  @Test def postMethod(): Unit = fail("Missing test")

  @Test def getMethod(): Unit = fail("Missing test")

  private def correctBodyHandler(fn: () => Unit) = { resp: HttpClientResponse =>
    resp.bodyHandler({ buf =>
      assertEquals(html, buf.toString)
      fn()
    }): Unit
  }

  private def checkServer(path: String, clientFn: HttpClient => Unit) = { ar: AsyncResult[HttpServer] =>
    if (ar.succeeded()) {
      val httpClient = vertx.createHttpClient.setPort(testPort)
      clientFn(httpClient)
    } else {
      fail("listening did not succeed: " + ar.cause().getMessage())
    }
  }

}