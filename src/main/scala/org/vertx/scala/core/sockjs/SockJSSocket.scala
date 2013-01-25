package org.vertx.scala.core.sockjs

import org.vertx.java.core.sockjs.{SockJSSocket => JSockJSSocket}
import org.vertx.scala.core.streams.ReadStream
import org.vertx.scastreams.la.core.WriteStream
import org.vertx.scala.handlers.FunctionHandler0
import org.vertx.scala.handlers.FunctionHandler1
import org.vertx.java.core.buffer.Buffer
import org.vertx.java.core.sockjs.{SockJSSocket => JSockJSSocket}

object SockJSSocket {
  def apply(internal: JSockJSSocket) = 
    new SockJSSocket(internal)
}

class SockJSSocket(internal: JSockJSSocket) extends ReadStream with WriteStream {

  def dataHandler(handler: (Buffer) => Unit):SockJSSocket = {
    internal.dataHandler(new FunctionHandler1(handler))
    this
  }

  def drainHandler(handler: () => Unit):SockJSSocket = {
    internal.drainHandler(new FunctionHandler0(handler))
    this
  }

  def endHandler(handler: () => Unit):SockJSSocket = {
    internal.endHandler(new FunctionHandler0(handler))
    this
  }

  def exceptionHandler(handler: (Exception) => Unit):SockJSSocket = {
    internal.exceptionHandler(new FunctionHandler1(handler))
    this
  }

  def pause():Unit = internal.pause()

  def resume():Unit = internal.resume()

  def setWriteQueueMaxSize(maxSize: Int):SockJSSocket = {
    internal.setWriteQueueMaxSize(maxSize)
    this
  }

  def writeBuffer(data: Buffer):SockJSSocket = {
    internal.writeBuffer(data)
    this
  }

  def writeQueueFull():Boolean = internal.writeQueueFull()

}