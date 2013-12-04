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

import org.vertx.java.core.{NetworkSupport => JNetworkSupport}
import org.vertx.scala.{Self, AsJava}

/**
 * Offers methods that can be used to configure a service that provide network services.
 *
 * @author <a href="mailto:nmaurer@redhat.com">Norman Maurer</a>
 * @author <a href="http://www.campudus.com/">Joern Bernhardt</a>
 * @author Galder Zamarreño
 */
trait NetworkSupport extends Self with AsJava {

  override type J <: JNetworkSupport[_]

  /**
   * Set the send buffer size for connections created by this instance to `size` in bytes.
   * @return a reference to this so multiple method calls can be chained together
   */
  def setSendBufferSize(size: Int): this.type = wrap(asJava.setSendBufferSize(size))

  /**
   * Set the receive buffer size for connections created by this instance to `size` in bytes.
   * @return a reference to this so multiple method calls can be chained together
   */
  def setReceiveBufferSize(size: Int): this.type = wrap(asJava.setReceiveBufferSize(size))

  /**
   * Set the reuseAddress setting for connections created by this instance to `reuse`.
   * @return a reference to this so multiple method calls can be chained together
   */
  def setReuseAddress(reuse: Boolean): this.type = wrap(asJava.setReuseAddress(reuse))

  /**
   * Set the trafficClass setting for connections created by this instance to `trafficClass`.
   * @return a reference to this so multiple method calls can be chained together
   */
  def setTrafficClass(trafficClass: Int): this.type = wrap(asJava.setTrafficClass(trafficClass))

  /**
   * @return The send buffer size
   */
  def getSendBufferSize: Int = asJava.getSendBufferSize

  /**
   * @return The receive buffer size
   */
  def getReceiveBufferSize: Int = asJava.getReceiveBufferSize

  /**
   * @return The value of reuse address
   */
  def isReuseAddress: Boolean = asJava.isReuseAddress

  /**
   * @return the value of traffic class
   */
  def getTrafficClass: Int = asJava.getTrafficClass

}
