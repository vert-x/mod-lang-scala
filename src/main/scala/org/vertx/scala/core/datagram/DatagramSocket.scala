package org.vertx.scala.core.datagram

import org.vertx.java.core.datagram.{DatagramSocket => JDatagramSocket}
import org.vertx.scala.VertxWrapper
import org.vertx.scala.core.streams.{WrappedReadSupport, WrappedDrainSupport}
import org.vertx.scala.core._
import org.vertx.scala.core.buffer._
import org.vertx.scala.core.FunctionConverters._
import java.net.InetSocketAddress

/** Factory for [[org.vertx.scala.core.datagram.DatagramSocket]] instances by wrapping a Java instance. */
object DatagramSocket {
  def apply(actual: JDatagramSocket) = new DatagramSocket(actual)
}

/**
 * A Datagram socket which can be used to send [[org.vertx.scala.core.datagram.DatagramPacket]]'s to
 * remote Datagram servers and receive [[org.vertx.scala.core.datagram.DatagramPacket]]s.
 *
 * Usually you use a Datagram Client to send UDP over the wire. UDP is
 * connection-less which means you are not connected to the remote peer in a
 * persistent way. Because of this you have to supply the address and port of
 * the remote peer when sending data.
 *
 * You can send data to ipv4 or ipv6 addresses, which also include multicast
 * addresses.
 *
 * @author <a href="mailto:nmaurer@redhat.com">Norman Maurer</a>
 * @author Galder Zamarreño
 */
class DatagramSocket(protected[this] val internal: JDatagramSocket)
    extends VertxWrapper
    with WrappedDrainSupport
    with WrappedNetworkSupport
    with WrappedReadSupport[DatagramPacket] {
  override type InternalType = JDatagramSocket

  /**
   * Write the given [[org.vertx.scala.core.buffer.Buffer]] to the [[java.net.InetSocketAddress]].
   * The handler will be notified once write completes.
   *
   * @param packet    the [[org.vertx.scala.core.buffer.Buffer]] to write
   * @param host      the host address of the remote peer
   * @param port      the host port of the remote peer
   * @param handler   the handler to notify once the write completes.
   * @return self     itself for method chaining
   */
  def send(packet: Buffer, host: String, port: Int, handler: AsyncResult[DatagramSocket] => Unit): this.type =
    wrap(internal.send(packet.toJava(), host, port, dataSocketAsyncResult(handler)))

  /**
   * Write the given String to the [[java.net.InetSocketAddress]].
   * The handler will be notified once write completes.
   *
   * @param str       the String to write
   * @param host      the host address of the remote peer
   * @param port      the host port of the remote peer
   * @param handler   the handler to notify once the write completes.
   * @return self     itself for method chaining
   */
  def send(str: String, host: String, port: Int, handler: AsyncResult[DatagramSocket] => Unit): this.type =
    wrap(internal.send(str, host, port, dataSocketAsyncResult(handler)))

  /**
   * Write the given String to the [[java.net.InetSocketAddress]] using the given encoding.
   * The handler will be notified once write completes.
   *
   * @param str       the String to write
   * @param enc       the charset used for encoding
   * @param host      the host address of the remote peer
   * @param port      the host port of the remote peer
   * @param handler   the handler to notify once the write completes.
   * @return self     itself for method chaining
   */
  def send(str: String, enc: String, host: String, port: Int, handler: AsyncResult[DatagramSocket] => Unit): this.type =
    wrap(internal.send(str, enc, host, port, dataSocketAsyncResult(handler)))

  /**
   * Gets the [[java.net.StandardSocketOptions.SO_BROADCAST]] option.
   */
  def isBroadcast: Boolean = internal.isBroadcast

  /**
   * Sets the [[java.net.StandardSocketOptions.SO_BROADCAST]] option.
   */
  def setBroadcast(broadcast: Boolean): this.type = wrap(internal.setBroadcast(broadcast))

  /**
   * Gets the [[java.net.StandardSocketOptions.IP_MULTICAST_LOOP]] option.
   *
   * @return `true` if and only if the loopback mode has been disabled
   */
  def isMulticastLoopbackMode: Boolean = internal.isMulticastLoopbackMode

  /**
   * Sets the [[java.net.StandardSocketOptions.IP_MULTICAST_LOOP]] option.
   *
   * @param loopbackModeDisabled `true` if and only if the loopback mode has been disabled
   */
  def setMulticastLoopbackMode(loopbackModeDisabled: Boolean): this.type =
    wrap(internal.setMulticastLoopbackMode(loopbackModeDisabled))

  /**
   * Gets the [[java.net.StandardSocketOptions.IP_MULTICAST_TTL]] option.
   */
  def getMulticastTimeToLive: Int = internal.getMulticastTimeToLive

  /**
   * Sets the [[java.net.StandardSocketOptions.IP_MULTICAST_TTL]] option.
   */
  def setMulticastTimeToLive(ttl: Int): this.type = wrap(internal.setMulticastTimeToLive(ttl))

  /**
   * Gets the [[java.net.StandardSocketOptions.IP_MULTICAST_IF]] option.
   */
  def getMulticastNetworkInterface: String = internal.getMulticastNetworkInterface

  /**
   * Sets the [[java.net.StandardSocketOptions.IP_MULTICAST_IF]] option.
   */
  def setMulticastNetworkInterface(iface: String): this.type =
    wrap(internal.setMulticastNetworkInterface(iface))

  /**
   * Close the [[org.vertx.scala.core.datagram.DatagramSocket]]
   * implementation asynchronous and notifies the handler once done.
   */
  def close(doneHandler: AsyncResult[Void] => Unit): Unit =
    internal.close(fnToHandler(doneHandler))

  /**
   * Close the [[org.vertx.scala.core.datagram.DatagramSocket]] implementation asynchronous.
   */
  def close(): Unit = internal.close()

  /**
   * Return the [[java.net.InetSocketAddress]] to which this
   * [[org.vertx.scala.core.datagram.DatagramSocket]] is bound too.
   */
  def localAddress(): InetSocketAddress = internal.localAddress()

  /**
   * Joins a multicast group and so start listen for packets send to it.
   * The handler is notified once the operation completes.
   *
   * @param   multicastAddress  the address of the multicast group to join
   * @param   handler           then handler to notify once the operation completes
   * @return  this              returns itself for method-chaining
   */
  def listenMulticastGroup(multicastAddress: String, handler: AsyncResult[DatagramSocket] => Unit): this.type =
    wrap(internal.listenMulticastGroup(multicastAddress, dataSocketAsyncResult(handler)))

  /**
   * Joins a multicast group and so start listen for packets send to it on
   * the given network interface. The handler is notified once the operation completes.
   *
   *
   * @param   multicastAddress  the address of the multicast group to join
   * @param   networkInterface  the network interface on which to listen for packets.
   * @param   source            the address of the source for which we will listen for mulicast packets
   * @param   handler           then handler to notify once the operation completes
   * @return  this              returns itself for method-chaining
   */
  def listenMulticastGroup(multicastAddress: String, networkInterface: String, source: String, handler: AsyncResult[DatagramSocket] => Unit): this.type =
    wrap(internal.listenMulticastGroup(multicastAddress, networkInterface, source, dataSocketAsyncResult(handler)))

  /**
   * Leaves a multicast group and so stop listen for packets send to it.
   * The handler is notified once the operation completes.
   *
   *
   * @param   multicastAddress  the address of the multicast group to leave
   * @param   handler           then handler to notify once the operation completes
   * @return  this              returns itself for method-chaining
   */
  def unlistenMulticastGroup(multicastAddress: String, handler: AsyncResult[DatagramSocket] => Unit): this.type =
    wrap(internal.unlistenMulticastGroup(multicastAddress, dataSocketAsyncResult(handler)))


  /**
   * Leaves a multicast group and so stop listen for packets send to it on the given network interface.
   * The handler is notified once the operation completes.
   *
   *
   * @param   multicastAddress  the address of the multicast group to join
   * @param   networkInterface  the network interface on which to listen for packets.
   * @param   source            the address of the source for which we will listen for mulicast packets
   * @param   handler           then handler to notify once the operation completes
   * @return  this              returns itself for method-chaining
   */
  def unlistenMulticastGroup(multicastAddress: String, networkInterface: String, source: String, handler: AsyncResult[DatagramSocket] => Unit): this.type =
    wrap(internal.unlistenMulticastGroup(multicastAddress, networkInterface, source, dataSocketAsyncResult(handler)))

  /**
   * Block the given sourceToBlock address for the given multicastAddress and
   * notifies the handler once the operation completes.
   *
   *
   * @param   multicastAddress  the address for which you want to block the sourceToBlock
   * @param   sourceToBlock     the source address which should be blocked. You will not receive an multicast packets
   *                            for it anymore.
   * @param   handler           then handler to notify once the operation completes
   * @return  this              returns itself for method-chaining
   */
  def blockMulticastGroup(multicastAddress: String, sourceToBlock: String, handler: AsyncResult[DatagramSocket] => Unit): this.type =
    wrap(internal.blockMulticastGroup(multicastAddress, sourceToBlock, dataSocketAsyncResult(handler)))

  /**
   * Block the given sourceToBlock address for the given multicastAddress on
   * the given network interface and notifies the handler once the operation completes.
   *
   *
   * @param   multicastAddress  the address for which you want to block the sourceToBlock
   * @param   networkInterface  the network interface on which the blocking should accour.
   * @param   sourceToBlock     the source address which should be blocked. You will not receive an multicast packets
   *                            for it anymore.
   * @param   handler           then handler to notify once the operation completes
   * @return  this              returns itself for method-chaining
   */
  def blockMulticastGroup(multicastAddress: String, networkInterface: String, sourceToBlock: String, handler: AsyncResult[DatagramSocket] => Unit): this.type =
    wrap(internal.blockMulticastGroup(multicastAddress, networkInterface, sourceToBlock, dataSocketAsyncResult(handler)))

  /**
   * @see [[org.vertx.scala.core.datagram.DatagramSocket.listen(java.net.InetSocketAddress, org.vertx.java.core.Handler)]]
   */
  def listen(address: String, port: Int, handler: AsyncResult[DatagramSocket] => Unit): this.type =
    wrap(internal.listen(address, port, dataSocketAsyncResult(handler)))

  /**
   * @see [[org.vertx.scala.core.datagram.DatagramSocket.listen(java.net.InetSocketAddress, org.vertx.java.core.Handler)]]
   */
  def listen(port: Int, handler: AsyncResult[DatagramSocket] => Unit): this.type =
    wrap(internal.listen(port, dataSocketAsyncResult(handler)))

  /**
   * Makes this [[org.vertx.scala.core.datagram.DatagramSocket]]} listen to
   * the given [[java.net.InetSocketAddress]]. Once the operation completes
   * the handler is notified.
   *
   * @param local     the [[java.net.InetSocketAddress]] on which the
   *                  [[org.vertx.scala.core.datagram.DatagramSocket]] will
   *                  listen for [[org.vertx.scala.core.datagram.DatagramPacket]]}s.
   * @param handler   the handler to notify once the operation completes
   * @return this     itself for method-chaining
   */
  def listen(local: InetSocketAddress, handler: AsyncResult[DatagramSocket] => Unit): this.type =
    wrap(internal.listen(local, dataSocketAsyncResult(handler)))

  /**
   * Set a data handler. As data is read, the handler will be called with the data.
   */
  override def dataHandler(handler: DatagramPacket => Unit): this.type =
    wrap(internal.dataHandler(dataPacketHandlerToJava(handler)))

  private def dataSocketAsyncResult = asyncResultConverter(DatagramSocket.apply) _
}
