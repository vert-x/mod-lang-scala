package org.vertx.scala.core.streams

import org.vertx.java.core.buffer.Buffer

trait ReadStream {

  def dataHandler(handler: (Buffer) => Unit):ReadStream.this.type

  def endHandler(handler: () => Unit):ReadStream.this.type

  def exceptionHandler(handler: (Exception) => Unit):ReadStream.this.type

  def pause():Unit

  def resume():Unit

}