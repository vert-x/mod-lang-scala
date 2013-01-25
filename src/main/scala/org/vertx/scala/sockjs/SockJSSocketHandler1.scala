package org.vertx.scala.sockjs

import org.vertx.java.core.Handler
import org.vertx.java.core.sockjs.{SockJSSocket => JSockJSSocket}
import org.vertx.java.core.sockjs.{SockJSSocket => JSockJSSocket}


object SockJSSocketHandler1 {
  def apply(delegate: (SockJSSocket) => Unit) = 
    new SockJSSocketHandler1(delegate)
}

class SockJSSocketHandler1(delegate: (SockJSSocket) => Unit) extends Handler[JSockJSSocket] {

  implicit def convertJavaToScalaSockJSSocket(jsocket: JSockJSSocket):SockJSSocket = {
    SockJSSocket(jsocket)
  }

  def handle(jsocket: JSockJSSocket) {
    delegate(jsocket)
  }

}