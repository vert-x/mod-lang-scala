package org.vertx.scala.core

import org.vertx.java.core.TCPSupport
import org.vertx.scala.VertxWrapper

trait WrappedTCPSupport extends WrappedNetworkSupport with VertxWrapper {
  override type InternalType <: TCPSupport[_]

  /**
   *
   * @return the value of TCP so linger
   */
  def getSoLinger: Int = internal.getSoLinger

  /**
   *
   * @return true if TCP keep alive is enabled
   */
  def isTCPKeepAlive: Boolean = internal.isTCPKeepAlive

  /**
   * @return true if Nagle's algorithm is disabled.
   */
  def isTCPNoDelay: Boolean = internal.isTCPNoDelay

  /**
   * @return `true` if pooled buffers are used
   */
  def isUsePooledBuffers: Boolean = internal.isUsePooledBuffers

  /**
   * Set the TCP soLinger setting for connections created by this instance to `linger`.
   * Using a negative value will disable soLinger.
   * @return a reference to this so multiple method calls can be chained together
   *
   */
  def setSoLinger(linger: Int): this.type = wrap(internal.setSoLinger(linger))

  /**
   * Set the TCP keepAlive setting for connections created by this instance to `keepAlive`.
   * @return a reference to this so multiple method calls can be chained together
   */
  def setTCPKeepAlive(keepAlive: Boolean): this.type = wrap(internal.setTCPKeepAlive(keepAlive))

  /**
   * If `tcpNoDelay` is set to `true` then <a href="http://en.wikipedia.org/wiki/Nagle's_algorithm">Nagle's algorithm</a>
   * will turned <b>off</b> for the TCP connections created by this instance.
   * @return a reference to this so multiple method calls can be chained together
   */
  def setTCPNoDelay(tcpNoDelay: Boolean): this.type = wrap(internal.setTCPNoDelay(tcpNoDelay))

  /**
   * Set if vertx should use pooled buffers for performance reasons. Doing so will give the best throughput but
   * may need a bit higher memory footprint.
   * @return a reference to this so multiple method calls can be chained together
   */
  def setUsePooledBuffers(pooledBuffers: Boolean): this.type = wrap(internal.setUsePooledBuffers(pooledBuffers))
}