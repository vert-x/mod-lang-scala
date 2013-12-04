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

package org.vertx.scala.core.net

import org.vertx.java.core.net.{ NetClient => JNetClient }
import org.vertx.scala.core.FunctionConverters._
import org.vertx.scala.core.TCPSupport
import org.vertx.scala.core.ClientSSLSupport
import org.vertx.scala.core.AsyncResult
import org.vertx.scala.Self

/**
 * A TCP/SSL client.<p>
 * Multiple connections to different servers can be made using the same instance.<p>
 * This client supports a configurable number of connection attempts and a configurable delay
 * between attempts.<p>
 * If an instance is instantiated from an event loop then the handlers of the instance will always
 * be called on that same event loop. If an instance is instantiated from some other arbitrary Java
 * thread (i.e. when using embedded) then an event loop will be assigned to the instance and used
 * when any of its handlers are called.<p>
 * Instances of this class are thread-safe.<p>
 *
 * @author <a href="http://tfox.org">Tim Fox</a>
 * @author swilliams
 * @author Edgar Chan
 * @author <a href="http://www.campudus.com/">Joern Bernhardt</a>
 * @author Galder Zamarreño
 */
final class NetClient private[scala] (val asJava: JNetClient) extends Self
  with TCPSupport
  with ClientSSLSupport {

  override type J = JNetClient

  /**
   * Attempt to open a connection to a server at the specific {@code port} and host {@code localhost}
   * The connect is done asynchronously and on success, a
   * {@link NetSocket} instance is supplied via the {@code connectHandler} instance.
   *
   * @return A reference to this so multiple method calls can be chained together.
   */
  def connect(port: Int, connectCallback: AsyncResult[NetSocket] => Unit): NetClient =
    wrap(asJava.connect(port, arNetSocket(connectCallback)))

  /**
   * Attempt to open a connection to a server at the specific {@code port} and {@code host}.
   * {@code host} can be a valid host name or IP address. The connect is done asynchronously and on success, a
   * {@link NetSocket} instance is supplied via the {@code connectHandler} instance.
   *
   * @return a reference to this so multiple method calls can be chained together
   */
  def connect(port: Int, host: String, connectHandler: AsyncResult[NetSocket] => Unit): NetClient =
    wrap(asJava.connect(port, host, arNetSocket(connectHandler)))

  /**
   * Set the number of reconnection attempts. In the event a connection attempt fails, the client will attempt
   * to connect a further number of times, before it fails. Default value is zero.
   */
  def setReconnectAttempts(attempts: Int): NetClient =
    wrap(asJava.setReconnectAttempts(attempts))

  /**
   * Get the number of reconnect attempts.
   *
   * @return The number of reconnect attempts.
   */
  def getReconnectAttempts(): Int = asJava.getReconnectAttempts()

  /**
   * Set the reconnect interval, in milliseconds.
   */
  def setReconnectInterval(interval: Long): NetClient =
    wrap(asJava.setReconnectInterval(interval))

  /**
   * Get the reconnect interval, in milliseconds.
   *
   * @return The reconnect interval in milliseconds.
   */
  def getReconnectInterval(): Long = asJava.getReconnectInterval()

  /**
   * Set the connect timeout in milliseconds.
   *
   * @return a reference to this so multiple method calls can be chained together
   */
  def setConnectTimeout(timeout: Int): NetClient =
    wrap(asJava.setConnectTimeout(timeout))

  /**
   * Returns the connect timeout in milliseconds.
   *
   * @return The connect timeout in milliseconds.
   */
  def getConnectTimeout(): Int = asJava.getConnectTimeout()

  /**
   * Close the client. Any sockets which have not been closed manually will be closed here.
   */
  def close(): Unit = asJava.close()

  private def arNetSocket = asyncResultConverter(NetSocket.apply) _
}

/** Factory for [[org.vertx.scala.core.net.NetClient]] instances. */
object NetClient {
  def apply(actual: JNetClient) = new NetClient(actual)
}
