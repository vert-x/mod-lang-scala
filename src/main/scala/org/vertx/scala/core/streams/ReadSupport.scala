package org.vertx.scala.core.streams

import org.vertx.java.core.streams.{ ReadSupport => JReadSupport }
import org.vertx.java.core.streams.{ ExceptionSupport => JExceptionSupport }
import org.vertx.scala.{Self, AsJava}

/**
 * Allows to set a handler which is notified once data was read.
 * It also allows to pause reading and resume later.
 * @tparam S self type to help provide fluent APIs
 *
 * @author <a href="mailto:nmaurer@redhat.com">Norman Maurer</a>
 * @author Galder Zamarreño
 */
trait ReadSupport[+S <: ReadSupport[S, E], E] extends Any
  with ExceptionSupport[S]
  with Self[S]
  with AsJava {

  override type J <: JReadSupport[_, _] with JExceptionSupport[_]

  /**
   * Set a data handler. As data is read, the handler will be called with the data.
   */
  def dataHandler(handler: E => Unit): S

  /**
   * Pause the `ReadSupport`. While it's paused, no data will be sent to the `dataHandler`
   */
  def pause(): S = wrap(asJava.pause())

  /**
   * Resume reading. If the `ReadSupport` has been paused, reading will recommence on it.
   */
  def resume(): S = wrap(asJava.resume())

}
