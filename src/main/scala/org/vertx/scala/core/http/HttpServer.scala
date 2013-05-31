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

import org.vertx.java.core.{Handler => JHandler, AsyncResult => JAsyncResult}
import org.vertx.java.core.http.{HttpServer => JHttpServer}
import org.vertx.java.core.http.{HttpServerRequest => JHttpServerRequest}
import org.vertx.java.core.http.{ServerWebSocket => JServerWebSocket}
import org.vertx.scala.core.FunctionConverters._
import org.vertx.scala.core.net.SocketConfigurer


/**
 * @author swilliams
 * 
 */
object HttpServer {
  def apply(actual: JHttpServer) =
    new HttpServer(actual)
}

class HttpServer(val actual: JHttpServer) extends SocketConfigurer {

  def close():Unit = actual.close

  def close(handler: () => Unit):Unit = actual.close(handler)

  def listen(port: Int):HttpServer.this.type = {
    actual.listen(port)
    this
  }

  def listen(port: Int, handler: JAsyncResult[JHttpServer] => Unit):HttpServer.this.type = {
    actual.listen(port, new JHandler[JAsyncResult[JHttpServer]]() {
      override def handle(result: JAsyncResult[JHttpServer]) = {
        handler(result)
      }
      })

    this
  }


  def listen(port: Int, address: String):HttpServer.this.type = {
    actual.listen(port, address)
    this
  }

  def requestHandler():JHandler[JHttpServerRequest] = {
    actual.requestHandler
  }

  def requestHandler(handler: HttpServerRequest => Unit):HttpServer.this.type = {
    actual.requestHandler(HttpServerRequestHandler(handler))
    this
  }

  def websocketHandler(): JHandler[JServerWebSocket] = actual.websocketHandler

  def websocketHandler(handler: ServerWebSocket => Unit):HttpServer.this.type = {
    actual.websocketHandler(ServerWebSocketHandler1(handler))
    this
  }

  def acceptBacklog():Int = actual.getAcceptBacklog()

  def acceptBacklog(backlog: Int):HttpServer.this.type = {
    actual.setAcceptBacklog(backlog)
    this
  }

  def keyStorePassword():String = actual.getKeyStorePassword

  def keyStorePassword(keyStorePassword: String):HttpServer.this.type = {
    actual.setKeyStorePassword(keyStorePassword)
    this
  }

  def keyStorePath():String = actual.getKeyStorePath

  def keyStorePath(keyStorePath: String):HttpServer.this.type = {
    actual.setKeyStorePath(keyStorePath)
    this
  }

  def receiveBufferSize():Int = actual.getReceiveBufferSize

  def receiveBufferSize(receiveBufferSize: Int):HttpServer.this.type = {
    actual.setReceiveBufferSize(receiveBufferSize)
    this
  }

  def sendBufferSize():Int = actual.getSendBufferSize

  def sendBufferSize(sendBufferSize: Int):HttpServer.this.type = {
    actual.setSendBufferSize(sendBufferSize)
    this
  }

  def trafficClass():Int = actual.getTrafficClass

  def trafficClass(trafficClass: Int):HttpServer.this.type = {
    actual.setTrafficClass(trafficClass)
    this
  }

  def trustStorePassword():String = actual.getTrustStorePassword

  def trustStorePassword(password: String):HttpServer.this.type = {
    actual.setTrustStorePassword(password)
    this
  }

  def trustStorePath():String = actual.getTrustStorePath

  def trustStorePath(path: String):HttpServer.this.type = {
    actual.setTrustStorePath(path)
    this
  }

}