package org.vertx.scala.core.streams

import org.vertx.scala.core.buffer._
import org.vertx.scala.core.FunctionConverters._

/**
 * Wrapper for Vert.x Java [[org.vertx.java.core.streams.ReadStream]] class.
 *
 * @author <a href="http://www.campudus.com/">Joern Bernhardt</a>
 * @author Galder Zamarreño
 */
trait WrappedReadStream extends WrappedReadSupport[Buffer] with ReadStream {

  /**
   * Set a data handler. As data is read, the handler will be called with the data.
   */
  override def dataHandler(handler: Buffer => Unit): this.type =
    wrap(internal.dataHandler(bufferHandlerToJava(handler)))

  /**
   * Set an end handler. Once the stream has ended, and there is no more data
   * to be read, this handler will be called.
   */
  override def endHandler(handler: => Unit): this.type =
    wrap(internal.endHandler(lazyToVoidHandler(handler)))

}