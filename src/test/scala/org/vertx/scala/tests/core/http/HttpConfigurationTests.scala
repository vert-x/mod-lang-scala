package org.vertx.scala.tests.core.http

import org.junit.Test
import org.vertx.scala.testtools.TestVerticle
import org.vertx.testtools.VertxAssert._

class HttpConfigurationTests extends TestVerticle {

  @Test def testSetGetMaxWebSocketFrameSizeServer() {
    val server = vertx.createHttpServer()
    val size = 61231763
    assertEquals(server, server.setMaxWebSocketFrameSize(size))
    assertEquals(size, server.getMaxWebSocketFrameSize)
    testComplete()
  }

  @Test def testSetGetMaxWebSocketFrameSizeClient() {
    val client = vertx.createHttpClient()
    val size = 61231763
    assertEquals(client, client.setMaxWebSocketFrameSize(size))
    assertEquals(size, client.getMaxWebSocketFrameSize)
    testComplete()
  }

}