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

import org.vertx.java.core.{ ClientSSLSupport => JClientSSLSupport }
import org.vertx.scala.Self

/**
 * Supports [[org.vertx.java.core.ClientSSLSupport]] functionality.
 *
 * @author <a href="http://www.campudus.com/">Joern Bernhardt</a>
 * @author Galder Zamarreño
 *
 */
trait ClientSSLSupport extends Self
  with SSLSupport {

  override type J <: JClientSSLSupport[_]

  /**
   * @return true if this client will trust all server certificates.
   */
  def isTrustAll: Boolean = asJava.isTrustAll

  /**
   * If you want an SSL client to trust *all* server certificates rather than match them
   * against those in its trust store, you can set this to true.<p>
   * Use this with caution as you may be exposed to "main in the middle" attacks
   * @param trustAll Set to true if you want to trust all server certificates
   */
  def setTrustAll(trustAll: Boolean): this.type = wrap(asJava.setTrustAll(trustAll))

}