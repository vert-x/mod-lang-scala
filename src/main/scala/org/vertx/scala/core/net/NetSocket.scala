package org.vertx.scala.core.net

import collection.JavaConversions._
import org.vertx.java.core.net.{NetSocket => JNetSocket}
import org.vertx.java.core.Handler
import org.vertx.java.core.buffer.Buffer
import org.vertx.scala.handlers.FunctionHandler1
import org.vertx.scala.handlers.FunctionHandler0

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