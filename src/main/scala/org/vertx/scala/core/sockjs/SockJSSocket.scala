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
import org.vertx.scala.core.streams.WrappedReadWriteStream

/**
 * @author swilliams
 */
object SockJSSocket {
  def apply(internal: JSockJSSocket) =
    new SockJSSocket(internal)
}

/**
 * You interact with SockJS clients through instances of SockJS socket.
 * <p>The API is very similar to {@link org.vertx.java.core.http.WebSocket}. It implements both
 * {@link ReadStream} and {@link WriteStream} so it can be used with
 * {@link org.vertx.scala.core.streams.Pump} to pump data with flow control.
 * <p>Instances of this class are not thread-safe.
 *
 * @author <a href="http://tfox.org">Tim Fox</a>
 * @author swilliams
 * @author <a href="http://www.campudus.com/">Joern Bernhardt</a>
 */
class SockJSSocket(protected[this] val internal: JSockJSSocket) extends JSockJSSocket with WrappedReadWriteStream {
  override type InternalType = JSockJSSocket

  override def close(): Unit = internal.close()

  override def writeHandlerID(): String = internal.writeHandlerID()

}