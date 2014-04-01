package org.vertx.scala.tests.core.http

import org.junit.Test
import org.vertx.scala.tests.util.TestUtils._
import org.vertx.testtools.VertxAssert._
import org.vertx.scala.testtools.TestVerticle
import org.vertx.scala.core.Vertx

final class HttpCompressionTest extends TestVerticle with HttpTestBase {

  override def compression: Boolean = true
  override def getVertx: Vertx = vertx

  @Test override def sendFile(): Unit = {
    val (file, content) = generateRandomContentFile("test-send-file.html", 10000)
    checkServer(vertx.createHttpServer(), _.response().sendFile(file.getAbsolutePath)) { c =>
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

  @Test override def sendFileWithHandler(): Unit = {
    val (file, content) = generateRandomContentFile("test-send-file.html", 10000)
    var sendComplete = false
    checkServer(vertx.createHttpServer(), _.response().sendFile(file.getAbsolutePath, { res =>
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

  @Test override def sendFileOverrideHeaders(): Unit = {
    val (file, content) = generateRandomContentFile("test-send-file.html", 10000)
    checkServer(vertx.createHttpServer(),
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
