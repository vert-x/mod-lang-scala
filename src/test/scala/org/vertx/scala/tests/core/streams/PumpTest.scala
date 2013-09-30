package org.vertx.scala.tests.core.streams

import org.junit.Test
import org.junit.runner.RunWith
import org.vertx.scala.core.AsyncResult
import org.vertx.scala.core.buffer.{ Buffer, BufferElem }
import org.vertx.scala.core.http.{ HttpClient, HttpClientResponse, HttpServer, HttpServerRequest }
import org.vertx.scala.core.streams.Pump
import org.vertx.scala.testtools.{ ScalaClassRunner, TestVerticle }
import org.vertx.testtools.VertxAssert._

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
      val chunk = Buffer("hello pump")
      val req = c.post("/", { resp =>
        assertEquals(200, resp.statusCode)
        val buffer = Buffer()
        resp.dataHandler { buff =>
          buffer.append(buff)
        }
        resp.endHandler {
          assertEquals(chunk.length(), buffer.length())
          assertEquals(chunk, buffer)
          testComplete()
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
    req.bodyHandler({ buf => println("buf-body:" + buf) })
    req.dataHandler({ buf => println("buf:" + buf) })
    req.endHandler({ req.response.end })
    //    req.response.drainHandler(req.response.end)
    Pump.createPump(req, req.response).start
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