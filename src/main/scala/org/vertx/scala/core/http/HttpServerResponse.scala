package org.vertx.scala.core.http

import org.vertx.java.core.http.{HttpServerResponse => JHttpServerResponse}
import org.vertx.java.core.buffer.Buffer
import org.vertx.scala.handlers.FunctionHandler1
import org.vertx.scala.handlers.FunctionHandler0
import scala.collection.JavaConversions._
import scala.collection.JavaConverters._
import org.vertx.scala.core.streams.WriteStream


object HttpServerResponse {
  def apply(internal: JHttpServerResponse) = 
    new HttpServerResponse(internal)
}

class HttpServerResponse(internal: JHttpServerResponse) extends WriteStream {

  def close(): Unit = {
    internal.close()
  }

  def closeHandler(handler: () => Unit): HttpServerResponse.this.type = {
    internal.closeHandler(FunctionHandler0(handler))
    this
  }

  def drainHandler(handler: () => Unit): HttpServerResponse.this.type = {
    internal.drainHandler(FunctionHandler0(handler))
    this
  }

  def exceptionHandler(handler: (Exception) => Unit): HttpServerResponse.this.type = {
    internal.exceptionHandler(FunctionHandler1(handler))
    this
  }

  def end(): Unit = {
    internal.end()
  }

  def end(chunk: Buffer): Unit = {
    internal.end(chunk)
  }

  def end(chunk: String): Unit = {
    internal.end(chunk)
  }

  def end(chunk: String, encoding: String): Unit = {
    internal.end(chunk, encoding)
  }

  def headers():Map[String, Object] = {
    mapAsScalaMapConverter(internal.headers()).asScala.toMap
  }

  def putHeader(name: String, value: Any): HttpServerResponse.this.type = {
    internal.putHeader(name, value)
    this
  }

  def putTrailer(name: String, value: Any): HttpServerResponse.this.type = {
    internal.putTrailer(name, value)
    this
  }

  def sendFile(name: String): HttpServerResponse.this.type = {
    internal.sendFile(name)
    this
  }

  def setChunked(value: Boolean): HttpServerResponse.this.type = {
    internal.setChunked(value)
    this
  }

  def statusCode():String = statusCode

  def statusCode(code: Int): HttpServerResponse.this.type = {
    internal.statusCode = code
    this
  }

  def statusMessage():String = internal.statusMessage

  def statusMessage(message: String): HttpServerResponse = {
    internal.statusMessage = message
    this
  }

  def setWriteQueueMaxSize(maxSize: Int): HttpServerResponse.this.type = {
    internal.setWriteQueueMaxSize(maxSize)
    this
  }

  def trailers():Map[String, Object] = {
    mapAsScalaMapConverter(internal.trailers()).asScala.toMap
  }

  def write(data: Buffer): HttpServerResponse = {
    internal.write(data)
    this
  }

  def write(data: Buffer, handler: () => Unit): HttpServerResponse.this.type = {
    internal.write(data, FunctionHandler0(handler))
    this
  }

  def writeBuffer(data: String): HttpServerResponse.this.type = {
    internal.write(data)
    this
  }

  def writeBuffer(data: String, handler: () => Unit): HttpServerResponse.this.type = {
    internal.write(data, FunctionHandler0(handler))
    this
  }

  def writeBuffer(data: String, encoding: String): HttpServerResponse.this.type = {
    internal.write(data, encoding)
    this
  }

  def writeBuffer(data: String, encoding: String, handler: () => Unit): HttpServerResponse.this.type = {
    internal.write(data, encoding, FunctionHandler0(handler))
    this
  }

  def writeBuffer(data: Buffer): HttpServerResponse.this.type = {
    internal.writeBuffer(data)
    this
  }

  def writeQueueFull():Boolean = internal.writeQueueFull()

}