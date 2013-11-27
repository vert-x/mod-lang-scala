package org.vertx.scala.core.streams

import org.vertx.java.core.streams.{ ReadStream => JReadStream, WriteStream => JWriteStream}

trait WrappedReadWriteStream extends WrappedReadStream with WrappedWriteStream with WrappedExceptionSupport {
  override type InternalType <: JReadStream[_] with JWriteStream[_]
}