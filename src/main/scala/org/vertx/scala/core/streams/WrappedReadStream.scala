package org.vertx.scala.core.streams

import org.vertx.java.core.buffer.Buffer
import org.vertx.java.core.streams.{ ReadStream => JReadStream }
import org.vertx.java.core.streams.{ ExceptionSupport => JExceptionSupport }
import org.vertx.scala.VertxWrapper
import org.vertx.scala.core.FunctionConverters._
import org.vertx.java.core.Handler

/**
 * @author <a href="http://www.campudus.com/">Joern Bernhardt</a>
 */
trait WrappedReadStream extends WrappedExceptionSupport with ReadStream {
  override def dataHandler(handler: Handler[Buffer]): this.type = wrap(internal.dataHandler(handler))
  override def dataHandler(handler: Buffer => Unit): this.type = dataHandler(convertFunctionToParameterisedHandler(handler))

  override def endHandler(handler: Handler[Void]): this.type = wrap(internal.endHandler(handler))
  override def endHandler(handler: () => Unit): this.type = endHandler(convertFunctionToVoidHandler(handler))

  override def pause(): this.type = wrap(internal.pause())

  override def resume(): this.type = wrap(internal.resume())

  override def toJava(): InternalType = internal
}