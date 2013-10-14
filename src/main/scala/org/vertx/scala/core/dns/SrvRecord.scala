package org.vertx.scala.core.dns

import org.vertx.java.core.dns.{ SrvRecord => JSrvRecord }
import org.vertx.scala.VertxWrapper

/**
 * Represent a Service-Record (SRV) which was resolved for a domain.
 *
 * @author <a href="mailto:nmaurer@redhat.com">Norman Maurer</a>
 * @author <a href="http://www.campudus.com/">Joern Bernhardt</a>
 */
class SrvRecord(protected[this] val internal: JSrvRecord) extends VertxWrapper {
  override type InternalType = JSrvRecord

  /**
   * Returns the priority for this service record.
   */
  def priority(): Int = internal.priority()

  /**
   * Returns the weight of this service record.
   */
  def weight(): Int = internal.weight()

  /**
   * Returns the port the service is running on.
   */
  def port(): Int = internal.port()

  /**
   * Returns the name for the server being queried.
   */
  def name(): String = internal.name()

  /**
   * Returns the protocol for the service being queried (i.e. "_tcp").
   */
  def protocol(): String = internal.protocol()

  /**
   * Returns the service's name (i.e. "_http").
   */
  def service(): String = internal.service()

  /**
   * Returns the name of the host for the service.
   */
  def target(): String = internal.target()
}

object SrvRecord {
  def apply(internal: JSrvRecord) = new SrvRecord(internal)
}