package org.vertx.scala.core.dns

import org.vertx.java.core.dns.{ MxRecord => JMxRecord }
import org.vertx.scala.VertxWrapper

/**
 * Represent a Mail-Exchange-Record (MX) which was resolved for a domain.
 *
 * @author <a href="mailto:nmaurer@redhat.com">Norman Maurer</a>
 * @author <a href="http://www.campudus.com/">Joern Bernhardt</a>
 */
class MxRecord(protected[this] val internal: JMxRecord) extends VertxWrapper {
  override type InternalType = JMxRecord

  /**
   * The priority of the MX record.
   */
  def priority(): Int = internal.priority()

  /**
   * The name of the MX record
   */
  def name(): String = internal.name()
}

object MxRecord {
  def apply(internal: JMxRecord) = new MxRecord(internal)
}
