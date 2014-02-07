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
import java.net.InetSocketAddress
import org.vertx.scala.core.FunctionConverters._
import org.vertx.scala.core.streams.{WriteStream, ReadStream}
import org.vertx.scala.Self
import org.vertx.scala.core._

/**
 * Represents a socket-like interface to a TCP/SSL connection on either the
 * client or the server side.<p>
 * Instances of this class are created on the client side by an [[org.vertx.scala.core.net.NetClient]]
 * when a connection to a server is made, or on the server side by a [[org.vertx.scala.core.net.NetServer]]
 * when a server accepts a connection.<p>
 * It implements both [[org.vertx.scala.core.streams.ReadStream]] and [[org.vertx.scala.core.streams.WriteStream]] so it can be used with
 * [[org.vertx.java.core.streams.Pump]] to pump data with flow control.<p>
 * Instances of this class are not thread-safe.<p>
 *
 * @author <a href="http://tfox.org">Tim Fox</a>
 * @author swilliams
 * @author <a href="http://www.campudus.com/">Joern Bernhardt</a>
 * @author Galder Zamarreño
 */
final class NetSocket private[scala] (val asJava: JNetSocket) extends Self
  with ReadStream
  with WriteStream {

  override type J = JNetSocket

  /**
   * When a `NetSocket` is created it automatically registers an event handler with the event bus, the ID of that
   * handler is given by `writeHandlerID`.<p>
   * Given this ID, a different event loop can send a buffer to that event handler using the event bus and
   * that buffer will be received by this instance in its own event loop and written to the underlying connection. This
   * allows you to write data to other connections which are owned by different event loops.
   */
  def writeHandlerID(): String = asJava.writeHandlerID

  /**
   * Write a [[java.lang.String]] to the connection, encoded in UTF-8.
   * @return A reference to this, so multiple method calls can be chained.
   */
  def write(data: String): NetSocket = wrap(asJava.write(data))

  /**
   * Write a [[java.lang.String]] to the connection, encoded using the encoding `enc`.
   * @return A reference to this, so multiple method calls can be chained.
   */
  def write(data: String, enc: String): NetSocket = wrap(asJava.write(data, enc))

  /**
   * Tell the kernel to stream a file as specified by `filename` directly from disk to the outgoing connection,
   * bypassing userspace altogether (where supported by the underlying operating system. This is a very efficient way to stream files.
   */
  def sendFile(filename: String): NetSocket = wrap(asJava.sendFile(filename))

  /**
   * Same as [[NetSocket.sendFile()]] but also takes a handler that will be
   * called when the send has completed or a failure has occurred
   */
  def sendFile(filename: String, handler: AsyncResult[Void] => Unit): NetSocket =
    wrap(asJava.sendFile(filename, fnToHandler(handler)))

  /**
   * Return the remote address for this socket
   */
  def remoteAddress(): InetSocketAddress = asJava.remoteAddress()

  /**
   * Return the local address for this socket
   */
  def localAddress(): InetSocketAddress = asJava.localAddress()

  /**
   * Close the NetSocket
   */
  def close(): Unit = asJava.close()

  /**
   * Set a handler that will be called when the NetSocket is closed
   */
  def closeHandler(handler: => Unit): NetSocket = wrap(asJava.closeHandler(handler))

  /**
   * Upgrade channel to use SSL/TLS. Be aware that for this to work SSL must be configured.
   */
  def ssl(handler: => Unit): NetSocket = wrap(asJava.ssl(handler))

  /**
   * Returns `true` if this [[org.vertx.scala.core.net.NetSocket]] is encrypted via SSL/TLS.
   */
  def isSsl: Boolean = asJava.isSsl
}

object NetSocket {
  def apply(socket: JNetSocket) = new NetSocket(socket)
}