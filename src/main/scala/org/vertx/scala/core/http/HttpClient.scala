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

import org.vertx.java.core.http.{HttpClient => JHttpClient}
import org.vertx.scala.core.{WrappedClientSSLSupport, WrappedTCPSupport}

/**
 * @author swilliams
 * @author Galder Zamarreño
 */
object HttpClient {

  def apply(actual: JHttpClient) =
    new HttpClient(actual)
}

class HttpClient(protected[this] val internal: JHttpClient) extends JHttpClient with WrappedTCPSupport with WrappedClientSSLSupport {
  override type InternalType = JHttpClient

  // Members declared in org.vertx.java.core.http.HttpClient
  def close(): Unit = ???
  def connect(x$1: String, x$2: org.vertx.java.core.Handler[org.vertx.java.core.http.HttpClientResponse]): org.vertx.java.core.http.HttpClientRequest = ???
  def connectWebsocket(x$1: String, x$2: org.vertx.java.core.http.WebSocketVersion, x$3: org.vertx.java.core.MultiMap, x$4: org.vertx.java.core.Handler[org.vertx.java.core.http.WebSocket]): org.vertx.java.core.http.HttpClient = ???
  def connectWebsocket(x$1: String, x$2: org.vertx.java.core.http.WebSocketVersion, x$3: org.vertx.java.core.Handler[org.vertx.java.core.http.WebSocket]): org.vertx.java.core.http.HttpClient = ???
  def connectWebsocket(x$1: String, x$2: org.vertx.java.core.Handler[org.vertx.java.core.http.WebSocket]): org.vertx.java.core.http.HttpClient = ???
  def delete(x$1: String, x$2: org.vertx.java.core.Handler[org.vertx.java.core.http.HttpClientResponse]): org.vertx.java.core.http.HttpClientRequest = ???
  def exceptionHandler(x$1: org.vertx.java.core.Handler[Throwable]): org.vertx.java.core.http.HttpClient = ???
  def get(x$1: String, x$2: org.vertx.java.core.Handler[org.vertx.java.core.http.HttpClientResponse]): org.vertx.java.core.http.HttpClientRequest = ???
  def getConnectTimeout(): Int = ???
  def getHost(): String = ???
  def getMaxPoolSize(): Int = ???
  def getNow(x$1: String, x$2: org.vertx.java.core.MultiMap, x$3: org.vertx.java.core.Handler[org.vertx.java.core.http.HttpClientResponse]): org.vertx.java.core.http.HttpClient = ???
  def getNow(x$1: String, x$2: org.vertx.java.core.Handler[org.vertx.java.core.http.HttpClientResponse]): org.vertx.java.core.http.HttpClient = ???
  def getPort(): Int = ???
  def head(x$1: String, x$2: org.vertx.java.core.Handler[org.vertx.java.core.http.HttpClientResponse]): org.vertx.java.core.http.HttpClientRequest = ???
  def isKeepAlive(): Boolean = ???
  def isVerifyHost(): Boolean = ???
  def options(x$1: String, x$2: org.vertx.java.core.Handler[org.vertx.java.core.http.HttpClientResponse]): org.vertx.java.core.http.HttpClientRequest = ???
  def patch(x$1: String, x$2: org.vertx.java.core.Handler[org.vertx.java.core.http.HttpClientResponse]): org.vertx.java.core.http.HttpClientRequest = ???
  def post(x$1: String, x$2: org.vertx.java.core.Handler[org.vertx.java.core.http.HttpClientResponse]): org.vertx.java.core.http.HttpClientRequest = ???
  def put(x$1: String, x$2: org.vertx.java.core.Handler[org.vertx.java.core.http.HttpClientResponse]): org.vertx.java.core.http.HttpClientRequest = ???
  def request(x$1: String, x$2: String, x$3: org.vertx.java.core.Handler[org.vertx.java.core.http.HttpClientResponse]): org.vertx.java.core.http.HttpClientRequest = ???
  def setConnectTimeout(x$1: Int): org.vertx.java.core.http.HttpClient = ???
  def setHost(x$1: String): org.vertx.java.core.http.HttpClient = ???
  def setKeepAlive(x$1: Boolean): org.vertx.java.core.http.HttpClient = ???
  def setMaxPoolSize(x$1: Int): org.vertx.java.core.http.HttpClient = ???
  def setPort(x$1: Int): org.vertx.java.core.http.HttpClient = ???
  def setVerifyHost(x$1: Boolean): org.vertx.java.core.http.HttpClient = ???
  def trace(x$1: String, x$2: org.vertx.java.core.Handler[org.vertx.java.core.http.HttpClientResponse]): org.vertx.java.core.http.HttpClientRequest = ???

}