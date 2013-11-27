package org.vertx.scala.core.datagram

import org.vertx.java.core.datagram.{DatagramPacket => JDatagramPacket}
import org.vertx.scala.VertxWrapper
import java.net.InetSocketAddress
import org.vertx.scala.core.buffer.Buffer

/** Factory for [[org.vertx.scala.core.datagram.DatagramPacket]] instances by wrapping a Java instance. */
object DatagramPacket {
  def apply(actual: JDatagramPacket) = new DatagramPacket(actual)
}

/**
 * A received Datagram packet (UDP) which contains the data and information about the sender of the data itself.
 *
 * @author <a href="mailto:nmaurer@redhat.com">Norman Maurer</a>
 * @author Galder Zamarreño
 */
class DatagramPacket(protected[this] val internal: JDatagramPacket) extends VertxWrapper {
  override type InternalType = JDatagramPacket

  /**
   * Returns the [[java.net.InetSocketAddress]] of the sender that send this
   * [[org.vertx.scala.core.datagram.DatagramPacket]].
   */
  def sender(): InetSocketAddress = internal.sender()

  /**
   * Returns the data of the [[org.vertx.scala.core.datagram.DatagramPacket]]
   */
  def data(): Buffer = Buffer(internal.data())

}
