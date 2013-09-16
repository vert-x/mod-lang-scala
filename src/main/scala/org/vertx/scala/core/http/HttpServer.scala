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

// FIXME Get rid of Java types
import org.vertx.java.core.http.{ HttpServer => JHttpServer }
import org.vertx.java.core.http.{ HttpServerRequest => JHttpServerRequest, ServerWebSocket => JServerWebSocket }
import org.vertx.scala.core.{ WrappedServerSSLSupport, WrappedTCPSupport }
import org.vertx.scala.core.WrappedServerTCPSupport
import org.vertx.scala.core.AsyncResult
import org.vertx.scala.core.Handler
import org.vertx.scala.core.WrappedCloseable

/**
 * Companion object for {@link HttpServer}.
 *
 * @author swilliams
 */
object HttpServer {
  def apply(internal: JHttpServer) = new HttpServer(internal)
}

/**
 * An HTTP and WebSockets server<p>
 * If an instance is instantiated from an event loop then the handlers
 * of the instance will always be called on that same event loop.
 * If an instance is instantiated from some other arbitrary Java thread then
 * an event loop will be assigned to the instance and used when any of its handlers
 * are called.<p>
 * Instances of HttpServer are thread-safe.<p>
 *
 * @author <a href="http://tfox.org">Tim Fox</a>
 * @author <a href="http://www.campudus.com/">Joern Bernhardt</a>
 */
class HttpServer(protected[this] val internal: JHttpServer) extends JHttpServer with WrappedCloseable with WrappedServerTCPSupport with WrappedServerSSLSupport {
  override type InternalType = JHttpServer

  /**
   * Tell the server to start listening on port {@code port} and hostname or ip address given by {@code host}.
   * @param port The port to listen on.
   * @param host The hostname or ip address.
   * @param listenHandler Callback when bind is done.
   */
  override def listen(port: Int, host: String, listenHandler: Handler[AsyncResult[JHttpServer]]): HttpServer = wrap(internal.listen(port, host, listenHandler))

  /**
   * Tell the server to start listening on port {@code port} and hostname or ip address given by {@code host}. Be aware this is an
   * async operation and the server may not bound on return of the method.
   * @param port The port to listen on.
   * @param host The hostname or ip address.
   */
  override def listen(port: Int, host: String): HttpServer = wrap(internal.listen(port, host))

  /**
   * Tell the server to start listening on all available interfaces and port {@code port}.
   * @param port The port to listen on.
   * @param listenHandler Callback when bind is done.
   */
  override def listen(port: Int, listenHandler: Handler[AsyncResult[JHttpServer]]): HttpServer = wrap(internal.listen(port, listenHandler))

  /**
   * Tell the server to start listening on all available interfaces and port {@code port}. Be aware this is an
   * async operation and the server may not bound on return of the method.
   *
   * @param port The port to listen on.
   */
  override def listen(port: Int): HttpServer = wrap(internal.listen(port))

  /**
   * Get the request handler
   * @return The request handler
   */
  override def requestHandler(): Handler[JHttpServerRequest] = internal.requestHandler()

  /**
   * Set the request handler for the server to {@code requestHandler}. As HTTP requests are received by the server,
   * instances of {@link HttpServerRequest} will be created and passed to this handler.
   *
   * @return a reference to this, so methods can be chained.
   */
  override def requestHandler(requestHandler: Handler[JHttpServerRequest]): HttpServer = wrap(internal.requestHandler(requestHandler))

  /**
   * Get the websocket handler
   * @return The websocket handler
   */
  override def websocketHandler(): Handler[JServerWebSocket] = internal.websocketHandler()

  /**
   * Set the websocket handler for the server to {@code wsHandler}. If a websocket connect handshake is successful a
   * new {@link ServerWebSocket} instance will be created and passed to the handler.
   *
   * @return a reference to this, so methods can be chained.
   */
  override def websocketHandler(wsHandler: Handler[JServerWebSocket]): HttpServer = wrap(internal.websocketHandler(wsHandler))

}