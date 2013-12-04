package org.vertx.scala.core.streams

import org.vertx.java.core.streams.{ ReadSupport => JReadSupport }
import org.vertx.java.core.streams.{ ExceptionSupport => JExceptionSupport }
import org.vertx.scala.{Self, AsJava}

/**
 * Allows to set a handler which is notified once data was read.
 * It also allows to pause reading and resume later.
 *
 * @author <a href="mailto:nmaurer@redhat.com">Norman Maurer</a>
 * @author Galder Zamarreño
 */
trait ReadSupport[E] extends Self
  with ExceptionSupport
  with AsJava {

  override type J <: JReadSupport[_, _] with JExceptionSupport[_]

  /**
   * Set a data handler. As data is read, the handler will be called with the data.
   */
  def dataHandler(handler: E => Unit): this.type

  /**
   * Pause the `ReadSupport`. While it's paused, no data will be sent to the `dataHandler`
   */
  def pause(): this.type = wrap(asJava.pause())

  /**
   * Resume reading. If the `ReadSupport` has been paused, reading will recommence on it.
   */
  def resume(): this.type = wrap(asJava.resume())

}
