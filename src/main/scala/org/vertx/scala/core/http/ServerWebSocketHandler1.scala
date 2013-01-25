package org.vertx.scala.core.http

import org.vertx.java.core.http.{ServerWebSocket => JServerWebSocket}
import org.vertx.java.core.Handler

object ServerWebSocketHandler1 {
  def apply(socket: (ServerWebSocket) => Unit) =
    new ServerWebSocketHandler1(socket)
}

class ServerWebSocketHandler1(delegate: (ServerWebSocket) => Unit) extends Handler[JServerWebSocket] {

  implicit def convertJavaToScalaNetSocket(jsocket: JServerWebSocket):ServerWebSocket = {
    ServerWebSocket(jsocket)
  }

  def handle(jsocket: JServerWebSocket) {
    delegate(jsocket)
  }

}