package org.vertx.scala

import org.vertx.java.core.Handler

/**
 * This trait shows that this type is a wrapper around a Vert.x Java class.
 *
 * @author <a href="http://www.campudus.com/">Joern Bernhardt</a>
 */
trait VertxWrapper extends Wrap {

  /** The internal type of the wrapped class. */
  type InternalType

  /** The internal instance of the wrapped class. */
  protected val internal: InternalType

  /**
   * Returns the internal type instance.
   *
   * @return The internal type instance.
   */
  def toJava(): InternalType = internal
}