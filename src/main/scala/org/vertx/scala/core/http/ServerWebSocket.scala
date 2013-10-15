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

package org.vertx.scala.core.http

import org.vertx.java.core.http.{ ServerWebSocket => JServerWebSocket }
import org.vertx.scala.core.streams.WrappedReadWriteStream

/**
 * Represents a server side WebSocket that is passed into a the websocketHandler of an {@link HttpServer}<p>
 * Instances of this class are not thread-safe<p>
 *
 * @author <a href="http://tfox.org">Tim Fox</a>
 * @author swilliams
 * @author Galder Zamarreño
 * @author <a href="http://www.campudus.com/">Joern Bernhardt</a>
 */
class ServerWebSocket(protected val internal: JServerWebSocket) extends WrappedWebSocketBase {
  override type InternalType = JServerWebSocket

  /**
   * The path the websocket is attempting to connect at
   */
  def path(): String = internal.path()

  /**
   * The query string passed on the websocket uri
   */
  def query(): String = internal.query()

  /**
   * A map of all headers in the request to upgrade to websocket
   */
  def headers(): org.vertx.java.core.MultiMap = internal.headers()

  /**
   * Reject the WebSocket<p>
   * Calling this method from the websocketHandler gives you the opportunity to reject
   * the websocket, which will cause the websocket handshake to fail by returning
   * a 404 response code.<p>
   * You might use this method, if for example you only want to accept websockets
   * with a particular path.
   */
  def reject(): ServerWebSocket = wrap(internal.reject())
}

/** Factory for [[http.ServerWebSocket]] instances. */
object ServerWebSocket {
  def apply(socket: JServerWebSocket) = new ServerWebSocket(socket)
  def unapply(socket: ServerWebSocket): JServerWebSocket = socket.toJava
}
