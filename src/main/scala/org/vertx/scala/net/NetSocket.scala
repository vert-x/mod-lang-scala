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

package org.vertx.scala.net

import collection.JavaConversions._
import org.vertx.java.core.net.{NetSocket => JNetSocket}
import org.vertx.java.core.buffer.Buffer
import org.vertx.java.core.net.{NetSocket => JNetSocket}
import org.vertx.scala.handlers.FunctionHandler1
import org.vertx.scala.handlers.FunctionHandler0

/**
 * @author swilliams
 * 
 */
object NetSocket {
  def apply(socket: JNetSocket) =
    new NetSocket(socket)
}

class NetSocket (internal: JNetSocket) {

  def dataHandler(buffer: (Buffer) => Unit):Unit = {
    internal.dataHandler(FunctionHandler1(buffer))
  }

  def drainHandler(handler: () => Unit):Unit = {
    internal.drainHandler(FunctionHandler0(handler))
  }

  def endHandler(handler: () => Unit):Unit = {
    internal.endHandler(FunctionHandler0(handler))
  }

  def sendFile(filename: String):Unit = {
    internal.sendFile(filename)
  }

  def write(data: Buffer):NetSocket = {
    internal.write(data)
    this
  }

  def write(data: Buffer, handler: () => Unit):NetSocket = {
    internal.write(data, FunctionHandler0(handler))
    this
  }

  def write(data: String):NetSocket = {
    internal.write(data)
    this
  }

  def write(data: String, handler: () => Unit):NetSocket = {
    internal.write(data, FunctionHandler0(handler))
    this
  }

  def write(data: String, enc: String):NetSocket = {
    internal.write(data, enc)
    this
  }

  def writeBuffer(data: Buffer):Unit = {
    internal.writeBuffer(data)
  }

  def writeHandlerID():String = {
    internal.writeHandlerID
  }

  def writeQueueFull():Boolean = {
    internal.writeQueueFull()
  }

}