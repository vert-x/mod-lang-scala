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

import org.vertx.java.core.http.{ HttpClient => JHttpClient }
import org.vertx.scala.core.{ ClientSSLSupport, TCPSupport }
import org.vertx.scala.core.MultiMap
import org.vertx.scala.core.FunctionConverters._
import org.vertx.scala.Self

/**
 * An HTTP client that maintains a pool of connections to a specific host, at a specific port. The client supports
 * pipelining of requests.<p>
 * As well as HTTP requests, the client can act as a factory for `WebSocket websockets`.<p>
 * If an instance is instantiated from an event loop then the handlers
 * of the instance will always be called on that same event loop.
 * If an instance is instantiated from some other arbitrary Java thread (i.e. when running embedded) then
 * and event loop will be assigned to the instance and used when any of its handlers
 * are called.<p>
 * Instances of HttpClient are thread-safe.<p>
 *
 * @author <a href="http://tfox.org">Tim Fox</a>
 * @author swilliams
 * @author Galder Zamarreño
 * @author <a href="http://www.campudus.com/">Joern Bernhardt</a>
 */
final class HttpClient private[scala] (val asJava: JHttpClient) extends Self
  with TCPSupport
  with ClientSSLSupport {

  override type J = JHttpClient

  /**
   * Set an exception handler
   *
   * @return A reference to this, so multiple invocations can be chained together.
   */
  def exceptionHandler(handler: Throwable => Unit): HttpClient =
    wrap(asJava.exceptionHandler(handler))

  /**
   * Set the maximum pool size<p>
   * The client will maintain up to `maxConnections` HTTP connections in an internal pool<p>
   * @return A reference to this, so multiple invocations can be chained together.
   */
  def setMaxPoolSize(maxConnections: Int): HttpClient =
    wrap(asJava.setMaxPoolSize(maxConnections))

  /**
   * Returns the maximum number of connections in the pool
   */
  def getMaxPoolSize: Int = asJava.getMaxPoolSize

  /**
   * If `keepAlive` is `true` then, after the request has ended the connection will be returned to the pool
   * where it can be used by another request. In this manner, many HTTP requests can be pipe-lined over an HTTP connection.
   * Keep alive connections will not be closed until the [[org.vertx.scala.core.http.HttpClient.close()]] method is invoked.<p>
   * If `keepAlive` is `false` then a new connection will be created for each request and it won't ever go in the pool,
   * the connection will closed after the response has been received. Even with no keep alive,
   * the client will not allow more than [[org.vertx.scala.core.http.HttpClient.getMaxPoolSize()]] connections to be created at any one time. <p>
   * @return A reference to this, so multiple invocations can be chained together.
   */
  def setKeepAlive(keepAlive: Boolean): HttpClient = wrap(asJava.setKeepAlive(keepAlive))

  /**
   *
   * @return Is the client keep alive?
   */
  def isKeepAlive: Boolean = asJava.isKeepAlive

  /**
   * Set the port that the client will attempt to connect to the server on to `port`. The default value is
   * `80`
   * @return A reference to this, so multiple invocations can be chained together.
   */
  def setPort(port: Int): HttpClient = wrap(asJava.setPort(port))

  /**
   *
   * @return The port
   */
  def getPort: Int = asJava.getPort

  /**
   * Set the host that the client will attempt to connect to the server on to `host`. The default value is
   * `localhost`
   * @return A reference to this, so multiple invocations can be chained together.
   */
  def setHost(host: String): HttpClient = wrap(asJava.setHost(host))

  /**
   *
   * @return The host
   */
  def getHost: String = asJava.getHost

  /**
   * Attempt to connect an HTML5 websocket to the specified URI<p>
   * The connect is done asynchronously and `wsConnect` is called back with the websocket
   */
  def connectWebsocket(uri: String, wsConnect: WebSocket => Unit): HttpClient =
    wrap(asJava.connectWebsocket(uri, webSocketFnConverter(wsConnect)))

  /**
   * Attempt to connect an HTML5 websocket to the specified URI<p>
   * This version of the method allows you to specify the websockets version using the `wsVersion parameter`
   * The connect is done asynchronously and `wsConnect` is called back with the websocket
   */
  def connectWebsocket(uri: String, wsVersion: WebSocketVersion, wsConnect: WebSocket => Unit): HttpClient =
    wrap(asJava.connectWebsocket(uri, wsVersion, webSocketFnConverter(wsConnect)))

  /**
   * Attempt to connect an HTML5 websocket to the specified URI<p>
   * This version of the method allows you to specify the websockets version using the `wsVersion parameter`
   * You can also specify a set of headers to append to the upgrade request
   * The connect is done asynchronously and `wsConnect` is called back with the websocket
   */
  def connectWebsocket(uri: String, wsVersion: WebSocketVersion, headers: MultiMap, wsConnect: WebSocket => Unit): HttpClient =
    wrap(asJava.connectWebsocket(uri, wsVersion, scalaMultiMapToMultiMap(headers), webSocketFnConverter(wsConnect)))

  /**
   * This is a quick version of the [[org.vertx.scala.core.http.HttpClient.get(String, org.vertx.java.core.Handler)]]
   * method where you do not want to do anything with the request before sending.<p>
   * Normally with any of the HTTP methods you create the request then when you are ready to send it you call
   * [[org.vertx.scala.core.http.HttpClientRequest.end()]] on it. With this method the request is immediately sent.<p>
   * When an HTTP response is received from the server the `responseHandler` is called passing in the response.
   */
  def getNow(uri: String, responseHandler: HttpClientResponse => Unit): HttpClient =
    wrap(asJava.getNow(uri, httpClientResponseFnConverter(responseHandler)))

  /**
   * This method works in the same manner as [[org.vertx.scala.core.http.HttpClient.getNow(String, org.vertx.java.core.Handler)]],
   * except that it allows you specify a set of `headers` that will be sent with the request.
   */
  def getNow(uri: String, headers: MultiMap, responseHandler: HttpClientResponse => Unit): HttpClient =
    wrap(asJava.getNow(uri, scalaMultiMapToMultiMap(headers), httpClientResponseFnConverter(responseHandler)))

  /**
   * This method returns an [[org.vertx.scala.core.http.HttpClientRequest]] instance which represents an HTTP OPTIONS request with the specified `uri`.<p>
   * When an HTTP response is received from the server the `responseHandler` is called passing in the response.
   */
  def options(uri: String, responseHandler: HttpClientResponse => Unit): HttpClientRequest =
    HttpClientRequest(asJava.options(uri, httpClientResponseFnConverter(responseHandler)))

  /**
   * This method returns an [[org.vertx.scala.core.http.HttpClientRequest]] instance which represents an HTTP GET request with the specified `uri`.<p>
   * When an HTTP response is received from the server the `responseHandler` is called passing in the response.
   */
  def get(uri: String, responseHandler: HttpClientResponse => Unit): HttpClientRequest =
    HttpClientRequest(asJava.get(uri, httpClientResponseFnConverter(responseHandler)))

  /**
   * This method returns an [[org.vertx.scala.core.http.HttpClientRequest]] instance which represents an HTTP HEAD request with the specified `uri`.<p>
   * When an HTTP response is received from the server the `responseHandler` is called passing in the response.
   */
  def head(uri: String, responseHandler: HttpClientResponse => Unit): HttpClientRequest =
    HttpClientRequest(asJava.head(uri, httpClientResponseFnConverter(responseHandler)))

  /**
   * This method returns an [[org.vertx.scala.core.http.HttpClientRequest]] instance which represents an HTTP POST request with the specified `uri`.<p>
   * When an HTTP response is received from the server the `responseHandler` is called passing in the response.
   */
  def post(uri: String, responseHandler: HttpClientResponse => Unit): HttpClientRequest =
    HttpClientRequest(asJava.post(uri, httpClientResponseFnConverter(responseHandler)))

  /**
   * This method returns an [[org.vertx.scala.core.http.HttpClientRequest]] instance which represents an HTTP PUT request with the specified `uri`.<p>
   * When an HTTP response is received from the server the `responseHandler` is called passing in the response.
   */
  def put(uri: String, responseHandler: HttpClientResponse => Unit): HttpClientRequest =
    HttpClientRequest(asJava.put(uri, httpClientResponseFnConverter(responseHandler)))

  /**
   * This method returns an [[org.vertx.scala.core.http.HttpClientRequest]] instance which represents an HTTP DELETE request with the specified `uri`.<p>
   * When an HTTP response is received from the server the `responseHandler` is called passing in the response.
   */
  def delete(uri: String, responseHandler: HttpClientResponse => Unit): HttpClientRequest =
    HttpClientRequest(asJava.delete(uri, httpClientResponseFnConverter(responseHandler)))

  /**
   * This method returns an [[org.vertx.scala.core.http.HttpClientRequest]] instance which represents an HTTP TRACE request with the specified `uri`.<p>
   * When an HTTP response is received from the server the `responseHandler` is called passing in the response.
   */
  def trace(uri: String, responseHandler: HttpClientResponse => Unit): HttpClientRequest =
    HttpClientRequest(asJava.trace(uri, httpClientResponseFnConverter(responseHandler)))

  /**
   * This method returns an [[org.vertx.scala.core.http.HttpClientRequest]] instance which represents an HTTP CONNECT request with the specified `uri`.<p>
   * When an HTTP response is received from the server the `responseHandler` is called passing in the response.
   */
  def connect(uri: String, responseHandler: HttpClientResponse => Unit): HttpClientRequest =
    HttpClientRequest(asJava.connect(uri, httpClientResponseFnConverter(responseHandler)))

  /**
   * This method returns an [[org.vertx.scala.core.http.HttpClientRequest]] instance which represents an HTTP PATCH request with the specified `uri`.<p>
   * When an HTTP response is received from the server the `responseHandler` is called passing in the response.
   */
  def patch(uri: String, responseHandler: HttpClientResponse => Unit): HttpClientRequest =
    HttpClientRequest(asJava.patch(uri, httpClientResponseFnConverter(responseHandler)))

  /**
   * This method returns an [[org.vertx.scala.core.http.HttpClientRequest]] instance which represents an HTTP request with the specified `uri`.
   * The specific HTTP method (e.g. GET, POST, PUT etc) is specified using the parameter `method`<p>
   * When an HTTP response is received from the server the `responseHandler` is called passing in the response.
   *
   * Because of the nature of CONNECT, the connection is automatically upgraded to raw TCP if a response code of 200 is received from the
   * remote peer. In this case you are able to get hold of the raw TCP socket via calling {@link HttpClientResponse#netSocket()}.
   */
  def request(method: String, uri: String, responseHandler: HttpClientResponse => Unit): HttpClientRequest =
    HttpClientRequest(asJava.request(method, uri, httpClientResponseFnConverter(responseHandler)))

  /**
   * Close the HTTP client. This will cause any pooled HTTP connections to be closed.
   */
  def close(): Unit = asJava.close()

  /**
   * If `verifyHost` is `true`, then the client will try to validate the remote server's certificate
   * hostname against the requested host. Should default to 'true'.
   * This method should only be used in SSL mode, i.e. after [[org.vertx.scala.core.http.HttpClient.setSSL(boolean)]] has been set to `true`.
   * @return A reference to this, so multiple invocations can be chained together.
   */
  def setVerifyHost(verifyHost: Boolean): HttpClient = wrap(asJava.setVerifyHost(verifyHost))

  /**
   *
   * @return true if this client will validate the remote server's certificate hostname against the requested host
   */
  def isVerifyHost: Boolean = asJava.isVerifyHost

  /**
   * Set the connect timeout in milliseconds.
   * @return a reference to this so multiple method calls can be chained together
   */
  def setConnectTimeout(timeout: Int): HttpClient = wrap(asJava.setConnectTimeout(timeout))

  /**
   *
   * @return The connect timeout in milliseconds
   */
  def getConnectTimeout: Int = asJava.getConnectTimeout

  /**
   * Set if the [[org.vertx.scala.core.http.HttpClient]] should try to use compression.
   */
  def setTryUseCompression(tryUseCompression: Boolean): HttpClient =
    wrap(asJava.setTryUseCompression(tryUseCompression))

  /**
   * Returns `true` if the [[org.vertx.scala.core.http.HttpClient]] should try to use compression.
   */
  def getTryUseCompression: Boolean = asJava.getTryUseCompression

  /**
   * Sets the maximum websocket frame size in bytes. Default is 65536 bytes.
   * @param maxSize The size in bytes
   */
  def setMaxWebSocketFrameSize(maxSize: Int): HttpClient =
    wrap(asJava.setMaxWebSocketFrameSize(maxSize))

  /**
   * Get the  maximum websocket frame size in bytes.
   */
  def getMaxWebSocketFrameSize: Int = asJava.getMaxWebSocketFrameSize

  private def httpClientResponseFnConverter(handler: HttpClientResponse => Unit) =
    fnToHandler(handler.compose(HttpClientResponse.apply))

  private def webSocketFnConverter(handler: WebSocket => Unit) =
    fnToHandler(handler.compose(WebSocket.apply))

}

/** Factory for [[org.vertx.scala.core.http.HttpClient]] instances by wrapping a Java instance. */
object HttpClient {
  def apply(actual: JHttpClient) = new HttpClient(actual)
}
