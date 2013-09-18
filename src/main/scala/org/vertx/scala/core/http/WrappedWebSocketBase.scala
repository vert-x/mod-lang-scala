package org.vertx.scala.core.http

import org.vertx.java.core.http.WebSocketBase
import org.vertx.scala.core.streams.WrappedReadWriteStream
import org.vertx.scala.core.Handler
import org.vertx.scala.core.buffer.Buffer

trait WrappedWebSocketBase extends WrappedReadWriteStream {
  override type InternalType <: WebSocketBase[InternalType]

  def binaryHandlerID(): String = internal.binaryHandlerID()
  def close(): Unit = internal.close()
  def closeHandler(handler: Handler[Void]): this.type = wrap(internal.closeHandler(handler))
  def textHandlerID(): String = internal.textHandlerID()
  def writeBinaryFrame(data: Buffer): this.type = wrap(internal.writeBinaryFrame(data.toJava))
  def writeTextFrame(str: String): this.type = wrap(internal.writeTextFrame(str))
}