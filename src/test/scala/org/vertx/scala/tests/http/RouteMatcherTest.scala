package org.vertx.scala.tests.http

import org.vertx.testtools.TestVerticle
import org.vertx.testtools.VertxAssert._
import org.junit.Test
import org.vertx.scala.core.Vertx
import org.vertx.java.core.AsyncResult
import org.vertx.java.core.http.HttpServer
import org.vertx.scala.core.http.RouteMatcher
import java.util.concurrent.atomic.AtomicInteger

/**
 * @author <a href="http://www.campudus.com/">Joern Bernhardt</a>
 */
class RouteMatcherTest extends TestVerticle {

  @Test
  def routeMatcherUsableWithHttpServer {
    val port1 = 8080
    val port2 = 8081
    val checks = new AtomicInteger(2)
    def htmlFor(str: String) = s"<html><body><h1>Hello from ${str}</h1></body></html>"
    def getNowCheck(port: Int, url: String) = {
      val client = vertx.newHttpClient.setPort(port)
      client.getNow("/get") { h =>
        h.bodyHandler { data =>
          assertEquals(htmlFor("/get"), data.toString)
          if (checks.decrementAndGet == 0) {
            testComplete()
          }
        }
      }
    }

    val rm = new RouteMatcher
    rm.get("/get") { r => r.response.end(htmlFor("/get")) }

    vertx.newHttpServer(rm).listen(port1, { ar: AsyncResult[HttpServer] =>
      assertTrue(ar.succeeded())
      getNowCheck(port1, "/get")
    })

    vertx.newHttpServer.requestHandler(rm).listen(port2, { ar: AsyncResult[HttpServer] =>
      assertTrue(ar.succeeded())
      getNowCheck(port2, "/get")
    })
  }
}