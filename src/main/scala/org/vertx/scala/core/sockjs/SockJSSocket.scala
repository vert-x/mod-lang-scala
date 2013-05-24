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

package org.vertx.scala.core.sockjs

import org.vertx.java.core.buffer.Buffer
import org.vertx.java.core.sockjs.{SockJSSocket => JSockJSSocket}
import org.vertx.scala.core.FunctionConverters._
import org.vertx.scala.core.streams.ReadStream
import org.vertx.scala.core.streams.WriteStream

/**
 * @author swilliams
 * 
 */
object SockJSSocket {
  def apply(internal: JSockJSSocket) = 
    new SockJSSocket(internal)
}

class SockJSSocket(internal: JSockJSSocket) extends ReadStream with WriteStream {

  def dataHandler(handler: Buffer => Unit): SockJSSocket.this.type = {
    internal.dataHandler(handler)
    this
  }

  def drainHandler(handler: () => Unit): SockJSSocket.this.type = {
    internal.drainHandler(handler)
    this
  }

  def endHandler(handler: () => Unit): SockJSSocket.this.type = {
    internal.endHandler(handler)
    this
  }

  def exceptionHandler(handler: Throwable => Unit): SockJSSocket.this.type = {
    internal.exceptionHandler(handler)
    this
  }

  def pause(): SockJSSocket.this.type = {
    internal.pause()
    this
  }

  def resume(): SockJSSocket.this.type = {
    internal.resume()
    this
  }

  def setWriteQueueMaxSize(maxSize: Int): SockJSSocket.this.type = {
    internal.setWriteQueueMaxSize(maxSize)
    this
  }

  def write(data: Buffer): SockJSSocket.this.type = {
    internal.write(data)
    this
  }

  def writeQueueFull():Boolean = internal.writeQueueFull()

}