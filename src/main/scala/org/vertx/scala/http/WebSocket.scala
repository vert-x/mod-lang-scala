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

package org.vertx.scala.http

import org.vertx.java.core.http.{WebSocket => JWebSocket}
import org.vertx.scala.handlers.FunctionHandler0
import org.vertx.scala.handlers.FunctionHandler1
import org.vertx.java.core.buffer.Buffer
import org.vertx.scala.streams.WriteStream
import org.vertx.scala.streams.ReadStream

/**
 * @author swilliams
 * 
 */
object WebSocket {
  def apply(jsocket: JWebSocket) =
    new WebSocket(jsocket)
}

class WebSocket(internal: JWebSocket) extends ReadStream with WriteStream {

  def binaryHandlerID():String = internal.binaryHandlerID

  def pause():WebSocket.this.type = {
    internal.pause()
    this
  }

  def resume():WebSocket.this.type = {
    internal.resume()
    this
  }

  def textHandlerID():String = internal.textHandlerID

  def close():Unit = internal.close()

  def setWriteQueueMaxSize(maxSize: Int):WebSocket.this.type = {
    internal.setWriteQueueMaxSize(maxSize)
    this
  }

  def writeBinaryFrame(data: Buffer):WebSocket.this.type = {
    internal.writeBinaryFrame(data)
    this
  }

  def writeBuffer(data: Buffer):WebSocket.this.type = {
    internal.writeBuffer(data)
    this
  }

  def writeTextFrame(data: String):WebSocket.this.type = {
    internal.writeTextFrame(data)
    this
  }

  def writeQueueFull():Boolean = internal.writeQueueFull()

  def closeHandler(handler: () => Unit):WebSocket.this.type = {
    internal.closedHandler(FunctionHandler0(handler))
    this
  }

  def dataHandler(handler: (Buffer) => Unit):WebSocket.this.type = {
    internal.dataHandler(FunctionHandler1(handler))
    this
  }

  def drainHandler(handler: () => Unit):WebSocket.this.type = {
    internal.drainHandler(FunctionHandler0(handler))
    this
  }

  def endHandler(handler: () => Unit):WebSocket.this.type = {
    internal.endHandler(FunctionHandler0(handler))
    this
  }

  def exceptionHandler(handler: (Exception) => Unit):WebSocket.this.type = {
    internal.exceptionHandler(FunctionHandler1(handler))
    this
  }

}