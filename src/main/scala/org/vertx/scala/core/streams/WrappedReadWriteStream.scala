package org.vertx.scala.core.streams

import org.vertx.java.core.streams.{ ReadStream => JReadStream, WriteStream => JWriteStream, ExceptionSupport => JExceptionSupport }
import org.vertx.scala.VertxWrapper

trait WrappedReadWriteStream extends WrappedReadStream with WrappedWriteStream with WrappedExceptionSupport {
  override type InternalType <: JReadStream[_] with JWriteStream[_]
}