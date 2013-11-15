package org.vertx.scala.core

import org.vertx.scala.core.logging.Logger
import org.vertx.scala.platform.Container

/**
 * Classes implementing this trait provide direct access to `Vertx`, `Container` and `Logger`. Be
 * cautious when using this trait: Do not use the provided `vertx`, `container` and `logger` at
 * construction of the object, otherwise they might not be initialized yet.
 */
trait VertxAccess {
  val vertx: Vertx
  val container: Container
  val logger: Logger
}