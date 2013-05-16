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

package org.vertx.scala.core.http

import org.vertx.java.core.buffer.Buffer
import org.vertx.java.core.http.{WebSocket => JWebSocket}
import org.vertx.scala.core.FunctionConverters._
import org.vertx.scala.core.streams.WriteStream
import org.vertx.scala.core.streams.ReadStream
import org.vertx.java.core.Handler

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
    internal.write(data)
    this
  }

  def writeTextFrame(data: String):WebSocket.this.type = {
    internal.writeTextFrame(data)
    this
  }

  def writeQueueFull():Boolean = internal.writeQueueFull()

  def closeHandler(handler: () => Unit):WebSocket.this.type = {
    internal.closeHandler(handler)
    this
  }

  def dataHandler(handler: (Buffer) => Unit):WebSocket.this.type = {
    internal.dataHandler(handler)
    this
  }

  def drainHandler(handler: () => Unit):WebSocket.this.type = {
    internal.drainHandler(handler)
    this
  }

  def endHandler(handler: () => Unit):WebSocket.this.type = {
    internal.endHandler(handler)
    this
  }

  def exceptionHandler(handler: Handler[Throwable]):WebSocket.this.type = {
    internal.exceptionHandler(handler)
    this
  }

}