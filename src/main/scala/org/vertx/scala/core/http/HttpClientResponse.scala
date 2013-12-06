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

import org.vertx.java.core.http.{ HttpClientResponse => JHttpClientResponse }
import org.vertx.scala.core.MultiMap
import org.vertx.scala.core.buffer._
import org.vertx.scala.core.net.NetSocket
import org.vertx.scala.core.streams.ReadStream

/**
 * Represents a client-side HTTP response.<p>
 * An instance is provided to the user via a {@link org.vertx.java.core.Handler}
 * instance that was specified when one of the HTTP method operations, or the
 * generic {@link HttpClient#request(String, String, org.vertx.java.core.Handler)}
 * method was called on an instance of {@link HttpClient}.<p>
 * It implements {@link org.vertx.java.core.streams.ReadStream} so it can be used with
 * {@link org.vertx.java.core.streams.Pump} to pump data with flow control.<p>
 * Instances of this class are not thread-safe.<p>
 *
 * @author <a href="http://tfox.org">Tim Fox</a>
 * @author swilliams
 * @author <a href="http://www.campudus.com/">Joern Bernhardt</a>
 */
// constructor is private because users should use apply in companion
// extends AnyVal to avoid object allocation and improve performance
class HttpClientResponse private[scala] (val asJava: JHttpClientResponse) extends AnyVal
  with ReadStream[HttpClientResponse] {

  override type J = JHttpClientResponse
  override protected[this] def self: HttpClientResponse = this

  /**
   * Returns the HTTP status code of the response.
   *
   * @return The HTTP status code of the response.
   */
  def statusCode(): Int = asJava.statusCode

  /**
   * Returns the HTTP status message of the response.
   *
   * @return The HTTP status message of the response.
   */
  def statusMessage(): String = asJava.statusMessage

  /**
   * Returns the HTTP headers.
   *
   * @return The HTTP headers.
   */
  def headers(): MultiMap = asJava.headers

  /**
   * Returns the HTTP trailers.
   *
   * @return The HTTP trailers.
   */
  def trailers(): MultiMap = asJava.trailers

  /**
   * Returns the Set-Cookie headers (including trailers).
   *
   * @return The Set-Cookie headers (including trailers).
   */
  def cookies(): java.util.List[String] = asJava.cookies()

  /**
   * Convenience method for receiving the entire request body in one piece. This saves the user having to manually
   * set a data and end handler and append the chunks of the body until the whole body received.
   * Don't use this if your request body is large - you could potentially run out of RAM.
   *
   * @param handler This handler will be called after all the body has been received.
   */
  def bodyHandler(handler: Buffer => Unit): HttpClientResponse =
    wrap(asJava.bodyHandler(bufferHandlerToJava(handler)))

  /**
   * Get a net socket for the underlying connection of this request. USE THIS WITH CAUTION!
   * Writing to the socket directly if you don't know what you're doing can easily break the HTTP protocol.
   *
   * @return the net socket
   */
  def netSocket(): NetSocket = NetSocket(asJava.netSocket())

}

/** Factory for [[org.vertx.scala.core.http.HttpClient]] instances by wrapping a Java instance. */
object HttpClientResponse {
  def apply(internal: JHttpClientResponse) = new HttpClientResponse(internal)
}
