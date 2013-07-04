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

import org.vertx.java.core.buffer.Buffer
import org.vertx.java.core.http.{WebSocketBase => JWebSocketBase}
import org.vertx.java.core.http.{WebSocket => JWebSocket}
import org.vertx.scala.core.FunctionConverters._
import org.vertx.java.core.Handler
import scala.xml.Node

/**
 * @author swilliams
 * @author Galder Zamarreño
 */
object WebSocket {
  def apply(jsocket: JWebSocket) = new WebSocket(jsocket)
}

class WebSocket(internal: JWebSocket) extends WebSocketBase[JWebSocket](internal)

abstract class WebSocketBase[T](internal: JWebSocketBase[T]) {

  def binaryHandlerID():String = internal.binaryHandlerID

  def pause(): T = internal.pause()

  def resume(): T = internal.resume()

  def textHandlerID(): String = internal.textHandlerID

  def close() {
    internal.close()
  }

  def setWriteQueueMaxSize(maxSize: Int): T = internal.setWriteQueueMaxSize(maxSize)

  def writeBinaryFrame(data: Buffer): T = internal.writeBinaryFrame(data)

  def writeBuffer(data: Buffer): T = internal.write(data)

  def writeTextFrame(data: String): T = internal.writeTextFrame(data)

  /**
   * Write XML to the websocket as a text frame.
   */
  def writeXml(xml: Node): T = internal.writeTextFrame(xml.toString())

  def writeQueueFull(): Boolean = internal.writeQueueFull()

  def dataHandler(handler: Buffer => Unit): T = internal.dataHandler(handler)

  def drainHandler(handler: () => Unit): T = internal.drainHandler(handler)

  def endHandler(handler: () => Unit): T = internal.endHandler(handler)

  def exceptionHandler(handler: Handler[Throwable]): T =
    internal.exceptionHandler(handler)

}