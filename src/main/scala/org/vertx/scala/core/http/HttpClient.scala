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
import org.vertx.java.core.http.{ HttpClientResponse => JHttpClientResponse }
import org.vertx.java.core.http.{ WebSocketVersion => JWebSocketVersion }
import org.vertx.scala.core.net.ClientConfigurer
import org.vertx.scala.core.FunctionConverters._
import org.vertx.java.core.{ MultiMap => JMultiMap }

/**
 * @author swilliams
 * @author Galder Zamarreño
 */
object HttpClient {
  def apply(actual: JHttpClient) =
    new HttpClient(actual)
}

class HttpClient(internal: JHttpClient) extends ClientConfigurer[HttpClient] {

  def connect(uri: String, handler: HttpClientResponse => Unit): Unit = {
    internal.connect(uri, handler.compose(HttpClientResponse.apply))
  }

  def connectWebsocket(uri: String)(wsConnect: WebSocket => Unit) {
    internal.connectWebsocket(uri, wsConnect.compose(WebSocket.apply))
  }

  def connectWebsocket(uri: String, wsVersion: JWebSocketVersion)(wsConnect: WebSocket => Unit) {
    internal.connectWebsocket(uri, wsVersion, wsConnect.compose(WebSocket.apply))
  }

  def delete(uri: String, handler: HttpClientResponse => Unit): Unit = {
    internal.delete(uri, handler.compose(HttpClientResponse.apply))
  }

  def exceptionHandler(handler: (Throwable) => Unit): Unit = {
    internal.exceptionHandler(handler)
  }

  def get(uri: String, handler: HttpClientResponse => Unit): Unit = {
    internal.get(uri, handler.compose(HttpClientResponse.apply))
  }

  def getNow(uri: String, headers: JMultiMap, handler: HttpClientResponse => Unit): Unit = {
    internal.getNow(uri, headers, handler.compose(HttpClientResponse.apply))
  }

  def getNow(uri: String)(handler: HttpClientResponse => Unit): Unit = {
    internal.getNow(uri, handler.compose(HttpClientResponse.apply))
  }

  def head(uri: String, handler: HttpClientResponse => Unit): Unit = {
    internal.head(uri, handler.compose(HttpClientResponse.apply))
  }

  def options(uri: String, handler: HttpClientResponse => Unit): Unit = {
    internal.options(uri, handler.compose(HttpClientResponse.apply))
  }

  def patch(uri: String, handler: HttpClientResponse => Unit): Unit = {
    internal.patch(uri, handler.compose(HttpClientResponse.apply))
  }

  def post(uri: String, handler: HttpClientResponse => Unit): Unit = {
    internal.post(uri, handler.compose(HttpClientResponse.apply))
  }

  def put(uri: String, handler: HttpClientResponse => Unit): Unit = {
    internal.put(uri, handler.compose(HttpClientResponse.apply))
  }

  def trace(uri: String, handler: HttpClientResponse => Unit): Unit = {
    internal.trace(uri, handler.compose(HttpClientResponse.apply))
  }

  def request(method: String, uri: String, handler: HttpClientResponse => Unit): Unit = {
    internal.request(method, uri, handler.compose(HttpClientResponse.apply))
  }

  def close(): Unit = internal.close()

  def connectTimeout(): Long = internal.getConnectTimeout()

  def keyStorePassword(): String = internal.getKeyStorePassword()

  def keyStorePath(): String = internal.getKeyStorePath()

  def keyStorePassword(keyStorePassword: String): HttpClient = {
    internal.setKeyStorePassword(keyStorePassword)
    this
  }

  def keyStorePath(keyStorePath: String): HttpClient = {
    internal.setKeyStorePath(keyStorePath)
    this
  }

  def maxPoolSize(): Int = internal.getMaxPoolSize()

  def receiveBufferSize(): Int = internal.getReceiveBufferSize()

  def receiveBufferSize(receiveBufferSize: Int): HttpClient = {
    internal.setReceiveBufferSize(receiveBufferSize)
    this
  }

  def sendBufferSize(): Int = internal.getSendBufferSize()

  def sendBufferSize(sendBufferSize: Int): HttpClient = {
    internal.setSendBufferSize(sendBufferSize)
    this
  }

  def trafficClass(): Int = internal.getTrafficClass()

  def trafficClass(trafficClass: Int): HttpClient = {
    internal.setTrafficClass(trafficClass)
    this
  }

  def trustStorePassword(): String = internal.getTrustStorePassword()

  def trustStorePassword(password: String): HttpClient = {
    internal.setTrustStorePassword(password)
    this
  }

  def trustStorePath(): String = internal.getTrustStorePath()

  def trustStorePath(path: String): HttpClient = {
    internal.setTrustStorePath(path)
    this
  }

  def setPort(port: Int) = {
    internal.setPort(port)
    this
  }

}