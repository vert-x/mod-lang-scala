package org.vertx.scala.core

import org.vertx.java.core.TCPSupport
import org.vertx.scala.VertxWrapper

trait WrappedTCPSupport extends VertxWrapper {
  override type InternalType <: TCPSupport[_]

  /**
   * @return The TCP receive buffer size
   */
  def getReceiveBufferSize(): Int = internal.getReceiveBufferSize()

  /**
   * @return The TCP send buffer size
   */
  def getSendBufferSize(): Int = internal.getSendBufferSize()

  /**
   *
   * @return the value of TCP so linger
   */
  def getSoLinger(): Int = internal.getSoLinger()

  /**
   *
   * @return the value of TCP traffic class
   */
  def getTrafficClass(): Int = internal.getTrafficClass()

  /**
   *
   * @return The value of TCP reuse address
   */
  def isReuseAddress(): Boolean = internal.isReuseAddress()

  /**
   *
   * @return true if TCP keep alive is enabled
   */
  def isTCPKeepAlive(): Boolean = internal.isTCPKeepAlive()

  /**
   * @return true if Nagle's algorithm is disabled.
   */
  def isTCPNoDelay(): Boolean = internal.isTCPNoDelay()

  /**
   * @return {@code true} if pooled buffers are used
   */
  def isUsePooledBuffers(): Boolean = internal.isUsePooledBuffers()

  /**
   * Set the TCP receive buffer size for connections created by this instance to {@code size} in bytes.
   * @return a reference to this so multiple method calls can be chained together
   */
  def setReceiveBufferSize(size: Int): this.type = wrap(internal.setReceiveBufferSize(size))

  /**
   * Set the TCP reuseAddress setting for connections created by this instance to {@code reuse}.
   * @return a reference to this so multiple method calls can be chained together
   */
  def setReuseAddress(reuse: Boolean): this.type = wrap(internal.setReuseAddress(reuse))

  /**
   * Set the TCP send buffer size for connections created by this instance to {@code size} in bytes.
   * @return a reference to this so multiple method calls can be chained together
   */
  def setSendBufferSize(size: Int): this.type = wrap(internal.setSendBufferSize(size))

  /**
   * Set the TCP soLinger setting for connections created by this instance to {@code linger}.
   * Using a negative value will disable soLinger.
   * @return a reference to this so multiple method calls can be chained together
   *
   */
  def setSoLinger(linger: Int): this.type = wrap(internal.setSoLinger(linger))

  /**
   * Set the TCP keepAlive setting for connections created by this instance to {@code keepAlive}.
   * @return a reference to this so multiple method calls can be chained together
   */
  def setTCPKeepAlive(keepAlive: Boolean): this.type = wrap(internal.setTCPKeepAlive(keepAlive))

  /**
   * If {@code tcpNoDelay} is set to {@code true} then <a href="http://en.wikipedia.org/wiki/Nagle's_algorithm">Nagle's algorithm</a>
   * will turned <b>off</b> for the TCP connections created by this instance.
   * @return a reference to this so multiple method calls can be chained together
   */
  def setTCPNoDelay(tcpNoDelay: Boolean): this.type = wrap(internal.setTCPNoDelay(tcpNoDelay))

  /**
   * Set the TCP trafficClass setting for connections created by this instance to {@code trafficClass}.
   * @return a reference to this so multiple method calls can be chained together
   */
  def setTrafficClass(trafficClass: Int): this.type = wrap(internal.setTrafficClass(trafficClass))

  /**
   * Set if vertx should use pooled buffers for performance reasons. Doing so will give the best throughput but
   * may need a bit higher memory footprint.
   * @return a reference to this so multiple method calls can be chained together
   */
  def setUsePooledBuffers(pooledBuffers: Boolean): this.type = wrap(internal.setUsePooledBuffers(pooledBuffers))
}