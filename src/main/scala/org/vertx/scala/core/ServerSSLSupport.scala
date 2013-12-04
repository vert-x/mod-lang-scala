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

import org.vertx.java.core.{ ServerSSLSupport => JServerSSLSupport }
import org.vertx.scala.Self

/**
 * Supports [[org.vertx.java.core.ServerSSLSupport]] functionality.
 * @tparam S self type to help provide fluent APIs
 *
 * @author <a href="http://www.campudus.com/">Joern Bernhardt</a>
 * @author Galder Zamarreño
 */
trait ServerSSLSupport extends Self
  with SSLSupport {

  override type J <: JServerSSLSupport[_]

  /**
   * Is client auth required?
   */
  def isClientAuthRequired(): Boolean = asJava.isClientAuthRequired()

  /**
   * Set {@code required} to true if you want the server to request client authentication from any connecting clients. This
   * is an extra level of security in SSL, and requires clients to provide client certificates. Those certificates must be added
   * to the server trust store.
   * @return A reference to this, so multiple invocations can be chained together.
   */
  def setClientAuthRequired(required: Boolean): this.type = wrap(asJava.setClientAuthRequired(required))

}