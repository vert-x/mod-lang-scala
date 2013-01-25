package org.vertx.scala.http

import org.vertx.java.core.Handler
import org.vertx.java.core.http.{WebSocket => JWebSocket}

object WebSocketHandler1 {
  def apply(delegate: (WebSocket) => Unit) = 
    new WebSocketHandler1(delegate)
}

class WebSocketHandler1(delegate: (WebSocket) => Unit) extends Handler[JWebSocket] {

  implicit def convertJavaToScalaWebSocket(jsocket: JWebSocket):WebSocket = {
    WebSocket(jsocket)
  }

  def handle(jsocket: JWebSocket):Unit = {
    delegate(jsocket)
  }

}