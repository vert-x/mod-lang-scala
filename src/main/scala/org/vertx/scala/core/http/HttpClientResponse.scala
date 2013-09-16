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

// FIXME Get rid of Java types
import scala.collection.JavaConverters._
import org.vertx.java.core.buffer.Buffer
import org.vertx.java.core.http.{ HttpClientResponse => JHttpClientResponse }
import org.vertx.scala.core.FunctionConverters._
import org.vertx.scala.core.Handler
import org.vertx.scala.core.streams.WrappedReadWriteStream
import org.vertx.scala.core.streams.WrappedReadStream
import org.vertx.scala.core.streams.WrappedReadStream
import org.vertx.scala.core.MultiMap
import org.vertx.scala.core.net.NetSocket

/**
 * @author swilliams
 * @author <a href="http://www.campudus.com/">Joern Bernhardt</a>
 */
object HttpClientResponse {
  def apply(internal: JHttpClientResponse) = new HttpClientResponse(internal)
}

class HttpClientResponse(protected[this] val internal: JHttpClientResponse) extends JHttpClientResponse with WrappedReadStream {
  override type InternalType = JHttpClientResponse

  override def bodyHandler(bodyHandler: Handler[Buffer]): HttpClientResponse = wrap(internal.bodyHandler(bodyHandler))

  def bodyHandler(handler: Buffer => Unit): HttpClientResponse = wrap(internal.bodyHandler(handler))
  
  override def netSocket(): NetSocket = NetSocket(internal.netSocket())

  override def cookies(): java.util.List[String] = internal.cookies()

  override def headers(): MultiMap = internal.headers

  override def statusCode(): Int = internal.statusCode

  override def statusMessage(): String = internal.statusMessage

  override def trailers(): MultiMap = internal.trailers
}