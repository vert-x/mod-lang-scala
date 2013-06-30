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

import org.vertx.scala.core.FunctionConverters._
import org.vertx.java.core.net.{NetServer => JNetServer}
import org.vertx.java.core.{Handler, AsyncResult, ServerTCPSupport, ServerSSLSupport}
import org.vertx.java.core.impl.DefaultFutureResult

/**
 *
 * @author swilliams
 * @author Ranie Jade Ramiso
 *
 */
object NetServer {
  def apply(actual: JNetServer) = new NetServer(actual)

}

class NetServer(val internal: JNetServer) extends ServerSSLSupport[NetServer] with ServerTCPSupport[NetServer] {


  def connectHandler(handler: (NetSocket) => Unit) = {
    internal.connectHandler(ConnectHandler(handler))
    this
  }

  def listen(port: Int) = {
    internal.listen(port)
    this
  }


  def listen(port: Int, host: String) = {
    internal.listen(port, host)
    this
  }

  def listen(port: Int, handler: AsyncResult[NetServer] => Unit): NetServer = {
    listen(port, "0.0.0.0", handler)
  }

  def listen(port: Int, host:String,  handler: AsyncResult[NetServer] => Unit) = {
    internal.listen(port,  host, new Handler[AsyncResult[JNetServer]]() {
       override def handle(result: AsyncResult[JNetServer]) = {
         if (result.succeeded)
             handler(new DefaultFutureResult[NetServer](NetServer(result.result)))
       }
    })
     this
  }


  def close() {
    internal.close
  }

  def close(handler: () => Unit) {
    internal.close(handler)
  }

  def port = internal.port

  def host = internal.host

  def setTCPNoDelay(tcpNoDelay: Boolean) = {
    internal.setTCPNoDelay(tcpNoDelay)
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

  def setAcceptBacklog(backlog: Int) = {
    internal.setAcceptBacklog(backlog)
    this
  }

  def getAcceptBacklog = internal.getAcceptBacklog

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

  def setClientAuthRequired(required: Boolean) = {
    internal.setClientAuthRequired(required)
    this
  }

  def isClientAuthRequired = internal.isClientAuthRequired
}
