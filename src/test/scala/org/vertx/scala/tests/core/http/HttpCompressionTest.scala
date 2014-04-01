package org.vertx.scala.tests.core.http

import org.junit.Test
import org.vertx.scala.tests.util.TestUtils._
import org.vertx.testtools.VertxAssert._
import org.vertx.scala.testtools.TestVerticle
import org.vertx.scala.core.Vertx

final class HttpCompressionTest extends TestVerticle {

  implicit lazy val implicitVertx: Vertx = vertx
  implicit val compression: Compression = Compressed

  // Sharing tests in superclasses causes issues with JUnit in certain
  // environments, and having a single test class run multiple configurations
  // has been problematic as well, so as last resort, duplicate test methods

  @Test def httpServer(): Unit = HttpTestBase.httpServer()
  @Test def httpsServer(): Unit = HttpTestBase.httpsServer()
  @Test def invalidPort(): Unit = HttpTestBase.invalidPort()
  @Test def invalidHost(): Unit = HttpTestBase.invalidHost()
  @Test def httpPostMethod(): Unit = HttpTestBase.httpPostMethod()
  @Test def httpGetMethod(): Unit = HttpTestBase.httpGetMethod()
  @Test def httpHeadMethod(): Unit = HttpTestBase.httpHeadMethod()
  @Test def httpConnectMethod(): Unit = HttpTestBase.httpConnectMethod()
  @Test def httpGetRequestMethod(): Unit = HttpTestBase.httpGetRequestMethod()
  @Test def httpPostRequestMethod(): Unit = HttpTestBase.httpPostRequestMethod()
  @Test def httpPutRequestMethod(): Unit = HttpTestBase.httpPutRequestMethod()
  @Test def httpDeleteRequestMethod(): Unit = HttpTestBase.httpDeleteRequestMethod()
  @Test def httpHeadRequestMethod(): Unit = HttpTestBase.httpHeadRequestMethod()
  @Test def httpTraceRequestMethod(): Unit = HttpTestBase.httpTraceRequestMethod()
  @Test def httpConnectRequestMethod(): Unit = HttpTestBase.httpConnectRequestMethod()
  @Test def httpOptionsRequestMethod(): Unit = HttpTestBase.httpOptionsRequestMethod()
  @Test def httpPatchRequestMethod(): Unit = HttpTestBase.httpPatchRequestMethod()
  @Test def sendFileNotFound(): Unit = HttpTestBase.sendFileNotFound()
  @Test def sendFileNotFoundWith404Page(): Unit = HttpTestBase.sendFileNotFoundWith404Page()
  @Test def sendFileNotFoundWith404PageAndHandler(): Unit = HttpTestBase.sendFileNotFoundWith404PageAndHandler()
  @Test def formUploadAttributes(): Unit = HttpTestBase.formUploadAttributes()
  @Test def formUploadAttributes2(): Unit = HttpTestBase.formUploadAttributes2()
  @Test def formUploadFile(): Unit = HttpTestBase.formUploadFile()
  @Test def sendFileMultipleOverrideHeaders(): Unit = HttpTestBase.sendFileMultipleOverrideHeaders()
  @Test def headerOverridesPossible(): Unit = HttpTestBase.headerOverridesPossible()
  @Test def continue100Default(): Unit = HttpTestBase.continue100Default()
  @Test def continue100Handled(): Unit = HttpTestBase.continue100Handled()

  @Test def sendFile(): Unit = {
    val (file, content) = generateRandomContentFile("test-send-file.html", 10000)
    HttpTestBase.checkServer(vertx.createHttpServer(), _.response().sendFile(file.getAbsolutePath)) { c =>
      c.getNow("some-uri", { res =>
        assertEquals(200, res.statusCode())
        assertTrue(res.headers().entryExists("content-type", _ == "text/html"))
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
    var sendComplete = false
    HttpTestBase.checkServer(vertx.createHttpServer(), _.response().sendFile(file.getAbsolutePath, { res =>
      sendComplete = true
    } )) { c =>
      c.getNow("some-uri", { res =>
        assertEquals(200, res.statusCode())
        assertTrue(res.headers().entryExists("content-type", _ == "text/html"))
        res.bodyHandler { buff =>
          assertEquals(content, buff.toString())
          file.delete()
          assertTrue(sendComplete)
          testComplete()
        }
      })
    }
  }

  @Test def sendFileOverrideHeaders(): Unit = {
    val (file, content) = generateRandomContentFile("test-send-file.html", 10000)
    HttpTestBase.checkServer(vertx.createHttpServer(),
      _.response().putHeader("Content-Type", "wibble").sendFile(file.getAbsolutePath)
    ) { c =>
      c.getNow("some-uri", { res =>
        assertEquals(200, res.statusCode())
        assertTrue(res.headers().entryExists("content-type", _ == "wibble"))
        res.bodyHandler { buff =>
          assertEquals(content, buff.toString())
          file.delete()
          testComplete()
        }
      })
    }
  }

}
