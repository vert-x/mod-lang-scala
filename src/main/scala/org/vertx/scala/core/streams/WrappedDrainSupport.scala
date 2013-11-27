package org.vertx.scala.core.streams

import org.vertx.scala.VertxWrapper
import org.vertx.java.core.streams.{ DrainSupport => JDrainSupport }
import org.vertx.scala.core.FunctionConverters._

/**
 * Wrapper for Vert.x Java [[org.vertx.java.core.streams.DrainSupport]] class.
 *
 * @author Galder Zamarreño
 */
trait WrappedDrainSupport extends DrainSupport with VertxWrapper {
  type InternalType <: JDrainSupport[_]

  /**
   * Set the maximum size of the write queue to `maxSize`. You will still be
   * able to write to the stream even if there is more than `maxSize` bytes in
   * the write queue. This is used as an indicator by classes such as `Pump`
   * to provide flow control.
   */
  override def setWriteQueueMaxSize(maxSize: Int): this.type =
    wrap(internal.setWriteQueueMaxSize(maxSize))

  /**
   * This will return `true` if there are more bytes in the write queue than
   * the value set using [[org.vertx.scala.core.streams.DrainSupport.setWriteQueueMaxSize]]
   */
  override def writeQueueFull: Boolean = internal.writeQueueFull()

  /**
   * Set a drain handler on the stream. If the write queue is full, then the
   * handler will be called when the write queue has been reduced to
   * maxSize / 2. See `Pump` for an example of this being used.
   */
  override def drainHandler(handler: => Unit): this.type =
    wrap(internal.drainHandler(lazyToVoidHandler(handler)))

}
