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
import org.vertx.java.core.{Handler => JHandler, AsyncResult => JAsyncResult}

/**
 * @author swilliams
 * 
 */
object NetServer {
  def apply(actual: JNetServer) = new NetServer(actual)

}

class NetServer(val internal: JNetServer) extends SocketConfigurer {

  def close():Unit = internal.close

  def close(handler: () => Unit):Unit = 
    internal.close(handler)

  def listen(port: Int):NetServer.this.type = {
    internal.listen(port)
    this
  }

  def listen(port: Int, address: String):NetServer.this.type = {
    internal.listen(port, address)
    this
  }


  def listen(port: Int, handler: JAsyncResult[JNetServer] => Unit):NetServer.this.type = {
    internal.listen(port,  new JHandler[JAsyncResult[JNetServer]]() {
       override def handle(result: JAsyncResult[JNetServer]) = {
         handler(result)
       }
    })
     this
  }


  def connectHandler(handler: (NetSocket) => Unit):NetServer.this.type = {
    internal.connectHandler(ConnectHandler(handler))
    this
  }

  def acceptBacklog():Int = internal.getAcceptBacklog

  def acceptBacklog(backlog: Int):NetServer.this.type = {
    internal.setAcceptBacklog(backlog)
    this
  }

  def keyStorePassword():String = internal.getKeyStorePassword

  def keyStorePassword(keyStorePassword: String):NetServer.this.type = {
    internal.setKeyStorePassword(keyStorePassword)
    this
  }

  def keyStorePath():String = internal.getKeyStorePath

  def keyStorePath(keyStorePath: String):NetServer.this.type = {
    internal.setKeyStorePath(keyStorePath)
    this
  }

  def receiveBufferSize():Int = internal.getReceiveBufferSize

  def receiveBufferSize(receiveBufferSize: Int):NetServer.this.type = {
    internal.setReceiveBufferSize(receiveBufferSize)
    this
  }

  def sendBufferSize():Int = internal.getSendBufferSize

  def sendBufferSize(sendBufferSize: Int):NetServer.this.type = {
    internal.setSendBufferSize(sendBufferSize)
    this
  }

  def trafficClass():Int = internal.getTrafficClass

  def trafficClass(trafficClass: Int):NetServer.this.type = {
    internal.setTrafficClass(trafficClass)
    this
  }

  def trustStorePassword():String = internal.getTrustStorePassword

  def trustStorePassword(password: String):NetServer.this.type = {
    internal.setTrustStorePassword(password)
    this
  }

  def trustStorePath():String = internal.getTrustStorePath

  def trustStorePath(path: String):NetServer.this.type = {
    internal.setTrustStorePath(path)
    this
  }
}