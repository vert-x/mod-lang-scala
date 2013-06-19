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
import org.vertx.java.core.http.{HttpClientResponse => JHttpClientResponse}
import org.vertx.scala.core.FunctionConverters._
import collection.mutable.{ HashMap, MultiMap, Set }
import org.vertx.java.core.{Handler}

/**
 * @author swilliams
 * 
 */
object HttpClientResponse {
  def apply(internal: JHttpClientResponse) = 
    new HttpClientResponse(internal)
}


class HttpClientResponse(val internal: JHttpClientResponse) {


  def cookies():List[String] = {
    asScalaBufferConverter(internal.cookies()).asScala.toList
  }

  def headers(): MultiMap[String, String] = {
    internal.headers
  }

  def statusCode():Int = internal.statusCode

  def statusMessage():String = internal.statusMessage

  def trailers():MultiMap[String, String] = {
    internal.trailers
  }

  def bodyHandler(handler: Buffer => Unit):HttpClientResponse.this.type = {

    internal.bodyHandler(new Handler[Buffer] {
      override def handle(bf: Buffer) {
        handler(bf);
      }
    });

    this
  }

  def dataHandler(handler: Buffer => Unit):HttpClientResponse.this.type = {
    internal.dataHandler(handler)
    this
  }

  def endHandler(handler: () => Unit):HttpClientResponse.this.type = {
    internal.endHandler(handler)
    this
  }

  def exceptionHandler(handler: Handler[Throwable]):HttpClientResponse.this.type = {
    internal.exceptionHandler(handler)
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