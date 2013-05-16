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

import scala.collection.JavaConversions._
import scala.collection.JavaConverters._
import org.vertx.java.core.http.{HttpServerResponse => JHttpServerResponse}
import org.vertx.java.core.buffer.Buffer
import org.vertx.scala.core.FunctionConverters._
import org.vertx.scala.core.streams.WriteStream
import collection.mutable.{ HashMap, MultiMap, Set }
import org.vertx.java.core.{MultiMap => JMultiMap}

/**
 * @author swilliams, nfmelendez
 * 
 */
object HttpServerResponse {
  def apply(internal: JHttpServerResponse) = 
    new HttpServerResponse(internal)
}

class HttpServerResponse(internal: JHttpServerResponse) extends WriteStream {

    //Code duplicated in HttpClientResponse.scala
  def multiMapAsScalaMultiMapConverter (multiMap: JMultiMap) : MultiMap[Any, Any] = {
    val mm = new HashMap[Any, Set[Any]] with MultiMap[Any, Any]
    mm.addBinding("1", "a");
    //TODO: convert jmultimap to scala multimap
    mm
  }

  def close(): Unit = {
    internal.close()
  }

  def closeHandler(handler: () => Unit): HttpServerResponse.this.type = {
    internal.closeHandler(handler)
    this
  }

  def drainHandler(handler: () => Unit): HttpServerResponse.this.type = {
    internal.drainHandler(handler)
    this
  }

  def exceptionHandler(handler: (Throwable) => Unit): HttpServerResponse.this.type = {
    internal.exceptionHandler(handler)
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

  def headers():MultiMap[Any, Any] = {
    multiMapAsScalaMultiMapConverter(internal.headers())
  }

  //TODO: add also methods for Iterable<String>
  def putHeader(name: String, value: String): HttpServerResponse.this.type = {
    internal.putHeader(name, value)
    this
  }

  def putTrailer(name: String, value: String): HttpServerResponse.this.type = {
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

  def statusCode():Int = internal.getStatusCode()

  def statusCode(code: Int): HttpServerResponse.this.type = {
    internal.setStatusCode(code)
    this
  }

  def statusMessage():String = internal.getStatusMessage()

  def statusMessage(message: String): HttpServerResponse = {
    internal.setStatusMessage(message)
    this
  }

  def setWriteQueueMaxSize(maxSize: Int): HttpServerResponse.this.type = {
    internal.setWriteQueueMaxSize(maxSize)
    this
  }

  def trailers():MultiMap[Any, Any] = {
    multiMapAsScalaMultiMapConverter(internal.trailers())
  }

  def write(data: Buffer): HttpServerResponse = {
    internal.write(data)
    this
  }

  def write(data: Buffer, handler: () => Unit): HttpServerResponse.this.type = {
    internal.write(data, handler)
    this
  }

  def writeBuffer(data: String): HttpServerResponse.this.type = {
    internal.write(data)
    this
  }

  def writeBuffer(data: String, handler: () => Unit): HttpServerResponse.this.type = {
    internal.write(data, handler)
    this
  }

  def writeBuffer(data: String, encoding: String): HttpServerResponse.this.type = {
    internal.write(data, encoding)
    this
  }

  def writeBuffer(data: String, encoding: String, handler: () => Unit): HttpServerResponse.this.type = {
    internal.write(data, encoding, handler)
    this
  }

  def writeBuffer(data: Buffer): HttpServerResponse.this.type = {
    internal.writeBuffer(data)
    this
  }

  def writeQueueFull():Boolean = internal.writeQueueFull()

}