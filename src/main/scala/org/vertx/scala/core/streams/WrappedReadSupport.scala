package org.vertx.scala.core.streams

/**
 * Wrapper for Vert.x Java [[org.vertx.java.core.streams.ReadSupport]] class.
 *
 * @author Galder Zamarreño
 */
trait WrappedReadSupport[E] extends WrappedExceptionSupport with ReadSupport[E] {

  /**
   * Pause the `ReadSupport`. While it's paused, no data will be sent to the `dataHandler`
   */
  override def pause(): this.type = wrap(internal.pause())

  /**
   * Resume reading. If the `ReadSupport` has been paused, reading will recommence on it.
   */
  override def resume(): this.type = wrap(internal.resume())

}
