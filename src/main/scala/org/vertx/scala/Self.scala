package org.vertx.scala

/**
 * [[org.vertx.scala.Self]] allows fluent return types pointing to the type of
 * this object.
 *
 * @author <a href="http://www.campudus.com/">Joern Bernhardt</a>
 * @author Galder Zamarreño
 */
private[scala] trait Self {

  /**
   * Helper method wrapping invocations and returning the Scala type, once
   * again to help provide fluent return types
   */
  protected[this] def wrap[X](doStuff: => X): this.type = {
    doStuff
    this
  }

}
