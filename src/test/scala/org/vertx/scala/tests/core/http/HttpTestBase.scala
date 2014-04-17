package org.vertx.scala.tests.core.http

import org.vertx.testtools.VertxAssert._
import org.vertx.scala.tests.util.TestUtils._
import org.vertx.scala.core.http.{HttpClientResponse, HttpClient, HttpServerRequest, HttpServer}
import java.util.concurrent.atomic.AtomicInteger
import org.vertx.scala.core.buffer.Buffer
import java.net.URLEncoder
import org.vertx.scala.core.Vertx
import scala.concurrent.{Await, Promise}
import scala.concurrent.duration._

/**
 * @author Galder Zamarreño
 */
object HttpTestBase {

  val testPort = 8844

  val html = <html>
    <head>
      <title>test</title>
    </head>
    <body>
      <h1>Hello world!</h1>
    </body>
  </html>.toString()

  def httpServer()(implicit v: Vertx, c: Compression): Unit = {
    checkServer(v.createHttpServer(), regularRequestHandler) { c =>
      c.getNow("/", correctHeadAndBodyHandler(c, testComplete))
    }
  }

  def httpsServer()(implicit v: Vertx, c: Compression): Unit = {
    checkServer(v.createHttpServer()
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

  def invalidPort()(implicit v: Vertx, c: Compression): Unit = {
    v.createHttpServer().requestHandler({ r => }).listen(1128371831, completeWithArFailed[HttpServer])
  }

  def invalidHost()(implicit v: Vertx, c: Compression): Unit = {
    v.createHttpServer().requestHandler({ r => }) listen (testPort, "iqwjdoqiwjdoiqwdiojwd", completeWithArFailed)
  }

  def httpPostMethod()(implicit v: Vertx, c: Compression): Unit = {
    checkServer(v.createHttpServer(), regularRequestHandler) { c =>
      c.post("/", correctHeadAndBodyHandler(c, testComplete)).end()
    }
  }

  def httpGetMethod()(implicit v: Vertx, c: Compression): Unit = {
    checkServer(v.createHttpServer(), regularRequestHandler) { c =>
      c.get("/", correctHeadAndBodyHandler(c, testComplete)).end()
    }
  }

  def httpHeadMethod()(implicit v: Vertx, c: Compression): Unit = {
    checkServer(v.createHttpServer(), regularRequestHandler) { c =>
      c.head("/", correctHeadAndEmptyBodyHandler(c, testComplete)).end()
    }
  }

  def httpConnectMethod()(implicit v: Vertx, c: Compression): Unit = {
    checkServer(v.createHttpServer(), regularRequestHandler) { c =>
      c.connect("/", correctHeadAndEmptyBodyHandler(c, testComplete)).end()
    }
  }

  def httpGetRequestMethod()(implicit v: Vertx, c: Compression): Unit = headAndBodyRequest("GET")
  def httpPostRequestMethod()(implicit v: Vertx, c: Compression): Unit = headAndBodyRequest("POST")
  def httpPutRequestMethod()(implicit v: Vertx, c: Compression): Unit = headAndBodyRequest("PUT")
  def httpDeleteRequestMethod()(implicit v: Vertx, c: Compression): Unit = headAndBodyRequest("DELETE")
  def httpHeadRequestMethod()(implicit v: Vertx, c: Compression): Unit = headOnlyRequest("HEAD")
  def httpTraceRequestMethod()(implicit v: Vertx, c: Compression): Unit = headAndBodyRequest("TRACE")
  def httpConnectRequestMethod()(implicit v: Vertx, c: Compression): Unit = headOnlyRequest("CONNECT")
  def httpOptionsRequestMethod()(implicit v: Vertx, c: Compression): Unit = headAndBodyRequest("OPTIONS")
  def httpPatchRequestMethod()(implicit v: Vertx, c: Compression): Unit = headAndBodyRequest("PATCH")

  def sendFile()(implicit v: Vertx, c: Compression): Unit = {
    val (file, content) = generateRandomContentFile("test-send-file.html", 10000)
    checkServer(v.createHttpServer(), _.response().sendFile(file.getAbsolutePath)) { c =>
      c.getNow("some-uri", { res =>
        assertEquals(200, res.statusCode())
        val headers = res.headers()
        assertTrue(headers.entryExists("content-length", _.toLong == file.length()))
        assertTrue(headers.entryExists("content-type", _ == "text/html"))
        res.bodyHandler { buff =>
          assertEquals(content, buff.toString())
          file.delete()
          testComplete()
        }
      })
    }
  }

  def sendFileWithHandler()(implicit v: Vertx, c: Compression): Unit = {
    val (file, content) = generateRandomContentFile("test-send-file.html", 10000)
    var sendComplete = false
    checkServer(v.createHttpServer(), _.response().sendFile(file.getAbsolutePath, { res =>
      sendComplete = true
    } )) { c =>
      c.getNow("some-uri", { res =>
        assertEquals(200, res.statusCode())
        val headers = res.headers()
        assertTrue(headers.entryExists("content-length", _.toLong == file.length()))
        assertTrue(headers.entryExists("content-type", _ == "text/html"))
        res.bodyHandler { buff =>
          assertEquals(content, buff.toString())
          file.delete()
          assertTrue(sendComplete)
          testComplete()
        }
      })
    }
  }

  def sendFileNotFound()(implicit v: Vertx, c: Compression): Unit = {
    checkServer(v.createHttpServer(), _.response().sendFile("doesnotexist.html")) { c =>
      c.getNow("some-uri", { res =>
        assertEquals(404, res.statusCode())
        assertTrue(res.headers().entryExists("content-type", _ == "text/html"))
        res.bodyHandler { buff =>
          assertEquals("<html><body>Resource not found</body><html>", buff.toString())
          testComplete()
        }
      })
    }
  }

  def sendFileNotFoundWith404Page()(implicit v: Vertx, c: Compression): Unit = {
    val (file, content) = generateFile("my-404-page.html", "<html><body>This is my 404 page</body></html>")
    checkServer(v.createHttpServer(),
      _.response().sendFile("doesnotexist.html", file.getAbsolutePath)
    ) { c =>
      c.getNow("some-uri", { res =>
        assertEquals(404, res.statusCode())
        assertTrue(res.headers().entryExists("content-type", _ == "text/html"))
        res.bodyHandler { buff =>
          assertEquals(content, buff.toString())
          testComplete()
        }
      })
    }
  }

  def sendFileNotFoundWith404PageAndHandler()(implicit v: Vertx, c: Compression): Unit = {
    val (file, content) = generateFile("my-404-page.html", "<html><body>This is my 404 page</body></html>")
    val sendFileHandlerPromise = Promise[Boolean]()
    checkServer(v.createHttpServer(),
      _.response().sendFile("doesnotexist.html", file.getAbsolutePath, { res =>
        if (res.succeeded()) sendFileHandlerPromise.success(res.succeeded())
        else sendFileHandlerPromise.failure(res.cause())
      })
    ) { c =>
      c.getNow("some-uri", { res =>
        assertEquals(404, res.statusCode())
        assertTrue(res.headers().entryExists("content-type", _ == "text/html"))
        res.bodyHandler { buff =>
          assertEquals(content, buff.toString())
          assertTrue(Await.result(sendFileHandlerPromise.future, 10 second))
          testComplete()
        }
      })
    }
  }

  def sendFileOverrideHeaders()(implicit v: Vertx, c: Compression): Unit = {
    val (file, content) = generateRandomContentFile("test-send-file.html", 10000)
    checkServer(v.createHttpServer(),
      _.response().putHeader("Content-Type", "wibble").sendFile(file.getAbsolutePath)
    ) { c =>
      c.getNow("some-uri", { res =>
        assertEquals(200, res.statusCode())
        val headers = res.headers()
        assertTrue(headers.entryExists("content-length", _.toLong == file.length()))
        assertTrue(headers.entryExists("content-type", _ == "wibble"))
        res.bodyHandler { buff =>
          assertEquals(content, buff.toString())
          file.delete()
          testComplete()
        }
      })
    }
  }

  def formUploadAttributes()(implicit v: Vertx, c: Compression): Unit = {
    val attributeCount = new AtomicInteger()
    checkServer(v.createHttpServer(), req =>
      if (req.uri().startsWith("/form")) {
        req.response().setChunked(chunked = true)
        req.expectMultiPart(expect = true)
        req.uploadHandler(_.dataHandler(buffer => fail()))
        req.endHandler({
          val attrs = req.formAttributes()
          attributeCount.set(attrs.size)
          assertEquals("vert x", attrs("framework").head)
          assertEquals("jvm", attrs("runson").head)
          req.response().end()
        })
      }
    ) { c =>
      val req = c.post("/form", resp => {
        // assert the response
        assertEquals(200, resp.statusCode())
        resp.bodyHandler(body => {
          assertEquals(0, body.length())
          assertEquals(2, attributeCount.get())
          testComplete()
        })
      })

      val buffer = Buffer()
      buffer.append("framework=" + URLEncoder.encode("vert x", "UTF-8") + "&runson=jvm", "UTF-8")
      req.headers().addBinding("content-length", String.valueOf(buffer.length()))
      req.headers().addBinding("content-type", "application/x-www-form-urlencoded")
      req.write(buffer).end()
    }
  }

  def formUploadAttributes2()(implicit v: Vertx, c: Compression): Unit = {
    val attributeCount = new AtomicInteger()
    checkServer(v.createHttpServer(), req =>
      if (req.uri().startsWith("/form")) {
        req.response().setChunked(chunked = true)
        req.expectMultiPart(expect = true)
        req.uploadHandler(_.dataHandler(buffer => fail()))
        req.endHandler({
          val attrs = req.formAttributes()
          attributeCount.set(attrs.size)
          assertEquals("junit-testUserAlias", attrs("origin").head)
          assertEquals("admin@foo.bar", attrs("login").head)
          assertEquals("admin", attrs("pass word").head)
          req.response().end()
        })
      }
    ) { c =>
      val req = c.post("/form", resp => {
        // assert the response
        assertEquals(200, resp.statusCode())
        resp.bodyHandler(body => {
          assertEquals(0, body.length())
          assertEquals(3, attributeCount.get())
          testComplete()
        })
      })

      val buffer = Buffer()
      buffer.append("origin=junit-testUserAlias&login=admin%40foo.bar&pass+word=admin")
      req.headers().addBinding("content-length", String.valueOf(buffer.length()))
      req.headers().addBinding("content-type", "application/x-www-form-urlencoded")
      req.write(buffer).end()
    }
  }

  def formUploadFile()(implicit v: Vertx, c: Compression): Unit = {
    val attributeCount = new AtomicInteger()
    val content = "Vert.x rocks!"
    checkServer(v.createHttpServer(), req =>
      if (req.uri().startsWith("/form")) {
        req.response().setChunked(chunked = true)
        req.expectMultiPart(expect = true)
        req.uploadHandler(upload => {
          upload.dataHandler(buff => assertEquals(content, buff.toString("UTF-8")))
          assertEquals("file", upload.name())
          assertEquals("tmp-0.txt", upload.filename())
          assertEquals("image/gif", upload.contentType())
          upload.endHandler(
            assertEquals(upload.size(), content.length)
          )
        })
        req.endHandler({
          val attrs = req.formAttributes()
          attributeCount.set(attrs.size)
          req.response().end()
        })
      }
    ) { c =>
      val req = c.post("/form", resp => {
        // assert the response
        assertEquals(200, resp.statusCode())
        resp.bodyHandler(body => {
          assertEquals(0, body.length())
          assertEquals(0, attributeCount.get())
          testComplete()
        })
      })

      val boundary = "dLV9Wyq26L_-JQxk6ferf-RT153LhOO"
      val buffer = Buffer()
      val body =
        "--" + boundary + "\r\n" +
          "Content-Disposition: form-data; name=\"file\"; filename=\"tmp-0.txt\"\r\n" +
          "Content-Type: image/gif\r\n" +
          "\r\n" +
          content + "\r\n" +
          "--" + boundary + "--\r\n"

      buffer.append(body)
      req.headers().addBinding("content-length", String.valueOf(buffer.length()))
      req.headers().addBinding("content-type", "multipart/form-data; boundary=" + boundary)
      req.write(buffer).end()
    }
  }

  def sendFileMultipleOverrideHeaders()(implicit v: Vertx, c: Compression): Unit = {
    val (file, content) = generateRandomContentFile("test-send-file.html", 10000)
    checkServer(v.createHttpServer(),
      _.response().putHeader("ConTeNt-TypE", "wibble", "wibble2", "wibble3").sendFile(file.getAbsolutePath)
    ) { c =>
      c.getNow("some-uri", { res =>
        assertEquals(200, res.statusCode())
        val headers = res.headers()
        assertTrue(headers.entryExists("content-type", _ == "wibble"))
        assertTrue(headers.entryExists("content-type", _ == "wibble2"))
        assertTrue(headers.entryExists("content-type", _ == "wibble3"))
        res.bodyHandler { buff =>
          assertEquals(content, buff.toString())
          file.delete()
          testComplete()
        }
      })
    }
  }

  def headerOverridesPossible()(implicit v: Vertx, c: Compression): Unit =  {
    val serverHandler = { req: HttpServerRequest =>
      // assertThread()
      assertTrue(req.headers().entryExists("Host", _ == "localhost:4444"))
      assertTrue(req.headers().entryExists("content-type", _ == "wibble"))
      assertTrue(req.headers().entryExists("content-type", _ == "wibble2"))
      assertTrue(req.headers().entryExists("content-type", _ == "wibble3"))
      req.response().end()
    }

    val clientHandler = { client: HttpClient =>
      val req = client.get("some-uri", { res: HttpClientResponse =>
        // assertThread()
        testComplete()
      })
      req.putHeader("Host", "localhost:4444")
      req.putHeader("Content-Type", "wibble", "wibble2", "wibble3")
      req.end()
    }

    checkServer(v.createHttpServer(), serverHandler)(clientHandler)
  }

  def continue100Default()(implicit v: Vertx, c: Compression): Unit = {
    val buffer = generateRandomBuffer(1000)

    val serverHandler = { req: HttpServerRequest =>
      req.bodyHandler { data =>
        // assertThread()
        assertEquals(buffer, data)
        req.response().end()
      }
      ()
    }

    val clientHandler = { client: HttpClient =>
      val req = client.get("some-uri", { res: HttpClientResponse =>
        res.endHandler {
          // assertThread()
          testComplete()
        }
      })
      req.headers().addBinding("Expect", "100-continue")
      req.setChunked(chunked = true)
      req.continueHandler {
        // assertThread()
        req.write(buffer)
        req.end()
      }
      req.sendHead()
      ()
    }

    checkServer(v.createHttpServer(), serverHandler)(clientHandler)
  }

  def continue100Handled()(implicit v: Vertx, c: Compression): Unit = {
    val buffer = generateRandomBuffer(1000)

    val serverHandler: HttpServerRequest => Unit = { req =>
      req.response().headers().addBinding("HTTP/1.1", "100 Continue")
      req.bodyHandler { data =>
        // assertThread()
        assertEquals(buffer, data)
        req.response().end()
      }
    }

    def clientHandler(client: HttpClient): Unit = {
      val req = client.get("some-uri", { res: HttpClientResponse =>
        res.endHandler {
          // assertThread()
          testComplete()
        }
      })
      req.headers().addBinding("Expect", "100-continue")
      req.setChunked(chunked = true)
      req.continueHandler {
        // assertThread()
        req.write(buffer)
        req.end()
      }
      req.sendHead()
    }

    checkServer(v.createHttpServer(), serverHandler)(clientHandler)
  }

  private def simpleRequest(fn: (HttpClient, () => Unit) => HttpClientResponse => Unit)(name: String)
        (implicit v: Vertx, c: Compression): Unit = {
    checkServer(v.createHttpServer(), regularRequestHandler) { c =>
      c.request(name, "/", fn(c, testComplete)).end()
    }
  }

  private def headAndBodyRequest(name: String)(implicit v: Vertx, c: Compression): Unit =
      simpleRequest(correctHeadAndBodyHandler)(name)

  private def headOnlyRequest(name: String)(implicit v: Vertx, c: Compression): Unit =
    simpleRequest(correctHeadAndEmptyBodyHandler)(name)

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

  def checkServer(server: => HttpServer, req: HttpServerRequest => Unit)(fn: HttpClient => Unit)
        (implicit v: Vertx, c: Compression) = {
    val localServer = server // on purpose, call by-name function to create server
    localServer.setCompressionSupported(compressionSupported = c.enabled())
    localServer.requestHandler(req)
    localServer.listen(testPort, { res =>
      if (res.succeeded()) {
        val client = v.createHttpClient().setPort(testPort).setTryUseCompression(c.enabled())
        // assertThread()
        fn(client)
      } else {
        fail("listening did not succeed: " + res.cause().getMessage)
      }
    })
  }

  private def assertMessage(c: HttpClient) =
    s"Assert failed for client(compression=$c.getTryUseCompression())"

}
