package org.vertx.scala.tests.core.http

import org.junit.Test
import org.vertx.scala.core.http.{ HttpClient, HttpClientResponse, HttpServer, HttpServerRequest }
import org.vertx.scala.tests.util.TestUtils.completeWithArFailed
import org.vertx.scala.testtools.TestVerticle
import org.vertx.testtools.VertxAssert._
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

  @Test def httpServer(): Unit = {
    checkServer(vertx.createHttpServer(), regularRequestHandler) { c =>
      c.getNow("/", correctHeadAndBodyHandler(c, testComplete))
    }
  }

  @Test def httpsServer(): Unit = {
    checkServer(vertx.createHttpServer()
          .setSSL(ssl = true)
          .setKeyStorePath("./src/test/keystores/server-keystore.jks")
          .setKeyStorePassword("wibble")
          .setTrustStorePath("./src/test/keystores/server-truststore.jks")
          .setTrustStorePassword("wibble"), regularRequestHandler) { c =>
      c.setSSL(ssl = true)
      c.setKeyStorePath("./src/test/keystores/client-keystore.jks").setKeyStorePassword("wibble")
      c.setTrustStorePath("./src/test/keystores/client-truststore.jks").setTrustStorePassword("wibble")
      c.exceptionHandler(ex => fail("Should not get exception but got " + ex)) getNow ("/", correctHeadAndBodyHandler(c, testComplete))
    }
  }

  @Test def invalidPort(): Unit = {
    vertx.createHttpServer().requestHandler({ r => }).listen(1128371831, completeWithArFailed[HttpServer])
  }

  @Test def invalidHost(): Unit = {
    vertx.createHttpServer().requestHandler({ r => }) listen (testPort, "iqwjdoqiwjdoiqwdiojwd", completeWithArFailed)
  }

  @Test def httpPostMethod(): Unit = {
    checkServer(vertx.createHttpServer(), regularRequestHandler) { c =>
      c.post("/", correctHeadAndBodyHandler(c, testComplete)).end()
    }
  }

  @Test def httpGetMethod(): Unit = {
    checkServer(vertx.createHttpServer(), regularRequestHandler) { c =>
      c.get("/", correctHeadAndBodyHandler(c, testComplete)).end()
    }
  }

  @Test def httpHeadMethod(): Unit = {
    checkServer(vertx.createHttpServer(), regularRequestHandler) { c =>
      c.head("/", correctHeadAndEmptyBodyHandler(c, testComplete)).end()
    }
  }

  @Test def httpConnectMethod(): Unit = {
    checkServer(vertx.createHttpServer(), regularRequestHandler) { c =>
      c.connect("/", correctHeadAndEmptyBodyHandler(c, testComplete)).end()
    }
  }

  @Test def httpGetRequestMethod(): Unit = headAndBodyRequest("GET")
  @Test def httpPostRequestMethod(): Unit = headAndBodyRequest("POST")
  @Test def httpPutRequestMethod(): Unit = headAndBodyRequest("PUT")
  @Test def httpDeleteRequestMethod(): Unit = headAndBodyRequest("DELETE")
  @Test def httpHeadRequestMethod(): Unit = headOnlyRequest("HEAD")
  @Test def httpTraceRequestMethod(): Unit = headAndBodyRequest("TRACE")
  @Test def httpConnectRequestMethod(): Unit = headOnlyRequest("CONNECT")
  @Test def httpOptionsRequestMethod(): Unit = headAndBodyRequest("OPTIONS")
  @Test def httpPatchRequestMethod(): Unit = headAndBodyRequest("PATCH")

  @Test def sendFile(): Unit = {
    val (file, content) = generateRandomContentFile("test-send-file.html", 10000)
    checkServer(vertx.createHttpServer(), _.response().sendFile(file.getAbsolutePath)) { c =>
      c.getNow("some-uri", { res =>
        assertEquals(200, res.statusCode())
        assertEquals(file.length(), res.headers().get("content-length").toLong)
        assertEquals("text/html", res.headers().get("content-type"))
        res.bodyHandler { buff =>
          assertEquals(content, buff.toString())
          file.delete()
          testComplete()
        }
      })
    }
  }

  @Test def sendFileWithHandler(): Unit = {
    val (file, content) = generateRandomContentFile("test-send-file.html", 10000)
    checkServer(vertx.createHttpServer(), _.response().sendFile(file.getAbsolutePath, { res =>
      assertTrue(res.succeeded())
      testComplete()
    } )) { c =>
      c.getNow("some-uri", { res =>
        assertEquals(200, res.statusCode())
        assertEquals(file.length(), res.headers().get("content-length").toLong)
        assertEquals("text/html", res.headers().get("content-type"))
        res.bodyHandler { buff =>
          assertEquals(content, buff.toString())
          file.delete()
        }
      })
    }
  }

  @Test def sendFileNotFound(): Unit = {
    checkServer(vertx.createHttpServer(), _.response().sendFile("doesnotexist.html")) { c =>
      c.getNow("some-uri", { res =>
        assertEquals(404, res.statusCode())
        assertEquals("text/html", res.headers().get("content-type"))
        res.bodyHandler { buff =>
          assertEquals("<html><body>Resource not found</body><html>", buff.toString())
          testComplete()
        }
      })
    }
  }

  @Test def sendFileNotFoundWith404Page(): Unit = {
    val (file, content) = generateFile("my-404-page.html", "<html><body>This is my 404 page</body></html>")
    checkServer(vertx.createHttpServer(),
        _.response().sendFile("doesnotexist.html", file.getAbsolutePath)
    ) { c =>
      c.getNow("some-uri", { res =>
        assertEquals(404, res.statusCode())
        assertEquals("text/html", res.headers().get("content-type"))
        res.bodyHandler { buff =>
          assertEquals(content, buff.toString())
          testComplete()
        }
      })
    }
  }

  @Test def sendFileNotFoundWith404PageAndHandler(): Unit = {
    val (file, content) = generateFile("my-404-page.html", "<html><body>This is my 404 page</body></html>")
    checkServer(vertx.createHttpServer(),
      _.response().sendFile("doesnotexist.html", file.getAbsolutePath, { res =>
        assertTrue(res.succeeded())
        testComplete()
      })
    ) { c =>
      c.getNow("some-uri", { res =>
        assertEquals(404, res.statusCode())
        assertEquals("text/html", res.headers().get("content-type"))
        res.bodyHandler { buff =>
          assertEquals(content, buff.toString())
        }
      })
    }
  }

  @Test def sendFileOverrideHeaders(): Unit = {
    val (file, content) = generateRandomContentFile("test-send-file.html", 10000)
    checkServer(vertx.createHttpServer(),
        _.response().putHeader("Content-Type", "wibble").sendFile(file.getAbsolutePath)
    ) { c =>
      c.getNow("some-uri", { res =>
        assertEquals(200, res.statusCode())
        assertEquals(file.length(), res.headers().get("content-length").toLong)
        assertEquals("wibble", res.headers().get("content-type"))
        res.bodyHandler { buff =>
          assertEquals(content, buff.toString())
          file.delete()
          testComplete()
        }
      })
    }
  }

  private def simpleRequest(fn: (HttpClient, () => Unit) => HttpClientResponse => Unit)(name: String): Unit = {
    checkServer(vertx.createHttpServer(), regularRequestHandler) { c =>
      c.request(name, "/", fn(c, testComplete)).end()
    }
  }

  private def headAndBodyRequest(name: String): Unit = simpleRequest(correctHeadAndBodyHandler)(name)
  private def headOnlyRequest(name: String): Unit = simpleRequest(correctHeadAndEmptyBodyHandler)(name)

  private def regularRequestHandler: HttpServerRequest => Unit = { req =>
    req.response().end(html)
  }

  private def checkCorrectHeader(c: HttpClient, fn: () => Unit) = { resp: HttpClientResponse =>
    assertEquals(assertMessage(c), 200, resp.statusCode())
    assertEquals(assertMessage(c), "OK", resp.statusMessage())
    fn()
  }

  private def correctHeadAndBodyHandler(c: HttpClient, fn: () => Unit) = { resp: HttpClientResponse =>
    checkCorrectHeader(c, { () =>
      resp.bodyHandler({ buf =>
        assertEquals(assertMessage(c), html, buf.toString())
        fn()
      })
    }).apply(resp): Unit
  }

  private def correctHeadAndEmptyBodyHandler(c: HttpClient, fn: () => Unit) = { resp: HttpClientResponse =>
    checkCorrectHeader(c, { () =>
      resp.bodyHandler { buf =>
        assertEquals(assertMessage(c), 0, buf.length())
        fn()
      }
    }).apply(resp): Unit
  }

  private def checkServer(server: => HttpServer, req: HttpServerRequest => Unit)(fn: HttpClient => Unit) = {
    List(true, false).foreach { compression =>
      val localServer = server // on purpose, call by-name function to create server
      localServer.setCompressionSupported(compressionSupported = compression)
      localServer.requestHandler(req)
      localServer.listen(testPort, { res =>
        if (res.succeeded()) {
          val client = vertx.createHttpClient().setPort(testPort).setTryUseCompression(compression)
          fn(client)
        } else {
          fail("listening did not succeed: " + res.cause().getMessage)
        }
      })
    }
  }

  private def assertMessage(c: HttpClient) =
    s"Assert failed for client(compression=$c.getTryUseCompression())"

}
