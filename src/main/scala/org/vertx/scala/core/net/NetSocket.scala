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

package org.vertx.scala.core.net

import org.vertx.java.core.net.{ NetSocket => JNetSocket }
import org.vertx.java.core.buffer.Buffer
import java.net.InetSocketAddress
import org.vertx.java.core.Handler
import org.vertx.java.core.streams.{ WriteStream, ReadStream }
import org.vertx.scala.core.FunctionConverters._
import org.vertx.scala.core.streams.WrappedReadWriteStream

/**
 * Represents a socket-like interface to a TCP/SSL connection on either the
 * client or the server side.<p>
 * Instances of this class are created on the client side by an {@link NetClient}
 * when a connection to a server is made, or on the server side by a {@link NetServer}
 * when a server accepts a connection.<p>
 * It implements both {@link ReadStream} and {@link WriteStream} so it can be used with
 * {@link org.vertx.java.core.streams.Pump} to pump data with flow control.<p>
 * Instances of this class are not thread-safe.<p>
 *
 * @author <a href="http://tfox.org">Tim Fox</a>
 * @author swilliams
 * @author <a href="http://www.campudus.com/">Joern Bernhardt</a>
 */
class NetSocket(val internal: JNetSocket) extends JNetSocket with WrappedReadWriteStream {
  override type InternalType = JNetSocket

  override def writeHandlerID(): String = internal.writeHandlerID

  override def write(data: String): NetSocket = wrap(internal.write(data))

  override def write(data: String, enc: String): NetSocket = wrap(internal.write(data, enc))

  override def sendFile(filename: String): NetSocket = wrap(internal.sendFile(filename))

  override def remoteAddress(): InetSocketAddress = internal.remoteAddress()

  override def localAddress(): InetSocketAddress = internal.localAddress()

  override def close(): Unit = internal.close()

  override def closeHandler(handler: Handler[Void]): NetSocket = wrap(internal.closeHandler(handler))
  def closeHandler(handler: () => Unit): NetSocket = wrap(internal.closeHandler(handler))

}

object NetSocket {
  def apply(socket: JNetSocket) = new NetSocket(socket)
}