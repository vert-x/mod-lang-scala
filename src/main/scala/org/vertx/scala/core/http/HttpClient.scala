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
import org.vertx.scala.core.{ WrappedClientSSLSupport, WrappedTCPSupport }
import org.vertx.scala.core.Handler
import org.vertx.scala.core.MultiMap
import org.vertx.scala.core.FunctionConverters._
import org.vertx.scala.core.streams.WrappedExceptionSupport

/** Factory for [[http.HttpClient]] instances by wrapping a Java instance. */
object HttpClient {
  def apply(actual: JHttpClient) = new HttpClient(actual)
}

/**
 * An HTTP client that maintains a pool of connections to a specific host, at a specific port. The client supports
 * pipelining of requests.<p>
 * As well as HTTP requests, the client can act as a factory for {@code WebSocket websockets}.<p>
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
class HttpClient(protected[this] val internal: JHttpClient) extends WrappedTCPSupport with WrappedClientSSLSupport {
  override type InternalType = JHttpClient

  /**
   * Set an exception handler
   *
   * @return A reference to this, so multiple invocations can be chained together.
   */
  def exceptionHandler(handler: Throwable => Unit): HttpClient =
    wrap(internal.exceptionHandler(handler))

  /**
   * Set the maximum pool size<p>
   * The client will maintain up to {@code maxConnections} HTTP connections in an internal pool<p>
   * @return A reference to this, so multiple invocations can be chained together.
   */
  def setMaxPoolSize(maxConnections: Int): HttpClient =
    wrap(internal.setMaxPoolSize(maxConnections))

  /**
   * Returns the maximum number of connections in the pool
   */
  def getMaxPoolSize(): Int = internal.getMaxPoolSize()

  /**
   * If {@code keepAlive} is {@code true} then, after the request has ended the connection will be returned to the pool
   * where it can be used by another request. In this manner, many HTTP requests can be pipe-lined over an HTTP connection.
   * Keep alive connections will not be closed until the {@link #close() close()} method is invoked.<p>
   * If {@code keepAlive} is {@code false} then a new connection will be created for each request and it won't ever go in the pool,
   * the connection will closed after the response has been received. Even with no keep alive,
   * the client will not allow more than {@link #getMaxPoolSize()} connections to be created at any one time. <p>
   * @return A reference to this, so multiple invocations can be chained together.
   */
  def setKeepAlive(keepAlive: Boolean): HttpClient = wrap(internal.setKeepAlive(keepAlive))

  /**
   *
   * @return Is the client keep alive?
   */
  def isKeepAlive(): Boolean = internal.isKeepAlive()

  /**
   * Set the port that the client will attempt to connect to the server on to {@code port}. The default value is
   * {@code 80}
   * @return A reference to this, so multiple invocations can be chained together.
   */
  def setPort(port: Int): HttpClient = wrap(internal.setPort(port))

  /**
   *
   * @return The port
   */
  def getPort(): Int = internal.getPort()

  /**
   * Set the host that the client will attempt to connect to the server on to {@code host}. The default value is
   * {@code localhost}
   * @return A reference to this, so multiple invocations can be chained together.
   */
  def setHost(host: String): HttpClient = wrap(internal.setHost(host))

  /**
   *
   * @return The host
   */
  def getHost(): String = internal.getHost()

  /**
   * Attempt to connect an HTML5 websocket to the specified URI<p>
   * The connect is done asynchronously and {@code wsConnect} is called back with the websocket
   */
  def connectWebsocket(uri: String, wsConnect: WebSocket => Unit): HttpClient =
    wrap(internal.connectWebsocket(uri, webSocketFnConverter(wsConnect)))

  /**
   * Attempt to connect an HTML5 websocket to the specified URI<p>
   * This version of the method allows you to specify the websockets version using the {@code wsVersion parameter}
   * The connect is done asynchronously and {@code wsConnect} is called back with the websocket
   */
  def connectWebsocket(uri: String, wsVersion: WebSocketVersion, wsConnect: WebSocket => Unit): HttpClient =
    wrap(internal.connectWebsocket(uri, wsVersion, webSocketFnConverter(wsConnect)))

  /**
   * Attempt to connect an HTML5 websocket to the specified URI<p>
   * This version of the method allows you to specify the websockets version using the {@code wsVersion parameter}
   * You can also specify a set of headers to append to the upgrade request
   * The connect is done asynchronously and {@code wsConnect} is called back with the websocket
   */
  def connectWebsocket(uri: String, wsVersion: WebSocketVersion, headers: MultiMap, wsConnect: WebSocket => Unit): HttpClient =
    wrap(internal.connectWebsocket(uri, wsVersion, headers, webSocketFnConverter(wsConnect)))

  /**
   * This is a quick version of the {@link #get(String, org.vertx.java.core.Handler)}
   * method where you do not want to do anything with the request before sending.<p>
   * Normally with any of the HTTP methods you create the request then when you are ready to send it you call
   * {@link HttpClientRequest#end()} on it. With this method the request is immediately sent.<p>
   * When an HTTP response is received from the server the {@code responseHandler} is called passing in the response.
   */
  def getNow(uri: String, responseHandler: HttpClientResponse => Unit): HttpClient =
    wrap(internal.getNow(uri, httpClientResponseFnConverter(responseHandler)))

  /**
   * This method works in the same manner as {@link #getNow(String, org.vertx.java.core.Handler)},
   * except that it allows you specify a set of {@code headers} that will be sent with the request.
   */
  def getNow(uri: String, headers: MultiMap, responseHandler: HttpClientResponse => Unit): HttpClient =
    wrap(internal.getNow(uri, headers, httpClientResponseFnConverter(responseHandler)))

  // TODO the following could reduce the code a lot, but would the compiler be able to optimize it?
  // private def httpRequest(internalMethod: (String, Handler[JHttpClientResponse]) => JHttpClientRequest) ={
  //   (uri: String, responseHandler: JHttpClientResponse=>Unit) => 
  //     HttpClientRequest(internalMethod(uri, responseHandler))
  // }
  // def options = httpRequest(internal.options)
  // def get = httpRequest(internal.get)
  // ...

  /**
   * This method returns an {@link HttpClientRequest} instance which represents an HTTP OPTIONS request with the specified {@code uri}.<p>
   * When an HTTP response is received from the server the {@code responseHandler} is called passing in the response.
   */
  def options(uri: String, responseHandler: HttpClientResponse => Unit): HttpClientRequest =
    HttpClientRequest(internal.options(uri, httpClientResponseFnConverter(responseHandler)))

  /**
   * This method returns an {@link HttpClientRequest} instance which represents an HTTP GET request with the specified {@code uri}.<p>
   * When an HTTP response is received from the server the {@code responseHandler} is called passing in the response.
   */
  def get(uri: String, responseHandler: HttpClientResponse => Unit): HttpClientRequest =
    HttpClientRequest(internal.get(uri, httpClientResponseFnConverter(responseHandler)))

  /**
   * This method returns an {@link HttpClientRequest} instance which represents an HTTP HEAD request with the specified {@code uri}.<p>
   * When an HTTP response is received from the server the {@code responseHandler} is called passing in the response.
   */
  def head(uri: String, responseHandler: HttpClientResponse => Unit): HttpClientRequest =
    HttpClientRequest(internal.head(uri, httpClientResponseFnConverter(responseHandler)))

  /**
   * This method returns an {@link HttpClientRequest} instance which represents an HTTP POST request with the specified {@code uri}.<p>
   * When an HTTP response is received from the server the {@code responseHandler} is called passing in the response.
   */
  def post(uri: String, responseHandler: HttpClientResponse => Unit): HttpClientRequest =
    HttpClientRequest(internal.post(uri, httpClientResponseFnConverter(responseHandler)))

  /**
   * This method returns an {@link HttpClientRequest} instance which represents an HTTP PUT request with the specified {@code uri}.<p>
   * When an HTTP response is received from the server the {@code responseHandler} is called passing in the response.
   */
  def put(uri: String, responseHandler: HttpClientResponse => Unit): HttpClientRequest =
    HttpClientRequest(internal.put(uri, httpClientResponseFnConverter(responseHandler)))

  /**
   * This method returns an {@link HttpClientRequest} instance which represents an HTTP DELETE request with the specified {@code uri}.<p>
   * When an HTTP response is received from the server the {@code responseHandler} is called passing in the response.
   */
  def delete(uri: String, responseHandler: HttpClientResponse => Unit): HttpClientRequest =
    HttpClientRequest(internal.delete(uri, httpClientResponseFnConverter(responseHandler)))

  /**
   * This method returns an {@link HttpClientRequest} instance which represents an HTTP TRACE request with the specified {@code uri}.<p>
   * When an HTTP response is received from the server the {@code responseHandler} is called passing in the response.
   */
  def trace(uri: String, responseHandler: HttpClientResponse => Unit): HttpClientRequest =
    HttpClientRequest(internal.trace(uri, httpClientResponseFnConverter(responseHandler)))

  /**
   * This method returns an {@link HttpClientRequest} instance which represents an HTTP CONNECT request with the specified {@code uri}.<p>
   * When an HTTP response is received from the server the {@code responseHandler} is called passing in the response.
   */
  def connect(uri: String, responseHandler: HttpClientResponse => Unit): HttpClientRequest =
    HttpClientRequest(internal.connect(uri, httpClientResponseFnConverter(responseHandler)))

  /**
   * This method returns an {@link HttpClientRequest} instance which represents an HTTP PATCH request with the specified {@code uri}.<p>
   * When an HTTP response is received from the server the {@code responseHandler} is called passing in the response.
   */
  def patch(uri: String, responseHandler: HttpClientResponse => Unit): HttpClientRequest =
    HttpClientRequest(internal.patch(uri, httpClientResponseFnConverter(responseHandler)))

  /**
   * This method returns an {@link HttpClientRequest} instance which represents an HTTP request with the specified {@code uri}.
   * The specific HTTP method (e.g. GET, POST, PUT etc) is specified using the parameter {@code method}<p>
   * When an HTTP response is received from the server the {@code responseHandler} is called passing in the response.
   */
  def request(method: String, uri: String, responseHandler: HttpClientResponse => Unit): HttpClientRequest =
    HttpClientRequest(internal.request(method, uri, httpClientResponseFnConverter(responseHandler)))

  /**
   * Close the HTTP client. This will cause any pooled HTTP connections to be closed.
   */
  def close(): Unit = internal.close()

  /**
   * If {@code verifyHost} is {@code true}, then the client will try to validate the remote server's certificate
   * hostname against the requested host. Should default to 'true'.
   * This method should only be used in SSL mode, i.e. after {@link #setSSL(boolean)} has been set to {@code true}.
   * @return A reference to this, so multiple invocations can be chained together.
   */
  def setVerifyHost(verifyHost: Boolean): HttpClient = wrap(internal.setVerifyHost(verifyHost))

  /**
   *
   * @return true if this client will validate the remote server's certificate hostname against the requested host
   */
  def isVerifyHost(): Boolean = internal.isVerifyHost()

  /**
   * Set the connect timeout in milliseconds.
   * @return a reference to this so multiple method calls can be chained together
   */
  def setConnectTimeout(timeout: Int): HttpClient = wrap(internal.setConnectTimeout(timeout))

  /**
   *
   * @return The connect timeout in milliseconds
   */
  def getConnectTimeout(): Int = internal.getConnectTimeout()

  private def httpClientRequestFnConverter(handler: HttpClientRequest => Unit) =
    fnToHandler(handler.compose(HttpClientRequest.apply))

  private def httpClientResponseFnConverter(handler: HttpClientResponse => Unit) =
    fnToHandler(handler.compose(HttpClientResponse.apply))

  private def webSocketFnConverter(handler: WebSocket => Unit) =
    fnToHandler(handler.compose(WebSocket.apply))

}