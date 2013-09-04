/*
 * Copyright 2011-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.vertx.scala.core.http

import org.vertx.java.core.http.{ HttpServerResponse => JHttpServerResponse }
import org.vertx.java.core.buffer.Buffer
import org.vertx.scala.core.FunctionConverters._
import collection.mutable
import org.vertx.java.core.Handler
import org.vertx.scala.core.streams.WrappedWriteStream
import org.vertx.java.core.streams.{ WriteStream => JWriteStream }
import org.vertx.java.core.MultiMap

/**
 * @author swilliams
 * @author nfmelendez
 * @author Ranie Jade Ramiso
 *
 */
object HttpServerResponse {
  def apply(internal: JHttpServerResponse) =
    new HttpServerResponse(internal)
}

class HttpServerResponse(protected[this] val internal: JHttpServerResponse) extends JHttpServerResponse with WrappedWriteStream {
  override type InternalType = JHttpServerResponse with JWriteStream[JHttpServerResponse]

  override def close(): Unit = internal.close()
  override def closeHandler(handler: Handler[Void]): HttpServerResponse = wrap(internal.closeHandler(handler))
  def closeHandler(handler: () => Unit): HttpServerResponse = wrap(internal.closeHandler(handler))
  override def end(): Unit = internal.end()
  override def end(buffer: org.vertx.java.core.buffer.Buffer): Unit = internal.end(buffer)
  override def end(chunk: String, enc: String): Unit = internal.end(chunk, enc)
  override def end(chunk: String): Unit = internal.end(chunk)
  override def getStatusCode(): Int = internal.getStatusCode()
  override def getStatusMessage(): String = internal.getStatusMessage()
  override def headers(): org.vertx.java.core.MultiMap = internal.headers()
  override def isChunked(): Boolean = internal.isChunked()
  override def putHeader(name: String, values: java.lang.Iterable[String]): HttpServerResponse = wrap(internal.putHeader(name, values))
  override def putHeader(name: String, value: String): HttpServerResponse = wrap(internal.putHeader(name, value))
  override def putTrailer(name: String, values: java.lang.Iterable[String]): HttpServerResponse = wrap(internal.putTrailer(name, values))
  override def putTrailer(name: String, value: String): HttpServerResponse = wrap(internal.putTrailer(name, value))
  override def sendFile(filename: String, notFoundFile: String): HttpServerResponse = wrap(internal.sendFile(filename, notFoundFile))
  override def sendFile(filename: String): HttpServerResponse = wrap(internal.sendFile(filename))
  override def setChunked(chunked: Boolean): HttpServerResponse = wrap(internal.setChunked(chunked))
  override def setStatusCode(statusCode: Int): HttpServerResponse = wrap(internal.setStatusCode(statusCode))
  override def setStatusMessage(statusMessage: String): HttpServerResponse = wrap(internal.setStatusMessage(statusMessage))
  override def trailers(): MultiMap = internal.trailers()
  override def write(chunk: String): HttpServerResponse = wrap(internal.write(chunk))
  override def write(chunk: String, enc: String): HttpServerResponse = wrap(internal.write(chunk, enc))

  //  override def close(): Unit = internal.close()
  //
  //  def closeHandler(handler: () => Unit): HttpServerResponse = wrap(internal.closeHandler(handler))
  //
  //  def end(): Unit = {
  //    internal.end()
  //  }
  //
  //  def end(chunk: Buffer): Unit = {
  //    internal.end(chunk)
  //  }
  //
  //  def end(chunk: String): Unit = {
  //    internal.end(chunk)
  //  }
  //
  //  def end(chunk: String, encoding: String): Unit = {
  //    internal.end(chunk, encoding)
  //  }
  //
  //  def headers(): mutable.MultiMap[String, String] = {
  //    internal.headers
  //  }
  //
  //  //TODO: add also methods for Iterable<String>
  //  def putHeader(name: String, value: String): HttpServerResponse.this.type = {
  //    internal.putHeader(name, value)
  //    this
  //  }
  //
  //  def putTrailer(name: String, value: String): HttpServerResponse.this.type = {
  //    internal.putTrailer(name, value)
  //    this
  //  }
  //
  //  def sendFile(name: String): HttpServerResponse.this.type = {
  //    internal.sendFile(name)
  //    this
  //  }
  //
  //  def sendFile(name: String, notFoundFile: String): HttpServerResponse.this.type = {
  //    internal.sendFile(name, notFoundFile)
  //    this
  //  }
  //
  //  def setChunked(value: Boolean): HttpServerResponse.this.type = {
  //    internal.setChunked(value)
  //    this
  //  }
  //
  //  def statusCode(): Int = internal.getStatusCode()
  //
  //  def statusCode(code: Int): HttpServerResponse.this.type = {
  //    internal.setStatusCode(code)
  //    this
  //  }
  //
  //  def statusMessage(): String = internal.getStatusMessage()
  //
  //  def statusMessage(message: String): HttpServerResponse.this.type = {
  //    internal.setStatusMessage(message)
  //    this
  //  }
  //
  //  def setWriteQueueMaxSize(maxSize: Int): HttpServerResponse.this.type = {
  //    internal.setWriteQueueMaxSize(maxSize)
  //    this
  //  }
  //
  //  def trailers(): mutable.MultiMap[String, String] = {
  //    internal.trailers
  //  }
  //
  //  def write(data: Buffer): HttpServerResponse.this.type = {
  //    internal.write(data)
  //    this
  //  }
  //
  //  def write(data: String): HttpServerResponse.this.type = {
  //    internal.write(data)
  //    this
  //  }
  //
  //  def write(data: String, encoding: String): HttpServerResponse.this.type = {
  //    internal.write(data, encoding)
  //    this
  //  }
  //
  //  def writeQueueFull(): Boolean = internal.writeQueueFull()
  //
  //  def writeBuffer(data: Buffer): HttpServerResponse.this.type = {
  //    internal.write(data)
  //    this
  //  }

}
