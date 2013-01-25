package org.vertx.scala.core.sockjs

import org.vertx.java.core.buffer.Buffer
import org.vertx.java.core.sockjs.{SockJSSocket => JSockJSSocket}
import org.vertx.scala.core.streams.ReadStream
import org.vertx.scala.handlers.FunctionHandler0
import org.vertx.scala.handlers.FunctionHandler1
import org.vertx.scala.core.streams.WriteStream

object SockJSSocket {
  def apply(internal: JSockJSSocket) = 
    new SockJSSocket(internal)
}

class SockJSSocket(internal: JSockJSSocket) extends ReadStream with WriteStream {

  def dataHandler(handler: (Buffer) => Unit): SockJSSocket.this.type = {
    internal.dataHandler(new FunctionHandler1(handler))
    this
  }

  def drainHandler(handler: () => Unit): SockJSSocket.this.type = {
    internal.drainHandler(new FunctionHandler0(handler))
    this
  }

  def endHandler(handler: () => Unit): SockJSSocket.this.type = {
    internal.endHandler(new FunctionHandler0(handler))
    this
  }

  def exceptionHandler(handler: (Exception) => Unit): SockJSSocket.this.type = {
    internal.exceptionHandler(new FunctionHandler1(handler))
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

  def writeBuffer(data: Buffer): SockJSSocket.this.type = {
    internal.writeBuffer(data)
    this
  }

  def writeQueueFull():Boolean = internal.writeQueueFull()

}