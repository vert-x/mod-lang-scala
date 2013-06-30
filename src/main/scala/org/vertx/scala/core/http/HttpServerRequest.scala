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

import org.vertx.java.core.buffer.Buffer
import org.vertx.java.core.http.{HttpServerRequest => JHttpServerRequest}
import org.vertx.scala.core.FunctionConverters._
import collection.mutable.MultiMap
import org.vertx.java.core.{Handler}


/**
 * @author swilliams
 * @author nfmelendez
 * @author Ranie Jade Ramiso
 * 
 */
object HttpServerRequest {
  def apply(internal: JHttpServerRequest) = 
    new HttpServerRequest(internal)
}

class HttpServerRequest(val internal: JHttpServerRequest) {

  def headers():MultiMap[String, String] = {
    internal.headers
  }

  def method():String = internal.method

  def path():String = internal.path

  def params():MultiMap[String, String] = {
    internal.params
  }

  def query():String = internal.query

  def response():HttpServerResponse = HttpServerResponse(internal.response)

  def uri():String = internal.uri

  def bodyHandler(handler: (Buffer) => Unit):HttpServerRequest.this.type = {
    internal.bodyHandler(handler)
    this
  }

  def dataHandler(handler: (Buffer) => Unit):HttpServerRequest.this.type = {
    internal.dataHandler(handler)
    this
  }

  def endHandler(handler: () => Unit):HttpServerRequest.this.type = {
    internal.endHandler(handler)
    this
  }

  def exceptionHandler(handler: Handler[Throwable]):HttpServerRequest.this.type = {
    internal.exceptionHandler(handler)
    this
  }

  def pause():HttpServerRequest.this.type = {
    internal.pause()
    this
  }

  def resume():HttpServerRequest.this.type = {
    internal.resume()
    this
  }

}