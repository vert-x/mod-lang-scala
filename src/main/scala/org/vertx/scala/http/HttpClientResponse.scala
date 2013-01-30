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

package org.vertx.scala.http

import org.vertx.java.core.http.{HttpClientResponse => JHttpClientResponse}
import scala.collection.JavaConverters._
import org.vertx.java.core.buffer.Buffer
import org.vertx.scala.handlers.FunctionHandler1
import org.vertx.scala.handlers.FunctionHandler0
import org.vertx.scala.streams.ReadStream

/**
 * @author swilliams
 * 
 */
object HttpClientResponse {
  def apply(internal: JHttpClientResponse) = 
    new HttpClientResponse(internal)
}

class HttpClientResponse(val internal: JHttpClientResponse) extends ReadStream {

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

  def bodyHandler(handler: (Buffer) => Unit):HttpClientResponse.this.type = {
    internal.bodyHandler(new FunctionHandler1(handler))
    this
  }

  def dataHandler(handler: (Buffer) => Unit):HttpClientResponse.this.type = {
    internal.dataHandler(new FunctionHandler1(handler))
    this
  }

  def endHandler(handler: () => Unit):HttpClientResponse.this.type = {
    internal.endHandler(new FunctionHandler0(handler))
    this
  }

  def exceptionHandler(handler: (Exception) => Unit):HttpClientResponse.this.type = {
    internal.exceptionHandler(new FunctionHandler1(handler))
    this
  }

  def pause():HttpClientResponse.this.type = {
    internal.pause()
    this
  }

  def resume():HttpClientResponse.this.type = {
    internal.resume()
    this
  }
}