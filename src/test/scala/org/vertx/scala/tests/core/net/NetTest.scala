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
    checkSslServerAndClient(false, regularConnectHandler, correctBodyHandler(testComplete))
  }

  @Test
  def sslUpgrade(): Unit = {
    checkSslServerAndClient(true, { ns =>
      assertFalse(ns.isSsl())
      ns.ssl({
        ns.write("ssl-test")
      })
    }, checkConnectedNetSocketHandler { ns =>
      assertFalse(ns.isSsl())
      ns.ssl({
        assertTrue(ns.isSsl())
        ns.dataHandler { buf =>
          assertTrue(ns.isSsl())
          assertEquals("ssl-test", buf.getString(0, buf.length()))
          testComplete()
        }
      })
    })
  }

  private def checkSslServerAndClient(upgradeToSsl: Boolean, serverConnectHandler: NetSocket => Unit, clientConnectHandler: AsyncResult[NetSocket] => Unit): Unit = {
    val server = vertx.createNetServer.setSSL(!upgradeToSsl)

    server.setKeyStorePath("./src/test/keystores/server-keystore.jks").setKeyStorePassword("wibble")
    server.setTrustStorePath("./src/test/keystores/server-truststore.jks").setTrustStorePassword("wibble")

    server.connectHandler(serverConnectHandler).listen(testPort, { ar: AsyncResult[NetServer] =>
      (if (ar.succeeded()) {
        val c = vertx.createNetClient
        c.setSSL(!upgradeToSsl)
        c.setKeyStorePath("./src/test/keystores/client-keystore.jks").setKeyStorePassword("wibble")
        c.setTrustStorePath("./src/test/keystores/client-truststore.jks").setTrustStorePassword("wibble")
        c.connect(testPort, clientConnectHandler)
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

  private def checkConnectedNetSocketHandler(checks: NetSocket => Unit) = { resp: AsyncResult[NetSocket] =>
    if (resp.succeeded()) {
      checks(resp.result())
    } else {
      fail("listening did not succeed: " + resp.cause().getMessage())
    }
  }

  private def correctBodyHandler(fn: () => Unit) = checkConnectedNetSocketHandler { ns =>
    ns.dataHandler({ buf =>
      assertEquals("hello-World", buf.toString)
      fn()
    }): Unit
  }

  private def regularConnectHandler: NetSocket => Unit = { ws =>
    ws.write(Buffer("hello-World"))
  }
}