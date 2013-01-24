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

package org.vertx.scala.core.net

import collection.JavaConversions._
import org.vertx.java.core.net.{NetClient => JNetClient}
import org.vertx.java.core.net.{NetSocket => JNetSocket}
import org.vertx.scala.handlers.FunctionHandler1
import org.vertx.scala.core.ClientConfigurer

object NetClient {
  def apply(actual: JNetClient) =
    new NetClient(actual)
}

class NetClient(internal: JNetClient) extends ClientConfigurer {

  def connect(port: Int, host: String, handler: (JNetSocket) => Unit): Unit = {
    internal.connect(port, host, FunctionHandler1(handler))
  }

  def connect(port: Int, handler: (JNetSocket) => Unit): Unit = {
    internal.connect(port, FunctionHandler1(handler))
  }

  def exceptionHandler(handler: (Exception) => Unit): Unit = {
    internal.exceptionHandler(FunctionHandler1(handler))
  }

  def close(): Unit = internal.close()

  def bossThreads(): Int = internal.getBossThreads()

  def connectTimeout(): Long = internal.getConnectTimeout()

  def keyStorePassword(): String = internal.getKeyStorePassword()

  def keyStorePath(): String = internal.getKeyStorePath()

  def keyStorePassword(keyStorePassword: String): NetClient.this.type = {
    internal.setKeyStorePassword(keyStorePassword)
    this
  }

  def keyStorePath(keyStorePath: String): NetClient.this.type = {
    internal.setKeyStorePath(keyStorePath)
    this
  }

  def reconnectAttempts():Int = internal.getReconnectAttempts()

  def reconnectInterval():Long = internal.getReconnectInterval()

  def receiveBufferSize():Int = internal.getReceiveBufferSize()

  def receiveBufferSize(receiveBufferSize: Int):NetClient.this.type = {
    internal.setReceiveBufferSize(receiveBufferSize)
    this
  }

  def sendBufferSize(): Int = internal.getSendBufferSize()

  def sendBufferSize(sendBufferSize: Int): NetClient.this.type = {
    internal.setSendBufferSize(sendBufferSize)
    this
  }

  def trafficClass(): Int = internal.getTrafficClass()

  def trafficClass(trafficClass: Int): NetClient.this.type = {
    internal.setTrafficClass(trafficClass)
    this
  }

  def trustStorePassword(): String = internal.getTrustStorePassword()

  def trustStorePassword(password: String): NetClient.this.type = {
    internal.setTrustStorePassword(password)
    this
  }

  def trustStorePath(): String = internal.getTrustStorePath()

  def trustStorePath(path: String): NetClient.this.type = {
    internal.setTrustStorePath(path)
    this
  }

}