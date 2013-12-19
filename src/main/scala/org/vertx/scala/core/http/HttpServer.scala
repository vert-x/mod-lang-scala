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

import org.vertx.java.core.http.{ HttpServer => JHttpServer }
import org.vertx.java.core.http.{ HttpServerRequest => JHttpServerRequest, ServerWebSocket => JServerWebSocket }
import org.vertx.scala.core.ServerSSLSupport
import org.vertx.scala.core.ServerTCPSupport
import org.vertx.scala.core.AsyncResult
import org.vertx.scala.core.Handler
import org.vertx.scala.core.FunctionConverters._
import org.vertx.scala.core.Closeable
import org.vertx.scala.Self

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
 * @author swilliams
 * @author <a href="http://www.campudus.com/">Joern Bernhardt</a>
 */
final class HttpServer private[scala] (val asJava: JHttpServer) extends Self
  with ServerTCPSupport
  with ServerSSLSupport
  with Closeable {

  override type J = JHttpServer

  /**
   * Tell the server to start listening on port `port} and hostname or ip address given by `host}.
   *
   * @param port The port to listen on.
   * @param host The hostname or ip address.
   * @param listenHandler Callback when bind is done.
   */
  def listen(port: Int, host: String, listenHandler: AsyncResult[HttpServer] => Unit): HttpServer =
    wrap(asJava.listen(port, host, arHttpServerFnConverter(listenHandler)))

  /**
   * Tell the server to start listening on port `port` and hostname or ip address given by `host`. Be aware this is an
   *
   * async operation and the server may not bound on return of the method.
   * @param port The port to listen on.
   * @param host The hostname or ip address.
   */
  def listen(port: Int, host: String): HttpServer = wrap(asJava.listen(port, host))

  /**
   * Tell the server to start listening on all available interfaces and port `port`.
   *
   * @param port The port to listen on.
   * @param listenHandler Callback when bind is done.
   */
  def listen(port: Int, listenHandler: AsyncResult[HttpServer] => Unit): HttpServer =
    wrap(asJava.listen(port, arHttpServerFnConverter(listenHandler)))

  /**
   * Tell the server to start listening on all available interfaces and port `port`. Be aware this is an
   * async operation and the server may not bound on return of the method.
   *
   * @param port The port to listen on.
   */
  def listen(port: Int): HttpServer = wrap(asJava.listen(port))

  /**
   * Get the request handler.
   *
   * @return The request handler.
   */
  def requestHandler(): HttpServerRequest => Unit =
    handlerToFn(asJava.requestHandler()).compose(HttpServerRequest.unapply)

  /**
   * Set the request handler for the server to `requestHandler`. As HTTP requests are received by the server,
   * instances of [[org.vertx.scala.core.http.HttpServerRequest]] will be created and passed to this handler.
   *
   * @return a reference to this, so methods can be chained.
   */
  def requestHandler(requestHandler: HttpServerRequest => Unit): HttpServer =
    wrap(asJava.requestHandler(httpServerRequestFnConverter(requestHandler)))

  /**
   * Get the websocket handler
   * @return The websocket handler
   */
  def websocketHandler(): ServerWebSocket => Unit =
    handlerToFn(asJava.websocketHandler()).compose(ServerWebSocket.unapply)

  /**
   * Set the websocket handler for the server to `wsHandler`. If a websocket connect handshake is successful a
   * new [[org.vertx.scala.core.http.ServerWebSocket]] instance will be created and passed to the handler.
   *
   * @return a reference to this, so methods can be chained.
   */
  def websocketHandler(wsHandler: ServerWebSocket => Unit): HttpServer =
    wrap(asJava.websocketHandler(serverWebSocketFnConverter(wsHandler)))

  /**
   * Set if the [[org.vertx.scala.core.http.HttpServer]] should compress the http response if the connected client supports it.
   */
  def setCompressionSupported(compressionSupported: Boolean): HttpServer = wrap(asJava.setCompressionSupported(compressionSupported))

  /**
   * Returns `true` if the [[org.vertx.scala.core.http.HttpServer]] should compress the http response if the connected client supports it.
   */
  def isCompressionSupported: Boolean = asJava.isCompressionSupported

  private def arHttpServerFnConverter(handler: AsyncResult[HttpServer] => Unit): Handler[AsyncResult[JHttpServer]] =
    asyncResultConverter(HttpServer.apply)(handler)

  private def httpServerRequestFnConverter(handler: HttpServerRequest => Unit): Handler[JHttpServerRequest] =
    fnToHandler(handler.compose(HttpServerRequest.apply))

  private def serverWebSocketFnConverter(handler: ServerWebSocket => Unit): Handler[JServerWebSocket] =
    fnToHandler(handler.compose(ServerWebSocket.apply))
}

/** Factory for [[org.vertx.scala.core.http.HttpServer]] instances by wrapping a Java instance. */
object HttpServer {
  def apply(internal: JHttpServer) = new HttpServer(internal)
}
