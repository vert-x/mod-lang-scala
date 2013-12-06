package org.vertx.scala

/**
 * [[scala.AnyVal]] instances do not allow references to `this.type`, so
 * [[org.vertx.scala.Self]] was created in order to allow fluent return types
 * pointing to the type of thi object. This way, classes extending to be
 * defined at the intermediate mixins for [[scala.AnyVal]] instances.
 *
 * @author <a href="http://www.campudus.com/">Joern Bernhardt</a>
 * @author Galder Zamarreño
 */
// Extends Any in order to be an universal trait and be mixed in with an AnyVal
private[scala] trait Self[+S] extends Any {

  /**
   * Returns the Scala object represented by this object
   */
  protected[this] def self: S

  /**
   * Helper method wrapping invocations and returning the Scala type, once
   * again to help provide fluent return types
   */
  protected[this] def wrap[X](doStuff: => X): S = {
    doStuff
    self
  }

}
