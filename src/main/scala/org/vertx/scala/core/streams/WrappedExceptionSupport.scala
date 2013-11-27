package org.vertx.scala.core.streams

import org.vertx.java.core.streams.{ ExceptionSupport => JExceptionSupport }
import org.vertx.scala.VertxWrapper
import org.vertx.scala.core.FunctionConverters._

/**
 * Wrapper for Vert.x Java [[org.vertx.java.core.streams.ExceptionSupport]] class.
 *
 * @author swilliams
 * @author <a href="http://www.campudus.com/">Joern Bernhardt</a>
 * @author Galder Zamarreño
 */
trait WrappedExceptionSupport extends ExceptionSupport with VertxWrapper {
  type InternalType <: JExceptionSupport[_]

  /**
   * Set an exception handler.
   */
  override def exceptionHandler(h: Throwable => Unit): this.type =
    wrap(internal.exceptionHandler(fnToHandler(h)))

  /**
   * Provide the corresponding Java version of this class.
   */
  override def toJava(): InternalType = internal

}