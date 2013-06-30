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
 *
 * @author swilliams
 * @author Edgar Chan
 * @author Ranie Jade Ramiso
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

  def connect(port: Int, host: String, handler: AsyncResult[NetSocket] => Unit) = {
    internal.connect(port, host, new Handler[AsyncResult[JNetSocket]]() {
      override def handle(result: AsyncResult[JNetSocket]) = {
        if (result.succeeded)
           handler(new DefaultFutureResult[NetSocket](NetSocket(result.result)))
      }
    })
    this
  }

  def close() {
    internal.close
  }

  def getConnectTimeout = internal.getConnectTimeout

  def reconnectAttempts = internal.getReconnectAttempts

  def reconnectInterval = internal.getReconnectInterval


  def setSSL(ssl: Boolean) = {
    internal.setSSL(ssl)
    this
  }

  def isSSL = internal.isSSL

  def setKeyStorePath(path: String) = {
    internal.setKeyStorePath(path)
    this
  }

  def getKeyStorePath = internal.getKeyStorePath

  def setKeyStorePassword(pwd: String) = {
    internal.setKeyStorePassword(pwd)
    this
  }

  def getKeyStorePassword = internal.getKeyStorePassword

  def setTrustStorePath(path: String) = {
    internal.setTrustStorePath(path)
    this
  }

  def getTrustStorePath = internal.getTrustStorePath

  def setTrustStorePassword(pwd: String) = {
    internal.setTrustStorePassword(pwd)
    this
  }

  def getTrustStorePassword = internal.getTrustStorePassword

  def setTrustAll(trustAll: Boolean) = {
    internal.setTrustAll(trustAll)
    this
  }

  def isTrustAll = internal.isTrustAll

  def setTCPNoDelay(tcpNoDelay: Boolean) = {
    internal.isTCPNoDelay
    this
  }

  def setSendBufferSize(size: Int) = {
    internal.setSendBufferSize(size)
    this
  }

  def setReceiveBufferSize(size: Int) = {
    internal.setReceiveBufferSize(size)
    this
  }

  def setTCPKeepAlive(keepAlive: Boolean) = {
    internal.setTCPKeepAlive(keepAlive)
    this
  }

  def setReuseAddress(reuse: Boolean) = {
    internal.setReuseAddress(reuse)
    this
  }

  def setSoLinger(linger: Int) = {
    internal.setSoLinger(linger)
    this
  }

  def setTrafficClass(trafficClass: Int) = {
    internal.setTrafficClass(trafficClass)
    this
  }

  def setUsePooledBuffers(pooledBuffers: Boolean) = {
    internal.setUsePooledBuffers(pooledBuffers)
    this
  }

  def isTCPNoDelay = internal.isTCPNoDelay

  def getSendBufferSize = internal.getSendBufferSize

  def getReceiveBufferSize = internal.getReceiveBufferSize

  def isTCPKeepAlive = internal.isTCPKeepAlive

  def isReuseAddress = internal.isReuseAddress

  def getSoLinger = internal.getSoLinger

  def getTrafficClass = internal.getTrafficClass

  def isUsePooledBuffers = internal.isUsePooledBuffers
}

