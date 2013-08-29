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

import org.vertx.java.core.{ Handler => JHandler, AsyncResult => JAsyncResult }
import org.vertx.java.core.http.{ HttpServer => JHttpServer }
import org.vertx.java.core.http.{ HttpServerRequest => JHttpServerRequest }
import org.vertx.java.core.http.{ ServerWebSocket => JServerWebSocket }
import org.vertx.scala.core.FunctionConverters._
import org.vertx.scala.core.net.SocketConfigurer
import org.vertx.scala.Wrap

/**
 * @author swilliams
 *
 */
object HttpServer {
  def apply(actual: JHttpServer) =
    new HttpServer(actual)
}

class HttpServer(val actual: JHttpServer) extends SocketConfigurer[HttpServer] with Wrap {

  def close(): Unit = actual.close

  def close(handler: () => Unit): Unit = actual.close(handler)

  def listen(port: Int): HttpServer = wrap(actual.listen(port))

  def listen(port: Int, handler: JAsyncResult[JHttpServer] => Unit): HttpServer = {
    wrap(actual.listen(port, handler))
  }

  def listen(port: Int, address: String, handler: JAsyncResult[JHttpServer] => Unit): HttpServer = {
    wrap(actual.listen(port, address, handler))
  }

  def listen(port: Int, address: String): HttpServer = wrap(actual.listen(port, address))

  def requestHandler(): JHandler[JHttpServerRequest] = {
    actual.requestHandler
  }

  def requestHandler(handler: HttpServerRequest => Unit): HttpServer = {
    wrap(actual.requestHandler(handler.compose(HttpServerRequest.apply)))
  }

  def websocketHandler(): JHandler[JServerWebSocket] = actual.websocketHandler

  def websocketHandler(handler: ServerWebSocket => Unit): HttpServer = {
    wrap(actual.websocketHandler(handler.compose(ServerWebSocket.apply)))
  }

  def acceptBacklog(): Int = actual.getAcceptBacklog()

  def acceptBacklog(backlog: Int): HttpServer = wrap(actual.setAcceptBacklog(backlog))

  def keyStorePassword(): String = actual.getKeyStorePassword

  def keyStorePassword(keyStorePassword: String): HttpServer = {
    wrap(actual.setKeyStorePassword(keyStorePassword))
  }

  def keyStorePath(): String = actual.getKeyStorePath

  def keyStorePath(keyStorePath: String): HttpServer = wrap(actual.setKeyStorePath(keyStorePath))

  def receiveBufferSize(): Int = actual.getReceiveBufferSize

  def receiveBufferSize(receiveBufferSize: Int): HttpServer = {
    wrap(actual.setReceiveBufferSize(receiveBufferSize))
  }

  def sendBufferSize(): Int = actual.getSendBufferSize

  def sendBufferSize(sendBufferSize: Int): HttpServer = {
    wrap(actual.setSendBufferSize(sendBufferSize))
  }

  def trafficClass(): Int = actual.getTrafficClass

  def trafficClass(trafficClass: Int): HttpServer = {
    wrap(actual.setTrafficClass(trafficClass))
  }

  def trustStorePassword(): String = actual.getTrustStorePassword

  def trustStorePassword(password: String): HttpServer = {
    wrap(actual.setTrustStorePassword(password))
  }

  def trustStorePath(): String = actual.getTrustStorePath

  def trustStorePath(path: String): HttpServer = wrap(actual.setTrustStorePath(path))

}