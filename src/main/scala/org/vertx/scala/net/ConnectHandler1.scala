package org.vertx.scala.net

import org.vertx.java.core.net.{NetSocket => JNetSocket}
import org.vertx.java.core.Handler
import org.vertx.java.core.net.{NetSocket => JNetSocket}

object ConnectHandler1 {
  def apply(socket: (NetSocket) => Unit) =
    new ConnectHandler1(socket)
}

class ConnectHandler1(delegate: (NetSocket) => Unit) extends Handler[JNetSocket] {

  implicit def convertJavaToScalaNetSocket(jsocket: JNetSocket):NetSocket = {
    NetSocket(jsocket)
  }

  def handle(jsocket: JNetSocket) {
    delegate(jsocket)
  }

}