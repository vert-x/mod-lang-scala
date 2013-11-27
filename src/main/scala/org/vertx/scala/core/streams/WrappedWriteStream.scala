package org.vertx.scala.core.streams

import org.vertx.scala.core.FunctionConverters._
import org.vertx.scala.core.buffer.Buffer

/**
 * Wrapper for Vert.x Java [[org.vertx.java.core.streams.WriteStream]] class.
 *
 * @author <a href="http://www.campudus.com/">Joern Bernhardt</a>
 * @author Galder Zamarreño
 */
trait WrappedWriteStream extends WrappedExceptionSupport with WriteStream {

  /**
   * Set a drain handler on the stream. If the write queue is full, then the handler will be called when the write
   * queue has been reduced to maxSize / 2. See [[org.vertx.scala.core.streams.Pump]] for an example of this being used.
   */
  override def drainHandler(handler: => Unit): this.type =
    wrap(internal.drainHandler(lazyToVoidHandler(handler)))

  /**
   * Set the maximum size of the write queue to `maxSize`. You will still be able to write to the stream even
   * if there is more than `maxSize` bytes in the write queue. This is used as an indicator by classes such as
   * `Pump` to provide flow control.
   */
  override def setWriteQueueMaxSize(maxSize: Int): this.type =
    wrap(internal.setWriteQueueMaxSize(maxSize))

  /**
   * Write some data to the stream. The data is put on an internal write queue, and the write actually happens
   * asynchronously. To avoid running out of memory by putting too much on the write queue,
   * check the [[org.vertx.scala.core.streams.WriteStream.writeQueueFull()]]
   * method before writing. This is done automatically if using a [[org.vertx.scala.core.streams.Pump]].
   */
  override def write(data: Buffer): this.type = wrap(internal.write(data.toJava()))

  /**
   * This will return `true` if there are more bytes in the write queue than the value set using
   * [[org.vertx.scala.core.streams.WriteStream.setWriteQueueMaxSize()]]
   */
  override def writeQueueFull(): Boolean = internal.writeQueueFull()

}