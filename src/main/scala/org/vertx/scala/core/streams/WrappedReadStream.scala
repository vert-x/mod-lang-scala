package org.vertx.scala.core.streams

import org.vertx.java.core.buffer.Buffer
import org.vertx.java.core.streams.{ ReadStream => JReadStream }
import org.vertx.java.core.streams.{ ExceptionSupport => JExceptionSupport }
import org.vertx.scala.VertxWrapper
import org.vertx.scala.core.FunctionConverters._

/**
 * @author <a href="http://www.campudus.com/">Joern Bernhardt</a>
 */
trait WrappedReadStream[ST <: ReadStream[ST] with ExceptionSupport[ST], JT <: JReadStream[JT] with JExceptionSupport[JT]] extends WrappedExceptionSupport[ST, JT] with ReadStream[ST] { this: ST =>

  override def dataHandler(handler: Buffer => Unit): ST = wrap(internal.dataHandler(handler))

  override def endHandler(handler: () => Unit): ST = wrap(internal.endHandler(handler))

  override def pause(): ST = wrap(internal.pause())

  override def resume(): ST = wrap(internal.resume())

  def toJava(): JReadStream[JT] = internal
}