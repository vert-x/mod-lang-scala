package org.vertx.scala.http

import org.vertx.java.core.http.{ServerWebSocket => JServerWebSocket}
import org.vertx.java.core.Handler

object ServerWebSocketHandler1 {
  def apply(socket: (ServerWebSocket) => Unit) =
    new ServerWebSocketHandler1(socket)
}

class ServerWebSocketHandler1(delegate: (ServerWebSocket) => Unit) extends Handler[JServerWebSocket] {

  implicit def convertJavaToScalaWebSocket(jsocket: JServerWebSocket):ServerWebSocket = {
    ServerWebSocket(jsocket)
  }

  def handle(jsocket: JServerWebSocket) {
    delegate(jsocket)
  }

}