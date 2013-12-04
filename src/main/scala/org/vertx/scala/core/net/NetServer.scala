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

import org.vertx.scala.core.FunctionConverters._
import org.vertx.java.core.net.{ NetServer => JNetServer }
import org.vertx.java.core.AsyncResult
import org.vertx.scala.core.ServerSSLSupport
import org.vertx.scala.core.ServerTCPSupport
import org.vertx.scala.core.Closeable
import org.vertx.scala.Self

/**
 * Represents a TCP or SSL server<p>
 * If an instance is instantiated from an event loop then the handlers
 * of the instance will always be called on that same event loop.
 * If an instance is instantiated from some other arbitrary Java thread (i.e. when running embedded) then
 * and event loop will be assigned to the instance and used when any of its handlers
 * are called.<p>
 * Instances of this class are thread-safe.<p>
 *
 * @author <a href="http://tfox.org">Tim Fox</a>
 * @author swilliams
 * @author <a href="http://www.campudus.com/">Joern Bernhardt</a>
 * @author Galder Zamarreño
 */
final class NetServer private[scala] (val asJava: JNetServer) extends Self
  with ServerSSLSupport
  with ServerTCPSupport
  with Closeable {

  override type J = JNetServer

  /**
   * Supply a connect handler for this server. The server can only have at most one connect handler at any one time.
   * As the server accepts TCP or SSL connections it creates an instance of {@link org.vertx.java.core.net.NetSocket} and passes it to the
   * connect handler.
   * @return a reference to this so multiple method calls can be chained together
   */
  def connectHandler(connectHandler: NetSocket => Unit): NetServer = wrap(asJava.connectHandler(connectHandler.compose(NetSocket.apply)))

  /**
   * Tell the server to start listening on all available interfaces and port {@code port}. Be aware this is an
   * async operation and the server may not bound on return of the method.
   */
  def listen(port: Int): NetServer = wrap(asJava.listen(port))

  /**
   * Instruct the server to listen for incoming connections on the specified {@code port} and all available interfaces.
   */
  def listen(port: Int, listenHandler: AsyncResult[NetServer] => Unit): NetServer = wrap(asJava.listen(port, arNetServer(listenHandler)))

  /**
   * Tell the server to start listening on port {@code port} and hostname or ip address given by {@code host}. Be aware this is an
   * async operation and the server may not bound on return of the method.
   *
   */
  def listen(port: Int, host: String): NetServer = wrap(asJava.listen(port, host))

  /**
   * Instruct the server to listen for incoming connections on the specified {@code port} and {@code host}. {@code host} can
   * be a host name or an IP address.
   */
  def listen(port: Int, host: String, listenHandler: AsyncResult[NetServer] => Unit): NetServer = wrap(asJava.listen(port, host, arNetServer(listenHandler)))

  /**
   * The actual port the server is listening on. This is useful if you bound the server specifying 0 as port number
   * signifying an ephemeral port
   */
  def port(): Int = asJava.port()

  /**
   * The host.
   */
  def host(): String = asJava.host()

  private def arNetServer = asyncResultConverter(NetServer.apply) _

}

/** Factory for [[org.vertx.scala.core.net.NetServer]] instances. */
object NetServer {
  def apply(actual: JNetServer) = new NetServer(actual)
}
