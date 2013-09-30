package org.vertx.scala.tests.core.streams

import org.vertx.scala.testtools.TestVerticle
import org.junit.Test
import org.vertx.testtools.VertxAssert._
import org.vertx.scala.core.streams.Pump
import org.vertx.scala.core.http.HttpServerRequest
import org.vertx.scala.core.AsyncResult
import org.vertx.scala.core.http.HttpServer
import org.vertx.scala.core.http.HttpClient
import org.vertx.scala.core.http.HttpClientResponse
import org.vertx.scala.core.http.HttpClientRequest
import org.vertx.scala.core.buffer._
import org.vertx.scala.core.buffer.BufferTypes._
import org.vertx.java.core.buffer.{ Buffer => JBuffer }

class PumpTest extends TestVerticle {

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
  def pumpingHttp(): Unit = {
    vertx.createHttpServer.requestHandler(regularRequestHandler).listen(testPort, checkServer({ c =>
      val chunk = new Buffer(new JBuffer("hello pump"))
      val req = c.post("/", { resp =>
        assertEquals(200, resp.statusCode)
        val buffer = new Buffer(new JBuffer())
        resp.dataHandler { buff =>
          buffer.append(new Buffer(buff))
        }
        resp.endHandler {
          assertEquals(chunk.internal.length(), buffer.internal.length())
          assertEquals(chunk, buffer)
        }
      })
      req.setChunked(true)
      req.write(chunk)
      req.end()
    }))
  }

  @Test
  def pumpingFile(): Unit = fail("not implemented")

  @Test
  def pumpStopAndResume(): Unit = fail("not implemented")

  private def regularRequestHandler: HttpServerRequest => Unit = { req =>
    req.response.setChunked(true)
    val pump = Pump.createPump(req, req.response)
    pump.start
  }

  private def checkServer(clientFn: HttpClient => Unit) = { ar: AsyncResult[HttpServer] =>
    if (ar.succeeded()) {
      val httpClient = vertx.createHttpClient.setPort(testPort)
      clientFn(httpClient)
    } else {
      fail("listening did not succeed: " + ar.cause().getMessage())
    }
  }

  private def correctHandler(fn: () => Unit) = { resp: HttpClientResponse =>
    resp.bodyHandler({ buf =>
      assertEquals(html, buf.toString)
      fn()
    }): Unit
  }

}