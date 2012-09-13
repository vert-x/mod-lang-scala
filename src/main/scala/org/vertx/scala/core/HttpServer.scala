/*
 * Copyright 2011-2012 the original author or authors.
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

package org.vertx.scala.core

import org.vertx.scala.deploy.FunctionHandler0
import org.vertx.scala.deploy.FunctionHandler1
import org.vertx.java.core.http.HttpServerRequest
import org.vertx.java.core.http.ServerWebSocket

class HttpServer(internal: org.vertx.java.core.http.HttpServer) {

  def internal():org.vertx.java.core.http.HttpServer = {
    internal
  }

  def close():Unit = {
    internal.close()
  }

  def close(handler: () => Unit):Unit = {
    internal.close(new FunctionHandler0(handler))
  }

  def listen(port: Int):HttpServer = {
    internal.listen(port)
    this
  }

  def listen(port: Int, address: String):HttpServer = {
    internal.listen(port, address)
    this
  }

  def requestHandler():Unit = {
    (e: HttpServerRequest) => internal.requestHandler().handle(e)
  }

  def requestHandler(handler: (HttpServerRequest) => Unit):HttpServer = {
    internal.requestHandler(new FunctionHandler1(handler))
    this
  }

  def websocketHandler():Unit = {
    (s: ServerWebSocket) => internal.websocketHandler().handle(s)
  }

  def websocketHandler(handler: (ServerWebSocket) => Unit):HttpServer = {
    internal.websocketHandler(new FunctionHandler1(handler))
    this
  }

  def acceptBacklog():java.lang.Integer = {
    internal.getAcceptBacklog()
  }

  def acceptBacklog(backlog: Int):HttpServer = {
    internal.setAcceptBacklog(backlog)
    this
  }

  def keyStorePassword():String = {
    internal.getKeyStorePassword()
  }

  def keyStorePassword(keyStorePassword: String):HttpServer = {
    internal.setKeyStorePassword(keyStorePassword)
    this
  }

  def keyStorePath():String = {
    internal.getKeyStorePath()
  }

  def keyStorePath(keyStorePath: String):HttpServer = {
    internal.setKeyStorePath(keyStorePath)
    this
  }

  def receiveBufferSize():java.lang.Integer = {
    internal.getReceiveBufferSize()
  }

  def receiveBufferSize(receiveBufferSize: Int):HttpServer = {
    internal.setReceiveBufferSize(receiveBufferSize)
    this
  }

  def sendBufferSize():java.lang.Integer = {
    internal.getSendBufferSize()
  }

  def sendBufferSize(sendBufferSize: Int):HttpServer = {
    internal.setSendBufferSize(sendBufferSize)
    this
  }

  def trafficClass():java.lang.Integer = {
    internal.getTrafficClass()
  }

  def trafficClass(trafficClass: Int):HttpServer = {
    internal.setTrafficClass(trafficClass)
    this
  }

  def trustStorePassword():String = {
    internal.getTrustStorePassword()
  }

  def trustStorePassword(password: String):HttpServer = {
    internal.setTrustStorePassword(password)
    this
  }

  def trustStorePath():String = {
    internal.getTrustStorePath()
  }

  def trustStorePath(path: String):HttpServer = {
    internal.setTrustStorePath(path)
    this
  }

}