package org.vertx.scala.core.streams

import org.vertx.java.core.streams.{ ReadStream => JReadStream, WriteStream => JWriteStream, ExceptionSupport => JExceptionSupport }
import org.vertx.scala.VertxWrapper

trait WrappedReadAndWriteStream[ST <: ReadStream[ST] with WriteStream[ST] with ExceptionSupport[ST], JT <: JReadStream[JT] with JWriteStream[JT] with JExceptionSupport[JT]] extends WrappedReadStream[ST, JT] with WrappedWriteStream[ST, JT] {
  this: ST =>
}
