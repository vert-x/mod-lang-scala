package org.vertx.scala.core.http

import org.vertx.java.core.Handler
import org.vertx.java.core.http.{HttpServerRequest => JHttpServerRequest}

object HttpServerRequestHandler1 {
  def apply(request: (HttpServerRequest) => Unit) =
    new HttpServerRequestHandler1(request)
}

class HttpServerRequestHandler1(delegate: (HttpServerRequest) => Unit) extends Handler[JHttpServerRequest] {

  implicit def convertJavaToScalaNetSocket(jreq: JHttpServerRequest):HttpServerRequest = {
    HttpServerRequest(jreq)
  }

  def handle(jreq: JHttpServerRequest) {
    delegate(jreq)
  }

}