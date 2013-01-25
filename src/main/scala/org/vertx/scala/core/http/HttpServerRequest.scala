package org.vertx.scala.core.http

import org.vertx.java.core.http.{HttpServerRequest => JHttpServerRequest}
import org.vertx.scala.handlers.FunctionHandler1
import org.vertx.scala.handlers.FunctionHandler0
import org.vertx.java.core.buffer.Buffer
import scala.collection.JavaConverters._

object HttpServerRequest {
  def apply(internal: JHttpServerRequest) = 
    new HttpServerRequest(internal)
}

class HttpServerRequest(val internal: JHttpServerRequest) {

  def headers():Map[String, String] = {
    mapAsScalaMapConverter(internal.headers()).asScala.toMap
  }

  def method():String = internal.method

  def path():String = internal.path

  def params():Map[String, String] = {
    mapAsScalaMapConverter(internal.params()).asScala.toMap
  }

  def query():String = internal.query

  def response():HttpServerResponse = HttpServerResponse(internal.response)

  def uri():String = internal.uri

  def bodyHandler(handler: (Buffer) => Unit):Unit = {
    internal.bodyHandler(new FunctionHandler1(handler))
  }

  def dataHandler(handler: (Buffer) => Unit):Unit = {
    internal.dataHandler(new FunctionHandler1(handler))
  }

  def endHandler(handler: () => Unit):Unit = {
    internal.endHandler(new FunctionHandler0(handler))
  }

  def exceptionHandler(handler: (Exception) => Unit):Unit = {
    internal.exceptionHandler(new FunctionHandler1(handler))
  }

  def pause():Unit = internal.pause()

  def resume():Unit = internal.resume()

}