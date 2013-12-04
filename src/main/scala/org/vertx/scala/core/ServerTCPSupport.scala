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

import org.vertx.java.core.{ ServerTCPSupport => JServerTCPSupport }
import org.vertx.scala.Self

/**
 * Supports [[org.vertx.java.core.ServerTCPSupport]] functionality.
 * @tparam S self type to help provide fluent APIs
 *
 * @author <a href="http://www.campudus.com/">Joern Bernhardt</a>
 * @author Galder Zamarreño
 */
trait ServerTCPSupport extends Self
  with TCPSupport {

  override type J <: JServerTCPSupport[_]

  /**
   * Set the accept backlog
   * @return a reference to this so multiple method calls can be chained together
   */
  def setAcceptBacklog(backlog: Int): this.type = wrap(asJava.setAcceptBacklog(backlog))

  /**
   *
   * @return The accept backlog
   */
  def getAcceptBacklog(): Int = asJava.getAcceptBacklog()
}