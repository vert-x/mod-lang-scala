package org.vertx.scala.core.http

import org.vertx.java.core.http.{HttpClientResponse => JHttpClientResponse}
import scala.collection.JavaConverters.asScalaBufferConverter
import scala.collection.JavaConverters.asScalaIteratorConverter
import scala.collection.JavaConverters.mapAsScalaMapConverter
import org.vertx.java.core.buffer.Buffer
import org.vertx.scala.handlers.FunctionHandler1
import org.vertx.scala.handlers.FunctionHandler0
import org.vertx.scala.core.streams.WriteStream


object HttpClientResponse {
  def apply(internal: JHttpClientResponse) = 
    new HttpClientResponse(internal)
}

class HttpClientResponse(val internal: JHttpClientResponse) extends WriteStream {

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

  def bodyHandler(handler: (Buffer) => Unit):HttpClientResponse = {
    internal.bodyHandler(new FunctionHandler1(handler))
    this
  }

  def dataHandler(handler: (Buffer) => Unit):HttpClientResponse = {
    internal.dataHandler(new FunctionHandler1(handler))
    this
  }

  def endHandler(handler: () => Unit):HttpClientResponse = {
    internal.endHandler(new FunctionHandler0(handler))
    this
  }

  def exceptionHandler(handler: (Exception) => Unit):HttpClientResponse = {
    internal.exceptionHandler(new FunctionHandler1(handler))
    this
  }

  def pause():Unit = internal.pause()

  def resume():Unit = internal.resume()
}