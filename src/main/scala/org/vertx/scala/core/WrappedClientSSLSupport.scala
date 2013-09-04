package org.vertx.scala.core

import org.vertx.java.core.ClientSSLSupport
import org.vertx.scala.VertxWrapper

trait WrappedClientSSLSupport extends WrappedSSLSupport {
  override type InternalType <: ClientSSLSupport[_]

  // Members declared in  org.vertx.java.core.ClientSSLSupport
  def isTrustAll(): Boolean = internal.isTrustAll()
  def setTrustAll(trustAll: Boolean): this.type = wrap(internal.setTrustAll(trustAll))
}