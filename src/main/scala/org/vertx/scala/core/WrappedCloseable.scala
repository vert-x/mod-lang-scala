package org.vertx.scala.core

import org.vertx.java.core.Handler
import org.vertx.java.core.AsyncResult
import org.vertx.scala.VertxWrapper

trait WrappedCloseable extends VertxWrapper {
  type CloseType = {
    def close(): Unit
    def close(handler: Handler[AsyncResult[Void]]): Unit
  }
  override type InternalType <: CloseType

  def close(): Unit = internal.close()
  def close(handler: Handler[AsyncResult[Void]]): Unit = internal.close(handler)

}