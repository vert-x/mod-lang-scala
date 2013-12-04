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
package org.vertx.scala.core

import java.net.InetSocketAddress
import org.vertx.java.core.Context
import org.vertx.java.core.{ Vertx => JVertx }
import org.vertx.scala.core.FunctionConverters._
import org.vertx.scala.core.datagram.InternetProtocolFamily._
import org.vertx.scala.core.datagram.{DatagramSocket, InternetProtocolFamily}
import org.vertx.scala.core.dns.DnsClient
import org.vertx.scala.core.eventbus.EventBus
import org.vertx.scala.core.file.FileSystem
import org.vertx.scala.core.http.{ HttpClient, HttpServer }
import org.vertx.scala.core.net.{ NetServer, NetClient }
import org.vertx.scala.core.shareddata.SharedData
import org.vertx.scala.core.sockjs.SockJSServer

/**
 * The control centre of the Vert.x Core API.<p>
 * You should normally only use a single instance of this class throughout
 * your application. If you are running in the Vert.x container an instance
 * will be provided to you.<p>
 * If you are using Vert.x embedded, you can create an instance using one of
 * the static core package `newVertx` methods.<p>
 * This class acts as a factory for TCP/SSL and HTTP/HTTPS servers and clients,
 * SockJS servers, and provides an instance of the event bus, file system and
 * shared data classes, as well as methods for setting and cancelling timers.<p>
 * Instances of this class are thread-safe.<p>
 *
 * @author <a href="http://tfox.org">Tim Fox</a>
 * @author swilliams
 * @author <a href="http://www.campudus.com/">Joern Bernhardt</a>
 * @author Galder Zamarreño
 */
final class Vertx private[scala] (val asJava: JVertx) {
  // Vertx class is really a singleton, so it's not hugely important for it
  // to extend AnyVal. The cached vals to the Scala components are required
  // in order to avoid leaking Java interfaces and avoid creating wrappers

  /**
   * Create a TCP/SSL server.
   */
  def createNetServer(): NetServer = NetServer(asJava.createNetServer())

  /**
   * Create a TCP/SSL client.
   */
  def createNetClient(): NetClient = NetClient(asJava.createNetClient())

  /**
   * Create an HTTP/HTTPS server.
   */
  def createHttpServer(): HttpServer = HttpServer(asJava.createHttpServer())

  /**
   * Create a HTTP/HTTPS client.
   */
  def createHttpClient(): HttpClient = HttpClient(asJava.createHttpClient())

  /**
   * Create a new [[org.vertx.scala.core.datagram.DatagramSocket]].
   *
   * @param family  optional user provided [[org.vertx.scala.core.datagram.InternetProtocolFamily]]
   *                to use for multicast. If [[scala.None]], it's up to the
   *                operation system to detect it's default.
   *@return socket the created [[org.vertx.scala.core.datagram.DatagramSocket]].
   */
  def createDatagramSocket(family: Option[InternetProtocolFamily] = None): DatagramSocket =
    DatagramSocket(asJava.createDatagramSocket(toJavaIpFamily(family).getOrElse(null)))

  /**
   * Create a SockJS server that wraps an HTTP server.
   */
  def createSockJSServer(httpServer: HttpServer): SockJSServer = SockJSServer(asJava.createSockJSServer(httpServer.asJava))

  /**
   * The File system object.
   */
  val fileSystem: FileSystem = new FileSystem(asJava.fileSystem())

  /**
   * The event bus.
   */
  val eventBus: EventBus = new EventBus(asJava.eventBus())

  /**
   * Return the {@link DnsClient}
   */
  def createDnsClient(dnsServers: InetSocketAddress*): DnsClient = DnsClient(asJava.createDnsClient(dnsServers: _*))

  /**
   * The shared data object.
   */
  val sharedData: SharedData = new SharedData(asJava.sharedData())

  /**
   * Set a one-shot timer to fire after {@code delay} milliseconds, at which point {@code handler} will be called with
   * the id of the timer.
   * @return The unique ID of the timer.
   */
  def setTimer(delay: Long, handler: Long => Unit): Long = {
    asJava.setTimer(delay, fnToHandler(handler.compose {
      l: java.lang.Long => Long.box(l)
    }))
  }

  /**
   * Set a periodic timer to fire every {@code delay} milliseconds, at which point {@code handler} will be called with
   * the id of the timer.
   * @return the unique ID of the timer.
   */
  def setPeriodic(delay: Long, handler: Long => Unit): Long = {
    asJava.setPeriodic(delay, fnToHandler(handler.compose {
      l: java.lang.Long => Long.box(l)
    }))
  }

  /**
   * Cancel the timer with the specified {@code id}. Returns {@code} true if the timer was successfully cancelled, or
   * {@code false} if the timer does not exist.
   */
  def cancelTimer(id: Long): Boolean = asJava.cancelTimer(id)

  /**
   * @return The current context.
   */
  def currentContext(): Context = asJava.currentContext()

  /**
   * Put the handler on the event queue for the current loop (or worker context) so it will be run asynchronously ASAP after this event has
   * been processed.
   */
  def runOnContext(action: => Unit): Unit = asJava.runOnContext(lazyToVoidHandler(action))

  /**
   * Is the current thread an event loop thread?
   * @return true if current thread is an event loop thread.
   */
  def isEventLoop(): Boolean = asJava.isEventLoop()

  /**
   * Is the current thread an worker thread?
   * @return true if current thread is an worker thread.
   */
  def isWorker(): Boolean = asJava.isWorker()

  /**
   * Stop the eventbus and any resource managed by the eventbus.
   */
  def stop(): Unit = asJava.stop()

}

/** Factory for [[org.vertx.scala.core.Vertx]] instances. */
object Vertx {
  def apply(actual: JVertx) = new Vertx(actual)
}