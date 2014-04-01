package org.vertx.scala.tests.core.http

/**
 * @author Galder Zamarreño
 */
sealed trait Compression {
  def enabled(): Boolean
}

case object Compressed extends Compression{
  override def enabled(): Boolean = true
}

case object Uncompressed extends Compression{
  override def enabled(): Boolean = false
}
