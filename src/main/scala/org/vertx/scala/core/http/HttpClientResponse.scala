package org.vertx.scala.core.http

import org.vertx.java.core.http.{HttpClientResponse => JHttpClientResponse}
import scala.collection.JavaConverters.asScalaBufferConverter
import scala.collection.JavaConverters.asScalaIteratorConverter
import scala.collection.JavaConverters.mapAsScalaMapConverter
import org.vertx.java.core.buffer.Buffer
import org.vertx.scala.handlers.FunctionHandler1
import org.vertx.scala.handlers.FunctionHandler0


object HttpClientResponse {
  def apply(internal: JHttpClientResponse) = 
    new HttpClientResponse(internal)
}

class HttpClientResponse(val internal: JHttpClientResponse) {

  def cookies():List[String] = {
    asScalaBufferConverter(internal.cookies()).asScala.toList
  }

  def headers():Map[String, String] = {
    mapAsScalaMapConverter(internal.headers()).asScala.toMap
  }

  def statusCode():Int = internal.statusCode

  def statusMessage():String = internal.statusMessage

  def trailers():Map[String, String] = {
    mapAsScalaMapConverter(internal.trailers()).asScala.toMap
  }

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