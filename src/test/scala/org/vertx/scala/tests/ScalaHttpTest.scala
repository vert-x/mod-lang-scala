
package org.vertx.scala.tests

import org.junit.Test
import org.junit.Assert._
import org.vertx.testtools.VertxAssert.assertEquals;
import org.vertx.testtools.TestVerticle
import org.vertx.scala.core.Vertx
import org.vertx.java.core.buffer.Buffer
import org.vertx.java.core.http.HttpClient
import org.vertx.java.core.http.HttpClientResponse
import org.vertx.java.core.Handler

import org.vertx.testtools.VertxAssert.testComplete;

class ScalaHttpTest extends TestVerticle {

  @Test def testClientDefaults() {

    val html = "<html><body><h1>Hello from vert.x!</h1></body></html>"
    val port = 8080

    Vertx(vertx).createHttpServer().requestHandler(
      { req =>
        req.response.end(html);
      }).listen(port, {
        server =>
          val client = Vertx(vertx).createHttpClient().setPort(port)

          client.getNow("/", {
            resp =>
              resp.bodyHandler({
                data =>
                  {
                    assertEquals(html, data.toString());
                    testComplete();
                  }
              });
          });

      });

  }

}