/*
 * Copyright 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.vertx.scala.core

import org.vertx.java.core.{ TCPSupport => JTCPSupport }

/**
 * Supports [[org.vertx.java.core.TCPSupport]] functionality.
 * @tparam S self type to help provide fluent APIs
 *
 * @author <a href="http://www.campudus.com/">Joern Bernhardt</a>
 * @author Galder Zamarreño
 */
trait TCPSupport[+S <: TCPSupport[S]] extends Any
  with NetworkSupport[S] {

  override type J <: JTCPSupport[_]

  /**
   *
   * @return the value of TCP so linger
   */
  def getSoLinger: Int = asJava.getSoLinger

  /**
   *
   * @return true if TCP keep alive is enabled
   */
  def isTCPKeepAlive: Boolean = asJava.isTCPKeepAlive

  /**
   * @return true if Nagle's algorithm is disabled.
   */
  def isTCPNoDelay: Boolean = asJava.isTCPNoDelay

  /**
   * @return `true` if pooled buffers are used
   */
  def isUsePooledBuffers: Boolean = asJava.isUsePooledBuffers

  /**
   * Set the TCP soLinger setting for connections created by this instance to `linger`.
   * Using a negative value will disable soLinger.
   * @return a reference to this so multiple method calls can be chained together
   *
   */
  def setSoLinger(linger: Int): S = wrap(asJava.setSoLinger(linger))

  /**
   * Set the TCP keepAlive setting for connections created by this instance to `keepAlive`.
   * @return a reference to this so multiple method calls can be chained together
   */
  def setTCPKeepAlive(keepAlive: Boolean): S = wrap(asJava.setTCPKeepAlive(keepAlive))

  /**
   * If `tcpNoDelay` is set to `true` then <a href="http://en.wikipedia.org/wiki/Nagle's_algorithm">Nagle's algorithm</a>
   * will turned <b>off</b> for the TCP connections created by this instance.
   * @return a reference to this so multiple method calls can be chained together
   */
  def setTCPNoDelay(tcpNoDelay: Boolean): S = wrap(asJava.setTCPNoDelay(tcpNoDelay))

  /**
   * Set if vertx should use pooled buffers for performance reasons. Doing so will give the best throughput but
   * may need a bit higher memory footprint.
   * @return a reference to this so multiple method calls can be chained together
   */
  def setUsePooledBuffers(pooledBuffers: Boolean): S = wrap(asJava.setUsePooledBuffers(pooledBuffers))
}