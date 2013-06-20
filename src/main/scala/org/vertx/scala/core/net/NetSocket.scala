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

import org.vertx.java.core.net.{NetSocket => JNetSocket}
import org.vertx.java.core.buffer.Buffer
import java.net.InetSocketAddress
import org.vertx.java.core.Handler
import org.vertx.java.core.streams.{WriteStream, ReadStream}
import org.vertx.scala.core.FunctionConverters._

/**
 * @author swilliams
 * 
 */
object NetSocket {
  def apply(socket: JNetSocket) = new NetSocket(socket)
}

class NetSocket(val internal: JNetSocket) extends ReadStream[NetSocket] with WriteStream[NetSocket]{

  def writeHandlerID():String = {
    internal.writeHandlerID
  }

  def write(data: Buffer):NetSocket = {
    internal.write(data)
    this
  }

  def setWriteQueueMaxSize(maxSize:Int):NetSocket={
    internal.setWriteQueueMaxSize(maxSize)
    this
  }


  def write(data: String):NetSocket = {
    internal.write(data)
    this
  }

  def write(data: String, enc:String):NetSocket = {
    internal.write(data, enc)
    this
  }


  def sendFile(filename: String):NetSocket= {
    internal.sendFile(filename)
    this
  }

  def remoteAddress():InetSocketAddress={
    internal.remoteAddress()
  }

  def localAddress():InetSocketAddress={
    internal.localAddress()
  }

  def close():Unit={
    internal.close()
  }



  def dataHandler(buffer: Buffer => Unit):Unit = {
    internal.dataHandler(buffer)
  }

  def endHandler(handler: () => Unit):Unit = {
    internal.endHandler(handler)
  }


  def pause():NetSocket={
    internal.pause()
    this
  }

  def resume():NetSocket={
    internal.resume()
    this
  }



  def exceptionHandler(handler: Handler[Throwable]):NetSocket={
    internal.exceptionHandler(handler )
    this
  }

  //def closeHandler(handler: () => Unit):NetSocket ={
  def closeHandler(handler: Handler[Void]):NetSocket ={
    internal.closeHandler(handler)
    this
  }

  //def dataHandler(buffer: Buffer => Unit):Unit = {
  def dataHandler(buffer: Handler[Buffer]):NetSocket = {
    internal.dataHandler(buffer)
    this
  }

  //def endHandler(handler: () => Unit):NetSocket = {
  def endHandler(handler: Handler[Void]):NetSocket = {
    internal.endHandler(handler)
    this
  }

  //def drainHandler(handler: () => Unit):NetSocket = {
  def drainHandler(handler: Handler[Void]):NetSocket = {
    internal.drainHandler(handler)
    this
  }

  def writeQueueFull:Boolean = {
    internal.writeQueueFull()
  }

}