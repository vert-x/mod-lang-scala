/*
 * Copyright 2013 the original author or authors.
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
 * @author Edgar Chan
 * 
 */
object NetClient {
  def apply(actual: JNetClient) =
    new NetClient(actual)
}

class NetClient(internal: JNetClient) extends ClientSSLSupport[NetClient] with TCPSupport[NetClient]{

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

  def keyStorePath(keyStorePath: String): NetClient = {
    internal.setKeyStorePath(keyStorePath)
    this
  }

  def reconnectAttempts():Int = internal.getReconnectAttempts

  def reconnectInterval():Long = internal.getReconnectInterval

  def receiveBufferSize():Int = internal.getReceiveBufferSize

  def receiveBufferSize(receiveBufferSize: Int):NetClient = {
    internal.setReceiveBufferSize(receiveBufferSize)
    this
  }

  def sendBufferSize(): Int = internal.getSendBufferSize

  def sendBufferSize(sendBufferSize: Int): NetClient= {
    internal.setSendBufferSize(sendBufferSize)
    this
  }

  def trafficClass(): Int = internal.getTrafficClass

  def trafficClass(trafficClass: Int): NetClient= {
    internal.setTrafficClass(trafficClass)
    this
  }

  def trustStorePassword(): String = internal.getTrustStorePassword

  def trustStorePassword(password: String): NetClient= {
    internal.setTrustStorePassword(password)
    this
  }

  def trustStorePath(): String = internal.getTrustStorePath

  def trustStorePath(path: String): NetClient = {
    internal.setTrustStorePath(path)
    this
  }

  def setSSL(ssl: Boolean): NetClient = {
    internal.setSSL(ssl)
    this
  }

  def isSSL: Boolean = {
    internal.isSSL
  }

  def setKeyStorePath(path: String): NetClient = {
    internal.setKeyStorePassword(path)
    this
  }

  def getKeyStorePath: String = {
    internal.getKeyStorePassword
  }

  def setKeyStorePassword(pwd: String): NetClient = {
    internal.setKeyStorePassword(pwd)
    this
  }

  def getKeyStorePassword: String = {
    internal.getKeyStorePassword
  }

  def setTrustStorePath(path: String): NetClient = {
    internal.setKeyStorePath(path)
    this
  }

  def getTrustStorePath: String = {
    internal.getTrustStorePassword
  }

  def setTrustStorePassword(pwd: String): NetClient = {
    internal.setTrustStorePassword(pwd)
    this
  }

  def getTrustStorePassword: String = {
    internal.getTrustStorePassword
  }

  def setTrustAll(trustAll: Boolean): NetClient = {
    internal.setTrustAll(trustAll)
    this
  }

  def isTrustAll: Boolean = {
    internal.isTrustAll
  }

  def setTCPNoDelay(tcpNoDelay: Boolean): NetClient = {
    internal.isTCPNoDelay
    this
  }

  def setSendBufferSize(size: Int): NetClient = {
    internal.setSendBufferSize(size)
    this
  }

  def setReceiveBufferSize(size: Int): NetClient = {
    internal.setReceiveBufferSize(size)
    this
  }

  def setTCPKeepAlive(keepAlive: Boolean): NetClient = {
    internal.setTCPKeepAlive(keepAlive)
    this
  }

  def setReuseAddress(reuse: Boolean): NetClient = {
    internal.setReuseAddress(reuse)
    this
  }

  def setSoLinger(linger: Int): NetClient = {
    internal.setSoLinger(linger)
    this
  }

  def setTrafficClass(trafficClass: Int): NetClient = {
    internal.setTrafficClass(trafficClass)
    this
  }

  def setUsePooledBuffers(pooledBuffers: Boolean): NetClient = {
    internal.setUsePooledBuffers(pooledBuffers)
    this
  }

  def isTCPNoDelay: Boolean = {
    internal.isTCPNoDelay
  }

  def getSendBufferSize: Int = {
    internal.getSendBufferSize
  }

  def getReceiveBufferSize: Int = {
    internal.getReceiveBufferSize
  }

  def isTCPKeepAlive: Boolean = {
    internal.isTCPKeepAlive
  }

  def isReuseAddress: Boolean = {
    internal.isReuseAddress
  }

  def getSoLinger: Int = {
    internal.getSoLinger
  }

  def getTrafficClass: Int = {
    internal.getTrafficClass
  }

  def isUsePooledBuffers: Boolean = {
    internal.isUsePooledBuffers
  }
}

