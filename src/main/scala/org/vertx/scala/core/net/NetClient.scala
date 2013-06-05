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

package org.vertx.scala.core.net

import org.vertx.java.core.net.{NetClient => JNetClient}
import org.vertx.java.core.net.{NetSocket => JNetSocket}
import org.vertx.java.core.{TCPSupport, ClientSSLSupport, Handler, AsyncResult}
import org.vertx.java.core.impl.DefaultFutureResult



/**
 * @author swilliams
 * 
 */
object NetClient {
  def apply(actual: JNetClient) =
    new NetClient(actual)
}

//TODO: ClientSSLSupport, TCPSupport extends ClientSSLSupport[NetClient] with TCPSupport[NetClient]
class NetClient(internal: JNetClient) {

  def connect(port: Int, handler: AsyncResult[NetSocket] => Unit): NetClient = {
    connect(port, "localhost", handler)
  }

  def connect(port: Int, host: String, handler: AsyncResult[NetSocket] => Unit):NetClient= {
    internal.connect(port, host, new Handler[AsyncResult[JNetSocket]]() {
      override def handle(result: AsyncResult[JNetSocket]) = {
        if (result.succeeded)
           handler(new DefaultFutureResult[NetSocket](NetSocket(result.result)))
      }
    })
    this
  }


  def close(): Unit = internal.close()

  def connectTimeout(): Long = internal.getConnectTimeout

  def keyStorePassword(): String = internal.getKeyStorePassword

  def keyStorePath(): String = internal.getKeyStorePath

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