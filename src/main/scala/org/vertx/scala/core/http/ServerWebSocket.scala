package org.vertx.scala.core.http

import org.vertx.java.core.buffer.{Buffer => JBuffer}
import org.vertx.java.core.http.{ServerWebSocket => JServerWebSocket}
import org.vertx.java.core.buffer.{Buffer => JBuffer}
import org.vertx.java.core.http.{ServerWebSocket => JServerWebSocket}
import org.vertx.scala.handlers.FunctionHandler0
import org.vertx.scala.handlers.FunctionHandler1

class ServerWebSocket(val internal: JServerWebSocket) {

  // TODO rest of methods

  def reject():Unit = internal.reject

  def close():Unit = internal.close

  def closeHandler(handler: () => Unit):Unit = internal.closedHandler(FunctionHandler0(handler))

  def dataHandler(handler: (JBuffer) => Unit):Unit = internal.dataHandler(FunctionHandler1(handler))

  def drainHandler(handler: () => Unit):Unit = internal.drainHandler(FunctionHandler0(handler))

  def endHandler(handler: () => Unit):Unit = internal.endHandler(FunctionHandler0(handler))

  def exceptionHandler(handler: (Exception) => Unit):Unit = internal.exceptionHandler(FunctionHandler1(handler))

}