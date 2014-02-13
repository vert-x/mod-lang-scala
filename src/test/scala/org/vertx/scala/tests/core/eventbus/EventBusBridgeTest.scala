/*
 * Copyright 2013 the original author or authors.
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
package org.vertx.scala.tests.core.eventbus

import org.vertx.scala.testtools.TestVerticle
import org.vertx.scala.core.json.Json
import org.vertx.testtools.VertxAssert._
import org.junit.Test

class EventBusBridgeTest extends TestVerticle {

  @Test
  def letAllThroughBridge(): Unit = {
    val server = vertx.createHttpServer()

    val permitted = Json.arr()
    permitted.add(Json.obj()) // Let everything through

    val sockJSServer = vertx.createSockJSServer(server)
    sockJSServer.bridge(Json.obj("prefix" -> "/eventbus"), permitted, permitted)

    server.listen(8080, result => {
      assertThread()
      def client = vertx.createHttpClient().setPort(8080)
      // We use rawwebsocket transport
      client.connectWebsocket("/eventbus/websocket", websocket => {
        assertThread()
        // Register
        val msg = Json.obj("type" -> "register", "address" -> "someaddress")
        websocket.writeTextFrame(msg.encode())

        // Send
        val msg2 = Json.obj("type" -> "send", "address" -> "someaddress", "body" -> "hello world")
        websocket.writeTextFrame(msg2.encode())

        websocket.dataHandler { buffer =>
            assertThread()
            val msg = buffer.toString()
            val received = Json.fromObjectString(msg)
            assert(received.getString("body").equals("hello world"))
            testComplete()
        }
      })
    })
  }
}
