package org.vertx.scala.http

import org.vertx.java.core.buffer.Buffer
import org.vertx.java.core.http.{ServerWebSocket => JServerWebSocket}
import org.vertx.scala.handlers.FunctionHandler0
import org.vertx.scala.handlers.FunctionHandler1


object ServerWebSocket {
  def apply(socket: JServerWebSocket) =
    new ServerWebSocket(socket)
}

class ServerWebSocket(internal: JServerWebSocket) extends WebSocket(internal) {

  def path():String = internal.path

  def reject():ServerWebSocket = {
    internal.reject()
    this
  }

}