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
    val port = 8080
    def htmlFor(str: String) = s"<html><body><h1>Hello from ${str}</h1></body></html>"

    val rm = new RouteMatcher
    rm.get("/get") { r => r.response.end(htmlFor("/get")) }

    vertx.newHttpServer(rm).listen(port, { ar: AsyncResult[HttpServer] =>
      assertTrue(ar.succeeded())
      val client = vertx.newHttpClient.setPort(port)
      client.getNow("/get") { h =>
        h.bodyHandler { data =>
          assertEquals(htmlFor("/get"), data.toString)
          testComplete()
        }
      }
    })
  }
}