package org.vertx.scala.tests.core.http

import org.vertx.scala.testtools.TestVerticle
import org.junit.Test
import org.vertx.testtools.VertxAssert._
import org.vertx.scala.core.Vertx
import org.vertx.scala.core.AsyncResult
import org.vertx.scala.core.http.HttpServer
import org.vertx.scala.platform.Verticle

class ScalaHttpTest extends TestVerticle {
  println("init scala http test - " + this.isInstanceOf[Verticle])

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
  def testCreateServer() {
    println("testCreateServer start")
    //val vertx = Vertx(this.vertx)
    vertx.createHttpServer.requestHandler({ req =>
      println("got a request")
      req.response.end(html)
    }).listen(testPort, { ar: AsyncResult[HttpServer] =>
      if (ar.succeeded()) {
        println("create http client")
        vertx.createHttpClient.setPort(testPort).exceptionHandler({ ex =>
          fail("Should not get an exception, but got " + ex.toString())
        }).get("/", { resp =>
          println("got a resonse")
          resp.bodyHandler({ buf =>
            assertEquals(html, buf.toString)
            testComplete()
          })
        }).end()
      } else {
        fail("listening did not succeed: " + ar.cause().getMessage())
      }
    })

    println("done creating server and client")
  }
}