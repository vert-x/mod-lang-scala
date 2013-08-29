package org.vertx.scala.core

import org.vertx.java.core.ServerSSLSupport

/**
 * @author <a href="http://www.campudus.com/">Joern Bernhardt</a>
 */
trait WrappedServerSSLSupport extends WrappedSSLSupport {
  override type InternalType <: ServerSSLSupport[_]

  /**
   * Is client auth required?
   */
  def isClientAuthRequired(): Boolean = internal.isClientAuthRequired()

  /**
   * Set {@code required} to true if you want the server to request client authentication from any connecting clients. This
   * is an extra level of security in SSL, and requires clients to provide client certificates. Those certificates must be added
   * to the server trust store.
   * @return A reference to this, so multiple invocations can be chained together.
   */
  def setClientAuthRequired(required: Boolean): this.type = wrap(internal.setClientAuthRequired(required))
}