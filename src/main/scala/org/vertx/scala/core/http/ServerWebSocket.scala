/*
 * Copyright 2011-2013 the original author or authors.
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

package org.vertx.scala.core.http

import org.vertx.java.core.http.{ ServerWebSocket => JServerWebSocket }
import org.vertx.scala.core.streams.WrappedReadWriteStream

/**
 * @author swilliams
 * @author Galder Zamarreño
 * @author <a href="http://www.campudus.com/">Joern Bernhardt</a>
 */
object ServerWebSocket {
  def apply(socket: JServerWebSocket) = new ServerWebSocket(socket)
}

class ServerWebSocket(protected[this] val internal: JServerWebSocket) extends JServerWebSocket with WrappedWebSocketBase {
  override type InternalType = JServerWebSocket

  override def headers(): org.vertx.java.core.MultiMap = internal.headers()
  override def path(): String = internal.path()
  override def query(): String = internal.query()
  override def reject(): ServerWebSocket = wrap(internal.reject())
}