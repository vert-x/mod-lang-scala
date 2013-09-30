package org.vertx.scala.tests.core.net

import org.vertx.scala.testtools.TestVerticle
import org.junit.Test
import org.vertx.testtools.VertxAssert._
import org.vertx.java.core.AsyncResult
import org.vertx.scala.core.net.NetSocket
import org.vertx.scala.core.net.NetClient
import org.vertx.scala.core.net.NetServer
import org.vertx.scala.core.buffer._
import org.vertx.scala.tests.util.TestUtils._

class NetTest extends TestVerticle {

  val testPort = 8080

  @Test
  def createNetServer() {
    vertx.createNetServer.connectHandler(regularConnectHandler).listen(testPort, checkServer())
  }

  @Test
  def sslNetServer(): Unit = {
    val server = vertx.createNetServer.setSSL(true)

    server.setKeyStorePath("./src/test/keystores/server-keystore.jks").setKeyStorePassword("wibble")
    server.setTrustStorePath("./src/test/keystores/server-truststore.jks").setTrustStorePassword("wibble")

    server.connectHandler(regularConnectHandler).listen(testPort, { ar: AsyncResult[NetServer] =>
      (if (ar.succeeded()) {
        val c = vertx.createNetClient
        c.setSSL(true)
        c.setKeyStorePath("./src/test/keystores/client-keystore.jks").setKeyStorePassword("wibble")
        c.setTrustStorePath("./src/test/keystores/client-truststore.jks").setTrustStorePassword("wibble")
        c.connect(testPort, correctBodyHandler(testComplete))
      } else {
        fail("listening did not succeed: " + ar.cause().getMessage())
      }): Unit
    })
  }

  @Test
  def invalidHost(): Unit = {
    vertx.createNetServer.connectHandler({ r => }) listen (testPort, "iqwjdoqiwjdoiqwdiojwd", completeWithArFailed)
  }

  @Test
  def invalidPort(): Unit = {
    vertx.createNetServer.connectHandler({ r => }).listen(1128371831, completeWithArFailed[NetServer])
  }

  private def checkServer() = { ar: AsyncResult[NetServer] =>
    (if (ar.succeeded()) {
      vertx.createNetClient.connect(testPort, correctBodyHandler(testComplete))
    } else {
      fail("listening did not succeed: " + ar.cause().getMessage())
    }): Unit
  }

  private def correctBodyHandler(fn: () => Unit) = { resp: AsyncResult[NetSocket] =>
    if (resp.succeeded()) {
      resp.result().dataHandler({ buf =>
        assertEquals("hello-World", buf.toString)
        fn()
      }): Unit
    } else {
      fail("listening did not succeed: " + resp.cause().getMessage())
    }
  }

  private def regularConnectHandler: NetSocket => Unit = { ws =>
    ws.write(Buffer("hello-World"))
  }
}