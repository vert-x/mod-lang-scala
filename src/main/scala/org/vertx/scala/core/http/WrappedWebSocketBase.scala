package org.vertx.scala.core.http

import org.vertx.java.core.http.WebSocketBase
import org.vertx.scala.core.streams.WrappedReadWriteStream
import org.vertx.java.core.Handler

trait WrappedWebSocketBase extends WrappedReadWriteStream {
  override type InternalType <: WebSocketBase[_]

  def binaryHandlerID(): String = internal.binaryHandlerID()
  def close(): Unit = internal.close()
  def closeHandler(handler: Handler[Void]): this.type = wrap(internal.closeHandler(handler))
  def textHandlerID(): String = internal.textHandlerID()
  def writeBinaryFrame(data: org.vertx.java.core.buffer.Buffer): this.type = wrap(internal.writeBinaryFrame(data))
  def writeTextFrame(str: String): this.type = wrap(internal.writeTextFrame(str))
}