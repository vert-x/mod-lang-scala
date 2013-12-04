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

  var peer1: Option[DatagramSocket] = None
  var peer2: Option[DatagramSocket] = None

  @Test def sendReceive() {
    peer2 = Some(vertx.createDatagramSocket())
    for (p <- peer2) yield {
      p.exceptionHandler(t => fail(s"Unexpected throwable $t"))
      p.listen("127.0.0.1", 1234, { h =>
        assertTrue(h.succeeded())
        val buffer = generateRandomBuffer(128)

        p.dataHandler { h =>
          assertEquals(buffer, h.data())
          testComplete()
        }

        p.send(buffer, "127.0.0.1", 1234, { h: AsyncResult[DatagramSocket] =>
          assertTrue(h.succeeded())
        })
      })
    }
  }

  @Test def listenHostPort() {
    peer2 = Some(vertx.createDatagramSocket())
    for (p <- peer2) yield {
      p.listen("127.0.0.1", 1234, { h =>
        assertTrue(h.succeeded())
        testComplete()
      })
    }
  }

  @Test def listenPort() {
    peer2 = Some(vertx.createDatagramSocket())
    for (p <- peer2) yield {
      // Due to overloading, type information needs to be provided
      p.listen(1234, { h: AsyncResult[DatagramSocket] =>
        assertTrue(h.succeeded())
        testComplete()
      })
    }
  }

  @Test def listenInetSocketAddress() {
    peer2 = Some(vertx.createDatagramSocket())
    for (p <- peer2) yield {
      p.listen(new InetSocketAddress("127.0.0.1", 1234), { h: AsyncResult[DatagramSocket] =>
        assertTrue(h.succeeded())
        testComplete()
      })
    }
  }

  @Test def listenSamePortMultipleTimes() {
    peer1 = Some(vertx.createDatagramSocket())
    peer2 = Some(vertx.createDatagramSocket())
    for {
      p1 <- peer1
      p2 <- peer2
    } yield {
      p2.listen(1234, { h: AsyncResult[DatagramSocket] =>
        assertTrue(h.succeeded())
        p1.listen(1234, { h: AsyncResult[DatagramSocket] =>
          assertTrue(h.failed())
          testComplete()
        })
        ()
      })
    }
  }

  @Test def echo() {
    peer1 = Some(vertx.createDatagramSocket())
    peer2 = Some(vertx.createDatagramSocket())
    for {
      p1 <- peer1
      p2 <- peer2
    } yield {
      p1.exceptionHandler(h => fail("Failed"))
      p2.exceptionHandler(h => fail("Failed"))

      p2.listen("127.0.0.1", 1234, { h =>
        assertTrue(h.succeeded())
        val buffer = generateRandomBuffer(128)
        p2.dataHandler { h =>
          assertEquals(new InetSocketAddress("127.0.0.1", 1235), h.sender())
          assertEquals(buffer, h.data())
          p2.send(h.data(), "127.0.0.1", 1235, { h: AsyncResult[DatagramSocket] =>
            assertTrue(h.succeeded())
          })
        }

        p1.listen("127.0.0.1", 1235, { h =>
          p1.dataHandler { h =>
            assertEquals(buffer, h.data())
            assertEquals(new InetSocketAddress("127.0.0.1", 1234), h.sender())
            testComplete()
          }

          p1.send(buffer, "127.0.0.1", 1234, { h: AsyncResult[DatagramSocket] =>
            assertTrue(h.succeeded())
          })
        })
      })
    }
  }

  @Test def sendAfterCloseFails() {
    peer1 = Some(vertx.createDatagramSocket())
    peer2 = Some(vertx.createDatagramSocket())
    for {
      p1 <- peer1
      p2 <- peer2
    } yield {
      p1.close { h =>
        p1.send("Test", "127.0.0.1", 1234, { h: AsyncResult[DatagramSocket] =>
          assertTrue(h.failed())
          // peer1 = null
          p2.close { h =>
            p2.send("Test", "127.0.0.1", 1234, { h: AsyncResult[DatagramSocket] =>
              assertTrue(h.failed())
              // peer2 = null
              testComplete()
            })
          }
        })
      }
    }
  }

  def testBroadcast() {
    peer1 = Some(vertx.createDatagramSocket())
    peer2 = Some(vertx.createDatagramSocket())
    for {
      p1 <- peer1
      p2 <- peer2
    } yield {
      p2.exceptionHandler(h => fail("Failed"))

      p1.setBroadcast(broadcast = true)
      p2.setBroadcast(broadcast = true)

      p2.listen(new InetSocketAddress(1234), { h: AsyncResult[DatagramSocket] =>
        assertTrue(h.succeeded())
        val buffer = generateRandomBuffer(128)
        p2.dataHandler { h =>
          assertEquals(buffer, h.data())
          testComplete()
        }
        p1.send(buffer, "255.255.255.255", 1234, { h: AsyncResult[DatagramSocket] =>
          assertTrue(h.succeeded())
        })
        ()
      })
    }
  }

  @Test def broadcastFailsIfNotConfigured() {
    peer1 = Some(vertx.createDatagramSocket())
    for (p <- peer1) yield {
      p.send("test", "255.255.255.255", 1234, { h: AsyncResult[DatagramSocket] =>
        assertTrue(h.failed())
        testComplete()
      })
    }
  }

  @Test def configureAfterSendString() {
    peer1 = Some(vertx.createDatagramSocket())
    for (p <- peer1) yield {
      p.send("test", "127.0.0.1", 1234, { h: AsyncResult[DatagramSocket] => })
      checkConfigure(p)
      p.close()
    }
  }

  @Test def configureAfterSendStringWithEnc() {
    peer1 = Some(vertx.createDatagramSocket())
    for (p <- peer1) yield {
      p.send("test", "UTF-8", "127.0.0.1", 1234, { h => })
      checkConfigure(p)
    }
  }

  @Test def configureAfterSendBuffer() {
    peer1 = Some(vertx.createDatagramSocket())
    for (p <- peer1) yield {
      p.send(generateRandomBuffer(64), "127.0.0.1", 1234, { h: AsyncResult[DatagramSocket] => })
      checkConfigure(p)
    }
  }

  @Test def configureAfterListen() {
    peer1 = Some(vertx.createDatagramSocket())
    for (p <- peer1) yield {
      p.listen("127.0.0.1", 1234, { h => })
      checkConfigure(p)
    }
  }

  @Test def configureAfterListenWithInetSocketAddress() {
    peer1 = Some(vertx.createDatagramSocket())
    for (p <- peer1) yield {
      p.listen(new InetSocketAddress("127.0.0.1", 1234), { h: AsyncResult[DatagramSocket] => })
      checkConfigure(p)
    }
  }

  @Test def configure() {
    peer1 = Some(vertx.createDatagramSocket(Some(IPv4)))

    for (p <- peer1) yield {
      assertFalse(p.isBroadcast)
      p.setBroadcast(broadcast = true)
      assertTrue(p.isBroadcast)

      assertTrue(p.isMulticastLoopbackMode)
      p.setMulticastLoopbackMode(loopbackModeDisabled = false)
      assertFalse(p.isMulticastLoopbackMode)

      for (iface <- getNetworkInterface)
      yield {
        assertNull(p.getMulticastNetworkInterface)
        p.setMulticastNetworkInterface(iface.getName)
        assertEquals(iface.getName, p.getMulticastNetworkInterface)
      }

      p.setReceiveBufferSize(1024)
      p.setSendBufferSize(1024)

      assertFalse(p.isReuseAddress)
      p.setReuseAddress(reuse = true)
      assertTrue(p.isReuseAddress)

      assertNotSame(2, p.getMulticastTimeToLive)
      p.setMulticastTimeToLive(2)
      assertEquals(2, p.getMulticastTimeToLive)

      testComplete()
    }
  }

  @Test def multicastJoinLeave() {
    val buffer = generateRandomBuffer(128)
    val groupAddress = "230.0.0.1"

    peer1 = Some(vertx.createDatagramSocket())
    peer2 = Some(vertx.createDatagramSocket(Some(IPv4)))

    for {
      p1 <- peer1
      p2 <- peer2
    } yield {
      p2.dataHandler(h => assertEquals(buffer, h.data()))
      p2.listen("127.0.0.1", 1234, { h =>
        assertTrue(h.succeeded())
        p2.listenMulticastGroup(groupAddress, { h =>
          assertTrue(h.succeeded())
          p1.send(buffer, groupAddress, 1234, { h: AsyncResult[DatagramSocket] =>
            assertTrue(h.succeeded())
            // leave group
            p2.unlistenMulticastGroup(groupAddress, { h =>
              assertTrue(h.succeeded())
              val received = Promise[Boolean]()
              val future = received.future
              p2.dataHandler { h =>
              // Should not receive any more event as it left the group
                received.complete(Success(true))
              }
              p1.send(buffer, groupAddress, 1234, { h: AsyncResult[DatagramSocket] =>
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
  }

  override def stop() {
    stopDatagramSocket(List(peer1, peer2))
  }

  private def stopDatagramSocket(sockets: List[Option[DatagramSocket]]) {
    sockets.foreach(_.map(_.close()))
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
