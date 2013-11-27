package org.vertx.scala.core

import org.vertx.java.core.NetworkSupport
import org.vertx.scala.VertxWrapper

trait WrappedNetworkSupport extends VertxWrapper {

  override type InternalType <: NetworkSupport[_]

  /**
   * Set the send buffer size for connections created by this instance to `size` in bytes.
   * @return a reference to this so multiple method calls can be chained together
   */
  def setSendBufferSize(size: Int): this.type = wrap(internal.setSendBufferSize(size))

  /**
   * Set the receive buffer size for connections created by this instance to `size` in bytes.
   * @return a reference to this so multiple method calls can be chained together
   */
  def setReceiveBufferSize(size: Int): this.type = wrap(internal.setReceiveBufferSize(size))

  /**
   * Set the reuseAddress setting for connections created by this instance to `reuse`.
   * @return a reference to this so multiple method calls can be chained together
   */
  def setReuseAddress(reuse: Boolean): this.type = wrap(internal.setReuseAddress(reuse))

  /**
   * Set the trafficClass setting for connections created by this instance to `trafficClass`.
   * @return a reference to this so multiple method calls can be chained together
   */
  def setTrafficClass(trafficClass: Int): this.type = wrap(internal.setTrafficClass(trafficClass))

  /**
   * @return The send buffer size
   */
  def getSendBufferSize: Int = internal.getSendBufferSize

  /**
   * @return The receive buffer size
   */
  def getReceiveBufferSize: Int = internal.getReceiveBufferSize

  /**
   * @return The value of reuse address
   */
  def isReuseAddress: Boolean = internal.isReuseAddress

  /**
   * @return the value of traffic class
   */
  def getTrafficClass: Int = internal.getTrafficClass

}
