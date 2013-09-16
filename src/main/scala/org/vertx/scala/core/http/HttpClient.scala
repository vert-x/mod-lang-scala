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

// FIXME Java types
import org.vertx.java.core.http.{ HttpClient => JHttpClient }
import org.vertx.java.core.http.{ HttpClientResponse => JHttpClientResponse }
import org.vertx.java.core.http.{ WebSocket => JWebSocket }
import org.vertx.scala.core.{ WrappedClientSSLSupport, WrappedTCPSupport }
import org.vertx.scala.core.Handler
import org.vertx.scala.core.MultiMap

/**
 * @author swilliams
 * @author Galder Zamarreño
 */
object HttpClient {
  def apply(actual: JHttpClient) = new HttpClient(actual)
}

/**
 * @author <a href="http://www.campudus.com/">Joern Bernhardt</a>
 */
class HttpClient(protected[this] val internal: JHttpClient) extends JHttpClient with WrappedTCPSupport with WrappedClientSSLSupport {
  override type InternalType = JHttpClient

  // Members declared in HttpClient
  def close(): Unit = ???
  def connect(x$1: String, x$2: Handler[JHttpClientResponse]): HttpClientRequest = ???
  def connectWebsocket(x$1: String, x$2: WebSocketVersion, x$3: MultiMap, x$4: Handler[JWebSocket]): HttpClient = ???
  def connectWebsocket(x$1: String, x$2: WebSocketVersion, x$3: Handler[JWebSocket]): HttpClient = ???
  def connectWebsocket(x$1: String, x$2: Handler[JWebSocket]): HttpClient = ???
  def delete(x$1: String, x$2: Handler[JHttpClientResponse]): HttpClientRequest = ???
  def exceptionHandler(x$1: Handler[Throwable]): HttpClient = ???
  def get(x$1: String, x$2: Handler[JHttpClientResponse]): HttpClientRequest = ???
  def getConnectTimeout(): Int = ???
  def getHost(): String = ???
  def getMaxPoolSize(): Int = ???
  def getNow(x$1: String, x$2: MultiMap, x$3: Handler[JHttpClientResponse]): HttpClient = ???
  def getNow(x$1: String, x$2: Handler[JHttpClientResponse]): HttpClient = ???
  def getPort(): Int = ???
  def head(x$1: String, x$2: Handler[JHttpClientResponse]): HttpClientRequest = ???
  def isKeepAlive(): Boolean = ???
  def isVerifyHost(): Boolean = ???
  def options(x$1: String, x$2: Handler[JHttpClientResponse]): HttpClientRequest = ???
  def patch(x$1: String, x$2: Handler[JHttpClientResponse]): HttpClientRequest = ???
  def post(x$1: String, x$2: Handler[JHttpClientResponse]): HttpClientRequest = ???
  def put(x$1: String, x$2: Handler[JHttpClientResponse]): HttpClientRequest = ???
  def request(x$1: String, x$2: String, x$3: Handler[JHttpClientResponse]): HttpClientRequest = ???
  def setConnectTimeout(x$1: Int): HttpClient = ???
  def setHost(x$1: String): HttpClient = ???
  def setKeepAlive(x$1: Boolean): HttpClient = ???
  def setMaxPoolSize(x$1: Int): HttpClient = ???
  def setPort(x$1: Int): HttpClient = ???
  def setVerifyHost(x$1: Boolean): HttpClient = ???
  def trace(x$1: String, x$2: Handler[JHttpClientResponse]): HttpClientRequest = ???

}