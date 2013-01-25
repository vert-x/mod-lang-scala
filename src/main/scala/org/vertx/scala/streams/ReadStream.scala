package org.vertx.scala.streams

import org.vertx.java.core.buffer.Buffer
import org.vertx.java.core.streams.{ReadStream => JReadStream}
import org.vertx.scala.handlers.FunctionHandler1
import org.vertx.scala.handlers.FunctionHandler0

// TODO can we place the actual code here to reduce duplication?
trait ReadStream {

  def dataHandler(handler: (Buffer) => Unit):ReadStream.this.type

  def endHandler(handler: () => Unit):ReadStream.this.type

  def exceptionHandler(handler: (Exception) => Unit):ReadStream.this.type

  def pause():ReadStream.this.type

  def resume():ReadStream.this.type

}