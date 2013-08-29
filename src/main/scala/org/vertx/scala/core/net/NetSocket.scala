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
import org.vertx.scala.core.streams.WrappedReadAndWriteStream

/**
 * @author swilliams
 *
 */
object NetSocket {
  def apply(socket: JNetSocket) = new NetSocket(socket)
}

class NetSocket(val internal: JNetSocket) extends WrappedReadAndWriteStream[NetSocket, JNetSocket] {

  def writeHandlerID(): String = internal.writeHandlerID

  def write(data: String): NetSocket = wrap(internal.write(data))

  def write(data: String, enc: String): NetSocket = wrap(internal.write(data, enc))

  def sendFile(filename: String): NetSocket = wrap(internal.sendFile(filename))

  def remoteAddress(): InetSocketAddress = internal.remoteAddress()

  def localAddress(): InetSocketAddress = internal.localAddress()

  def close(): Unit = internal.close()

  def closeHandler(handler: () => Unit): NetSocket = wrap(internal.closeHandler(handler))

}