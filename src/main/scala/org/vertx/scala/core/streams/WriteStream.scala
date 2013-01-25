package org.vertx.scala.core.streams

import org.vertx.java.core.buffer.Buffer

trait WriteStream {

  def drainHandler(handler: () => Unit):WriteStream.this.type

  def exceptionHandler(handler: (Exception) => Unit):WriteStream.this.type

  def setWriteQueueMaxSize(maxSize: Int):WriteStream.this.type

  def writeBuffer(data: Buffer):WriteStream.this.type

  def writeQueueFull():Boolean

}