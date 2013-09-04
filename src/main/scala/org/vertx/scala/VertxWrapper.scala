package org.vertx.scala

import org.vertx.java.core.Handler

/**
 * This trait shows that this type is a wrapper around a Vert.x Java class.
 *
 * @author <a href="http://www.campudus.com/">Joern Bernhardt</a>
 */
trait VertxWrapper extends Wrap {
  type InternalType
  protected[this] val internal: InternalType

  def toJava(): InternalType = internal
}