/*
 * Copyright 2011-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.vertx.scala.tests.http

import org.vertx.java.core.http.{ HttpServerRequest => JHttpServerRequest, HttpServerResponse, HttpServerFileUpload, HttpVersion }
import org.vertx.java.core.{ MultiMap => JMultiMap, Handler }
import org.vertx.java.core.impl.CaseInsensitiveMultiMap
import org.vertx.java.core.buffer.Buffer
import java.net.{ URI, InetSocketAddress }
import javax.security.cert.X509Certificate
import org.vertx.java.core.net.NetSocket
import java.util
import org.junit.Test
import org.vertx.scala.core.http.HttpServerRequest
import org.vertx.scala.core.http.HttpClient
import org.vertx.scala.core.http.HttpServer
import org.vertx.scala.VertxConverters._
import org.vertx.testtools.TestVerticle
import org.vertx.testtools.VertxAssert._

/**
 * @author Ranie Jade Ramiso
 * @author <a href="http://www.campudus.com/">Joern Bernhardt</a>
 */
class HttpServerRequestTest extends TestVerticle {

  private def createHttpServer(handler: HttpServerRequest => Unit) = {
    vertx.createHttpServer.requestHandler(new Handler[JHttpServerRequest]() {
      def handle(event: JHttpServerRequest) = handler(HttpServerRequest(event))
    }).listen(8080)
  }

  @Test
  def httpHeaderShouldExistTest() {
    createHttpServer { internalRequest =>
      // add some headers to the internal multimap
      internalRequest.headers.add("header1", "value1")
      internalRequest.headers.add("header2", "value2")

      val request = new HttpServerRequest(internalRequest)

      val headers = request.headers

      assertTrue(headers.contains("header1"))
      assertEquals("value1", headers.get("header1"))
      assertTrue(headers.contains("header2"))
      assertEquals("value2", headers.get("header2"))
      assertEquals(1, headers.getAll("header2").size())
      testComplete()
    }
  }

  @Test
  def httpParamShouldExistTest() {
    createHttpServer { internalRequest =>

      // add some headers to the internal multimap
      internalRequest.params.add("param1", "value1")
      internalRequest.params.add("param2", "value2")

      val request = new HttpServerRequest(internalRequest)

      val headers = request.params

      assertTrue(headers.contains("param1"))
      assertEquals("value1", headers.get("param1"))
      assertTrue(headers.contains("param2"))
      assertEquals("value2", headers.get("param2"))
      assertEquals(1, headers.getAll("param2").size())
    }
  }
}
