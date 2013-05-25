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

import scala.collection.JavaConverters._
import org.vertx.java.core.buffer.Buffer
import org.vertx.java.core.http.{HttpServerRequest => JHttpServerRequest}
import org.vertx.scala.core.FunctionConverters._
import org.vertx.scala.core.streams.ReadStream
import collection.mutable.{ HashMap, MultiMap, Set }
import org.vertx.java.core.{MultiMap => JMultiMap}


/**
 * @author swilliams, nfmelendez
 * 
 */
object HttpServerRequest {
  def apply(internal: JHttpServerRequest) = 
    new HttpServerRequest(internal)
}

class HttpServerRequest(val internal: JHttpServerRequest) {

  //Code duplicated in HttpClientResponse.scala
  def multiMapAsScalaMultiMapConverter (multiMap: JMultiMap) : MultiMap[Any, Any] = {
    val mm = new HashMap[Any, Set[Any]] with MultiMap[Any, Any]
    mm.addBinding("1", "a");
    //TODO: convert jmultimap to scala multimap
    mm
  }


  def headers():MultiMap[Any, Any] = {
    multiMapAsScalaMultiMapConverter(internal.headers())
  }

  def method():String = internal.method

  def path():String = internal.path

  def params():MultiMap[Any, Any] = {
    multiMapAsScalaMultiMapConverter(internal.params())
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

  def exceptionHandler(handler: (Throwable) => Unit):HttpServerRequest.this.type = {
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