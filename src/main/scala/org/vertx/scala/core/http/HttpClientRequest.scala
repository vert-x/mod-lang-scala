/*
 * Copyright 2011-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"): 
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
import org.vertx.scala.core.buffer.Buffer
import org.vertx.scala.core.FunctionConverters._
import org.vertx.scala.core.MultiMap
import org.vertx.java.core.http.{ HttpClientRequest => JHttpClientRequest }
import org.vertx.scala.core.streams.WriteStream
import org.vertx.scala.Self

/**
 * Represents a client-side HTTP request.<p>
 * Instances are created by an [[org.vertx.scala.core.http.HttpClient]] instance, via one of the methods corresponding to the
 * specific HTTP methods, or the generic [[org.vertx.scala.core.http.HttpClient.request]] method.<p>
 * Once a request has been obtained, headers can be set on it, and data can be written to its body if required. Once
 * you are ready to send the request, the `end()` method should be called.<p>
 * Nothing is actually sent until the request has been internally assigned an HTTP connection. The [[org.vertx.scala.core.http.HttpClient]]
 * instance will return an instance of this class immediately, even if there are no HTTP connections available in the pool. Any requests
 * sent before a connection is assigned will be queued internally and actually sent when an HTTP connection becomes
 * available from the pool.<p>
 * The headers of the request are actually sent either when the `end()` method is called, or, when the first
 * part of the body is written, whichever occurs first.<p>
 * This class supports both chunked and non-chunked HTTP.<p>
 * It implements [[org.vertx.java.core.streams.WriteStream]] so it can be used with
 * [[org.vertx.java.core.streams.Pump]] to pump data with flow control.<p>
 * An example of using this class is as follows:
 * <p>
 * <pre>
 *
 * val req = httpClient.post("/some-url", { (response: HttpClientResponse) =>
 *   println("Got response: " + response.statusCode)
 * })
 *
 * req.headers().put("some-header", "hello")
 *     .put("Content-Length", 5)
 *     .write(new Buffer(Array[Byte](1, 2, 3, 4, 5)))
 *     .write(new Buffer(Array[Byte](6, 7, 8, 9, 10)))
 *     .end()
 *
 * </pre>
 * Instances of HttpClientRequest are not thread-safe.
 *
 * @author <a href="http://tfox.org">Tim Fox</a>
 * @author <a href="http://www.campudus.com/">Joern Bernhardt</a>
 * @author Galder Zamarreño
 */
final class HttpClientRequest private[scala] (val asJava: JHttpClientRequest) extends Self
  with WriteStream {

  override type J = JHttpClientRequest

  /**
   * If chunked is true then the request will be set into HTTP chunked mode.
   *
   * @param chunked True if you want the request to be in chunked mode.
   * @return A reference to this, so multiple method calls can be chained.
   */
  def setChunked(chunked: Boolean): HttpClientRequest = wrap(asJava.setChunked(chunked))

  /**
   * Checks whether the request is chunked.
   *
   * @return True if the request is chunked.
   */
  def isChunked: Boolean = asJava.isChunked

  /**
   * Returns the HTTP headers.
   *
   * This method converts a Java collection into a Scala collection every
   * time it gets called, so use it sensibly.
   *
   * @return The HTTP headers.
   */
  def headers(): MultiMap = multiMapToScalaMultiMap(asJava.headers())

  /**
   * Put an HTTP header - fluent API.
   *
   * @param name The header name
   * @param values The header values
   * @return A reference to this, so multiple method calls can be chained.
   */
  def putHeader(name: String, values: String*): HttpClientRequest =
    wrap(asJava.putHeader(name, values.asJava))

  /**
   * Write a [[java.lang.String]] to the request body, encoded in UTF-8.
   *
   * @return A reference to this, so multiple method calls can be chained.
   */
  def write(chunk: String): HttpClientRequest = wrap(asJava.write(chunk))

  /**
   * Write a [[java.lang.String]] to the request body, encoded using the encoding `enc`.
   *
   *@return A reference to this, so multiple method calls can be chained.
   */
  def write(chunk: String, enc: String): HttpClientRequest = wrap(asJava.write(chunk, enc))

  /**
   * If you send an HTTP request with the header `Expect` set to the value `100-continue`
   * and the server responds with an interim HTTP response with a status code of `100` and a continue handler
   * has been set using this method, then the `handler` will be called.<p>
   * You can then continue to write data to the request body and later end it. This is normally used in conjunction with
   * the [[org.vertx.scala.core.http.HttpClientRequest.sendHead()]] method to force the request header to be written before the request has ended.
   * @return A reference to this, so multiple method calls can be chained.
   */
  def continueHandler(handler: => Unit): HttpClientRequest =
    wrap(asJava.continueHandler(lazyToVoidHandler(handler)))

  /**
   * Forces the head of the request to be written before [[org.vertx.scala.core.http.HttpClientRequest.end()]] is called on the request or
   * any data is written to it. This is normally used to implement HTTP 100-continue handling, see
   * [[org.vertx.scala.core.http.HttpClientRequest.continueHandler(org.vertx.java.core.Handler)]] for more information.
   *
   * @return A reference to this, so multiple method calls can be chained.
   */
  def sendHead(): HttpClientRequest = wrap(asJava.sendHead())

  /**
   * Same as [[org.vertx.scala.core.http.HttpClientRequest.end(Buffer)]] but writes a String with the default encoding.
   */
  def end(chunk: String): Unit = asJava.end(chunk)

  /**
   * Same as [[org.vertx.scala.core.http.HttpClientRequest.end(Buffer)]] but writes a String with the specified encoding.
   */
  def end(chunk: String, enc: String): Unit = asJava.end(chunk, enc)

  /**
   * Same as [[org.vertx.scala.core.http.HttpClientRequest.end()]] but writes some data to the request body before ending. If the request is not chunked and
   * no other data has been written then the Content-Length header will be automatically set.
   */
  def end(chunk: Buffer): Unit = asJava.end(chunk.asJava)

  /**
   * Ends the request. If no data has been written to the request body, and [[org.vertx.scala.core.http.HttpClientRequest.sendHead()]] has not been called then
   * the actual request won't get written until this method gets called.<p>
   * Once the request has ended, it cannot be used any more, and if keep alive is true the underlying connection will
   * be returned to the [[org.vertx.scala.core.http.HttpClient]] pool so it can be assigned to another request.
   */
  def end(): Unit = asJava.end()

  /**
   * Sets the amount of time after which if a response is not received TimeoutException()
   * will be sent to the exception handler of this request. Calling this method more than once
   * has the effect of canceling any existing timeout and starting the timeout from scratch.
   *
   * @param timeoutMs The quantity of time in milliseconds.
   * @return A reference to this, so multiple method calls can be chained.
   */
  def setTimeout(timeoutMs: Long): HttpClientRequest = wrap(asJava.setTimeout(timeoutMs))

}

/** Factory for [[org.vertx.scala.core.http.HttpClientRequest]] instances, by wrapping a Java instance. */
object HttpClientRequest {
  def apply(internal: JHttpClientRequest) = new HttpClientRequest(internal)
}
