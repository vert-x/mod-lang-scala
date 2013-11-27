package org.vertx.scala.tests.core.datagram

import org.vertx.scala.testtools.TestVerticle
import org.junit.Test
import org.vertx.scala.core.datagram.{IPv4, DatagramSocket}
import org.vertx.scala.tests.util.TestUtils._
import org.vertx.testtools.VertxAssert._
import org.vertx.scala.core.AsyncResult
import java.net.{NetworkInterface, SocketException, InetSocketAddress}
import scala.concurrent.Promise
import scala.util.Success

/**
 * Datagram tests
 *
 * @author Galder Zamarreño
 */
class DatagramTest extends TestVerticle {

  var peer1: DatagramSocket = _
  var peer2: DatagramSocket = _

  @Test def sendReceive() {
    peer1 = vertx.createDatagramSocket()
    peer2 = vertx.createDatagramSocket()
    peer2.exceptionHandler(t => fail(s"Unexpected throwable $t"))
    peer2.listen("127.0.0.1", 1234, { h =>
      assertTrue(h.succeeded())
      val buffer = generateRandomBuffer(128)

      peer2.dataHandler { h =>
        assertEquals(buffer, h.data())
        testComplete()
      }

      peer2.send(buffer, "127.0.0.1", 1234, { h: AsyncResult[DatagramSocket] =>
        assertTrue(h.succeeded())
      })
    })
  }

  @Test def listenHostPort() {
    peer2 = vertx.createDatagramSocket()
    peer2.listen("127.0.0.1", 1234, { h =>
      assertTrue(h.succeeded())
      testComplete()
    })
  }

  @Test def listenPort() {
    peer2 = vertx.createDatagramSocket()
    // Due to overloading, type information needs to be provided
    peer2.listen(1234, { h: AsyncResult[DatagramSocket] =>
      assertTrue(h.succeeded())
      testComplete()
    })
  }

  @Test def listenInetSocketAddress() {
    peer2 = vertx.createDatagramSocket()
    peer2.listen(new InetSocketAddress("127.0.0.1", 1234), { h: AsyncResult[DatagramSocket] =>
      assertTrue(h.succeeded())
      testComplete()
    })
  }

  @Test def listenSamePortMultipleTimes() {
    peer1 = vertx.createDatagramSocket()
    peer2 = vertx.createDatagramSocket()
    peer2.listen(1234, { h: AsyncResult[DatagramSocket] =>
      assertTrue(h.succeeded())
      peer1.listen(1234, { h: AsyncResult[DatagramSocket] =>
          assertTrue(h.failed())
          testComplete()
      })
      ()
    })
  }

  @Test def echo() {
    peer1 = vertx.createDatagramSocket()
    peer2 = vertx.createDatagramSocket()
    peer1.exceptionHandler(h => fail("Failed"))
    peer2.exceptionHandler(h => fail("Failed"))

    peer2.listen("127.0.0.1", 1234, { h =>
      assertTrue(h.succeeded())
      val buffer = generateRandomBuffer(128)
      peer2.dataHandler { h =>
        assertEquals(new InetSocketAddress("127.0.0.1", 1235), h.sender())
        assertEquals(buffer, h.data())
        peer2.send(h.data(), "127.0.0.1", 1235, { h: AsyncResult[DatagramSocket] =>
          assertTrue(h.succeeded())
        })
      }

      peer1.listen("127.0.0.1", 1235, { h =>
        peer1.dataHandler { h =>
           assertEquals(buffer, h.data())
           assertEquals(new InetSocketAddress("127.0.0.1", 1234), h.sender())
           testComplete()
        }

        peer1.send(buffer, "127.0.0.1", 1234, { h: AsyncResult[DatagramSocket] =>
          assertTrue(h.succeeded())
        })
      })
    })
  }

  @Test def sendAfterCloseFails() {
    peer1 = vertx.createDatagramSocket()
    peer2 = vertx.createDatagramSocket()
    peer1.close { h =>
      peer1.send("Test", "127.0.0.1", 1234, { h: AsyncResult[DatagramSocket] =>
        assertTrue(h.failed())
        peer1 = null
        peer2.close { h =>
          peer2.send("Test", "127.0.0.1", 1234, { h: AsyncResult[DatagramSocket] =>
            assertTrue(h.failed())
            peer2 = null
            testComplete()
          })
        }
      })
    }
  }

  def testBroadcast() {
    peer1 = vertx.createDatagramSocket()
    peer2 = vertx.createDatagramSocket()
    peer2.exceptionHandler(h => fail("Failed"))

    peer1.setBroadcast(broadcast = true)
    peer2.setBroadcast(broadcast = true)

    peer2.listen(new InetSocketAddress(1234), { h: AsyncResult[DatagramSocket] =>
      assertTrue(h.succeeded())
      val buffer = generateRandomBuffer(128)
      peer2.dataHandler { h =>
        assertEquals(buffer, h.data())
        testComplete()
      }
      peer1.send(buffer, "255.255.255.255", 1234, { h: AsyncResult[DatagramSocket] =>
        assertTrue(h.succeeded())
      })
      ()
    })
  }

  @Test def broadcastFailsIfNotConfigured() {
    peer1 = vertx.createDatagramSocket()
    peer1.send("test", "255.255.255.255", 1234, { h: AsyncResult[DatagramSocket] =>
      assertTrue(h.failed())
      testComplete()
    })
  }

  @Test def configureAfterSendString() {
    peer1 = vertx.createDatagramSocket()
    peer1.send("test", "127.0.0.1", 1234, { h: AsyncResult[DatagramSocket] => })
    checkConfigure(peer1)
    peer1.close()
  }

  @Test def configureAfterSendStringWithEnc() {
    peer1 = vertx.createDatagramSocket()
    peer1.send("test", "UTF-8", "127.0.0.1", 1234, { h => })
    checkConfigure(peer1)
  }

  @Test def configureAfterSendBuffer() {
    peer1 = vertx.createDatagramSocket()
    peer1.send(generateRandomBuffer(64), "127.0.0.1", 1234, { h: AsyncResult[DatagramSocket] => })
    checkConfigure(peer1)
  }

  @Test def configureAfterListen() {
    peer1 = vertx.createDatagramSocket()
    peer1.listen("127.0.0.1", 1234, { h => })
    checkConfigure(peer1)
  }

  @Test def configureAfterListenWithInetSocketAddress() {
    peer1 = vertx.createDatagramSocket()
    peer1.listen(new InetSocketAddress("127.0.0.1", 1234), { h: AsyncResult[DatagramSocket] => })
    checkConfigure(peer1)
  }

  @Test def configure() {
    peer1 = vertx.createDatagramSocket(Some(IPv4))

    assertFalse(peer1.isBroadcast)
    peer1.setBroadcast(broadcast = true)
    assertTrue(peer1.isBroadcast)

    assertTrue(peer1.isMulticastLoopbackMode)
    peer1.setMulticastLoopbackMode(loopbackModeDisabled = false)
    assertFalse(peer1.isMulticastLoopbackMode)

    for (iface <- getNetworkInterface)
    yield {
      assertNull(peer1.getMulticastNetworkInterface)
      peer1.setMulticastNetworkInterface(iface.getName)
      assertEquals(iface.getName, peer1.getMulticastNetworkInterface)
    }

    peer1.setReceiveBufferSize(1024)
    peer1.setSendBufferSize(1024)

    assertFalse(peer1.isReuseAddress)
    peer1.setReuseAddress(reuse = true)
    assertTrue(peer1.isReuseAddress)

    assertNotSame(2, peer1.getMulticastTimeToLive)
    peer1.setMulticastTimeToLive(2)
    assertEquals(2, peer1.getMulticastTimeToLive)

    testComplete()
  }

  @Test def multicastJoinLeave() {
    val buffer = generateRandomBuffer(128)
    val groupAddress = "230.0.0.1"

    peer1 = vertx.createDatagramSocket()
    peer2 = vertx.createDatagramSocket(Some(IPv4))

    peer2.dataHandler(h => assertEquals(buffer, h.data()))
    peer2.listen("127.0.0.1", 1234, { h =>
      assertTrue(h.succeeded())
      peer2.listenMulticastGroup(groupAddress, { h =>
        assertTrue(h.succeeded())
        peer1.send(buffer, groupAddress, 1234, { h: AsyncResult[DatagramSocket] =>
          assertTrue(h.succeeded())
          // leave group
          peer2.unlistenMulticastGroup(groupAddress, { h =>
            assertTrue(h.succeeded())
            val received = Promise[Boolean]()
            val future = received.future
            peer2.dataHandler { h =>
              // Should not receive any more event as it left the group
              received.complete(Success(true))
            }
            peer1.send(buffer, groupAddress, 1234, { h: AsyncResult[DatagramSocket] =>
              // schedule a timer which will check in 1 second if we received
              // a message after the group was left before
              vertx.setTimer(1000, { h =>
                if (!future.isCompleted) {
                  // Should not have completed
                  testComplete()
                } else {
                  fail("Should not receive any more event after leaving the group")
                }
              })
              ()
            })
          })
          ()
        })
      })
    })
  }

  override def stop() {
    stopDatagramSocket(List(peer1, peer2))
  }

  private def stopDatagramSocket(sockets: List[DatagramSocket]) {
    sockets match {
      case Nil => super.stop() // Final stop
      case x::xs => // Stop each socket
        if (x != null) {
          x.close({ h =>
            stopDatagramSocket(xs)
          })
        } else {
          stopDatagramSocket(xs)
        }
        // Not tail recursive, but that's fine since list is small
    }
  }

  private def checkConfigure(endpoint: DatagramSocket) {
    expectIllegalStateException(endpoint.setBroadcast(broadcast = true))
    expectIllegalStateException(endpoint.setMulticastLoopbackMode(loopbackModeDisabled = true))

    expectIllegalStateException {
      endpoint.setMulticastNetworkInterface(NetworkInterface.getNetworkInterfaces.nextElement().getName)
    }

    expectIllegalStateException(endpoint.setReceiveBufferSize(1024))
    expectIllegalStateException(endpoint.setReuseAddress(reuse = true))
    expectIllegalStateException(endpoint.setSendBufferSize(1024))
    expectIllegalStateException(endpoint.setMulticastTimeToLive(2))
    expectIllegalStateException(endpoint.setTrafficClass(1))

    testComplete()
  }

  private def expectIllegalStateException(action: => Unit) {
    try {
      action
      fail("Expeced to throw IllegalStateException")
    } catch {
      case e: IllegalStateException => // expected
      case e: SocketException => // ignore
    }
  }

  private def getNetworkInterface: Option[NetworkInterface] = {
    val ifaces = NetworkInterface.getNetworkInterfaces
    while (ifaces.hasMoreElements) {
      val networkInterface = ifaces.nextElement()
      try {
        if (networkInterface.supportsMulticast()) {
          val addresses = networkInterface.getInetAddresses
          if (addresses.hasMoreElements)
            Some(networkInterface)
        }
      } catch {
        case e: SocketException => // ignore
      }
    }
    None
  }

}
