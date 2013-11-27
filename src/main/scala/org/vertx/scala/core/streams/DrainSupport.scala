package org.vertx.scala.core.streams

/**
 * Allows to set a `Handler` which is notified once the write queue is
 * drained again. This way you can stop writing once the write queue consumes
 * to much memory and so prevent an OutOfMemoryError.
 *
 * @author <a href="mailto:nmaurer@redhat.com">Norman Maurer</a>
 * @author Galder Zamarreño
 */
trait DrainSupport {

  /**
   * Set the maximum size of the write queue to `maxSize`. You will still be
   * able to write to the stream even if there is more than `maxSize` bytes in
   * the write queue. This is used as an indicator by classes such as `Pump`
   * to provide flow control.
   */
  def setWriteQueueMaxSize(maxSize: Int): this.type

  /**
   * This will return `true` if there are more bytes in the write queue than
   * the value set using [[org.vertx.scala.core.streams.DrainSupport.setWriteQueueMaxSize]]
   */
  def writeQueueFull: Boolean

  /**
   * Set a drain handler on the stream. If the write queue is full, then the
   * handler will be called when the write queue has been reduced to
   * maxSize / 2. See `Pump` for an example of this being used.
   */
  def drainHandler(handler: => Unit): this.type

}
