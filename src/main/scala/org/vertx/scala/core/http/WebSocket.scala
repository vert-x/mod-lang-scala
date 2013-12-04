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

import org.vertx.java.core.http.{ WebSocket => JWebSocket }
import org.vertx.scala.Self

/**
 * Represents a client side WebSocket.<p>
 * Instances of this class are not thread-safe<p>
 *
 * @author <a href="http://tfox.org">Tim Fox</a>
 * @author swilliams
 * @author Galder Zamarreño
 * @author <a href="http://www.campudus.com/">Joern Bernhardt</a>
 */
final class WebSocket private[scala] (val asJava: JWebSocket) extends Self
  with WebSocketBase {

  override type J = JWebSocket

}

/** Factory for [[org.vertx.scala.core.http.WebSocket]] instances. */
object WebSocket {
  def apply(jsocket: JWebSocket) = new WebSocket(jsocket)
}
