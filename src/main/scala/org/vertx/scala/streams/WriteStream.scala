package org.vertx.scala.streams

import org.vertx.java.core.buffer.Buffer
import org.vertx.java.core.streams.{WriteStream => JWriteStream}
import org.vertx.scala.handlers.FunctionHandler0
import org.vertx.scala.handlers.FunctionHandler1

trait WriteStream {

  def drainHandler(handler: () => Unit):WriteStream.this.type

  def exceptionHandler(handler: (Exception) => Unit):WriteStream.this.type

  def setWriteQueueMaxSize(maxSize: Int):WriteStream.this.type

  def writeBuffer(data: Buffer):WriteStream.this.type

  def writeQueueFull():Boolean

}