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
import org.vertx.java.core.{Handler => JHandler, AsyncResult => JAsyncResult, ServerTCPSupport, ServerSSLSupport}

/**
 * @author swilliams
 * 
 */
object NetServer {
  def apply(actual: JNetServer) = new NetServer(actual)

}

class NetServer(val internal: JNetServer) extends ServerSSLSupport[NetServer] with ServerTCPSupport[NetServer] {


  def connectHandler(handler: (NetSocket) => Unit):NetServer = {
    internal.connectHandler(ConnectHandler(handler))
    this
  }

  def listen(port: Int):NetServer = {
    internal.listen(port)
    this
  }


  def listen(port: Int, host: String):NetServer = {
    internal.listen(port, host)
    this
  }

  def listen(port: Int, handler: JAsyncResult[JNetServer] => Unit):NetServer = {
    internal.listen(port,  new JHandler[JAsyncResult[JNetServer]]() {
       override def handle(result: JAsyncResult[JNetServer]) = {
         handler(result)
       }
    })
     this
  }

  def listen(port: Int, host:String,  handler: JAsyncResult[JNetServer] => Unit):NetServer = {
    internal.listen(port,  host, new JHandler[JAsyncResult[JNetServer]]() {
       override def handle(result: JAsyncResult[JNetServer]) = {
         handler(result)
       }
    })
     this
  }


  def close:Unit = internal.close

  def close(handler: () => Unit):Unit = 
    internal.close(handler)

  def port:Int={
    internal.port
  }

  def host:String={
    internal.host
  }

  def setTCPNoDelay(tcpNoDelay: Boolean): NetServer = {
    internal.setTCPNoDelay(tcpNoDelay)
    this
  }

  def setSendBufferSize(size: Int): NetServer = {
    internal.setSendBufferSize(size)
    this
  }

  def setReceiveBufferSize(size: Int): NetServer = {
    internal.setReceiveBufferSize(size)
    this
  }

  def setTCPKeepAlive(keepAlive: Boolean): NetServer = {
    internal.setTCPKeepAlive(keepAlive)
    this
  }

  def setReuseAddress(reuse: Boolean): NetServer = {
    internal.setReuseAddress(reuse)
    this
  }

  def setSoLinger(linger: Int): NetServer = {
    internal.setSoLinger(linger)
    this
  }

  def setTrafficClass(trafficClass: Int): NetServer = {
    internal.setTrafficClass(trafficClass)
    this
  }

  def setUsePooledBuffers(pooledBuffers: Boolean): NetServer = {
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

  def setAcceptBacklog(backlog: Int): NetServer = {
    internal.setAcceptBacklog(backlog)
    this
  }

  def getAcceptBacklog: Int = {
    internal.getAcceptBacklog
  }

  def setSSL(ssl: Boolean): NetServer = {
    internal.setSSL(ssl)
    this
  }

  def isSSL: Boolean = {
    internal.isSSL
  }

  def setKeyStorePath(path: String): NetServer = {
    internal.setKeyStorePath(path)
    this
  }

  def getKeyStorePath: String = {
    internal.getKeyStorePath
  }

  def setKeyStorePassword(pwd: String): NetServer = {
    internal.setKeyStorePassword(pwd)
    this
  }

  def getKeyStorePassword: String = {
    internal.getKeyStorePassword
  }

  def setTrustStorePath(path: String): NetServer = {
    internal.setTrustStorePath(path)
    this
  }

  def getTrustStorePath: String = {
    internal.getTrustStorePath
  }

  def setTrustStorePassword(pwd: String): NetServer = {
    internal.setTrustStorePassword(pwd)
    this
  }

  def getTrustStorePassword: String = {
    internal.getTrustStorePassword
  }

  def setClientAuthRequired(required: Boolean): NetServer = {
    internal.setClientAuthRequired(required)
    this
  }

  def isClientAuthRequired: Boolean = {
    internal.isClientAuthRequired
  }
}