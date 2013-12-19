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

package org.vertx.scala.core.sockjs

import org.vertx.java.core.sockjs.{ SockJSSocket => JSockJSSocket }
import org.vertx.scala.core.streams.{WriteStream, ReadStream}
import org.vertx.scala.Self

/**
 * You interact with SockJS clients through instances of SockJS socket.
 * <p>The API is very similar to [[org.vertx.scala.core.http.WebSocket]]. It implements both
 * [[org.vertx.scala.core.streams.ReadStream]] and [[org.vertx.scala.core.streams.WriteStream]] so it can be used with
 * [[org.vertx.scala.core.streams.Pump]] to pump data with flow control.
 * <p>Instances of this class are not thread-safe.
 *
 * @author <a href="http://tfox.org">Tim Fox</a>
 * @author swilliams
 * @author <a href="http://www.campudus.com/">Joern Bernhardt</a>
 */
final class SockJSSocket private[scala] (val asJava: JSockJSSocket) extends Self
  with ReadStream
  with WriteStream {

  override type J = JSockJSSocket

  /**
   * When a `SockJSSocket` is created it automatically registers an event handler with the event bus, the ID of that
   * handler is given by `writeHandlerID`.<p>
   * Given this ID, a different event loop can send a buffer to that event handler using the event bus and
   * that buffer will be received by this instance in its own event loop and written to the underlying socket. This
   * allows you to write data to other sockets which are owned by different event loops.
   */
  def writeHandlerID(): String = asJava.writeHandlerID()

  /**
   * Close it
   */
  def close(): Unit = asJava.close()

}

/** Factory for [[org.vertx.scala.core.sockjs.SockJSSocket]] instances. */
object SockJSSocket {
  def apply(internal: JSockJSSocket) = new SockJSSocket(internal)
}
