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
import org.vertx.scala.core.buffer._
import org.vertx.scala.core.FunctionConverters._
import org.vertx.scala.core.streams.WriteStream
import org.vertx.scala.core.{AsyncResult, MultiMap}
import collection.JavaConverters._
import org.vertx.scala.Self

/**
 * Represents a server-side HTTP response.<p>
 * Instances of this class are created and associated to every instance of
 * [[org.vertx.scala.core.http.HttpServerRequest]] that is created.<p>
 * It allows the developer to control the HTTP response that is sent back to the
 * client for a particular HTTP request. It contains methods that allow HTTP
 * headers and trailers to be set, and for a body to be written out to the response.<p>
 * It also allows files to be streamed by the kernel directly from disk to the
 * outgoing HTTP connection, bypassing user space altogether (where supported by
 * the underlying operating system). This is a very efficient way of
 * serving files from the server since buffers do not have to be read one by one
 * from the file and written to the outgoing socket.<p>
 * It implements [[org.vertx.scala.core.streams.WriteStream]] so it can be used with
 * [[org.vertx.scala.core.streams.Pump]] to pump data with flow control.<p>
 * Instances of this class are not thread-safe<p>
 *
 * @author <a href="http://tfox.org">Tim Fox</a>
 * @author <a href="http://www.campudus.com/">Joern Bernhardt</a>
 * @author Galder Zamarreño
 */
final class HttpServerResponse private[scala] (val asJava: JHttpServerResponse) extends Self
  with WriteStream {

  override type J = JHttpServerResponse

  /**
   * Close the underlying TCP connection
   */
  def close(): Unit = asJava.close()

  /**
   * Set a close handler for the response. This will be called if the underlying connection closes before the response
   * is complete.
   * @param handler callback when close has completed
   */
  def closeHandler(handler: => Unit): HttpServerResponse = wrap(asJava.closeHandler(handler))

  /**
   * Ends the response. If no data has been written to the response body,
   * the actual response won't get written until this method gets called.<p>
   * Once the response has ended, it cannot be used any more.
   */
  def end(): Unit = asJava.end()

  /**
   * Same as [[org.vertx.scala.core.http.HttpServerResponse.end()]] but writes some data to the response body before ending. If the response is not chunked and
   * no other data has been written then the Content-Length header will be automatically set
   */
  def end(chunk: Buffer): Unit = asJava.end(chunk.asJava)

  /**
   * Same as [[org.vertx.scala.core.http.HttpServerResponse.end(Buffer)]] but writes a String with the specified encoding before ending the response.
   */
  def end(chunk: String, enc: String): Unit = asJava.end(chunk, enc)

  /**
   * Same as [[org.vertx.scala.core.http.HttpServerResponse.end(Buffer)]] but writes a String with the default encoding before ending the response.
   */
  def end(chunk: String): Unit = asJava.end(chunk)

  /**
   * The HTTP status code of the response. The default is `200} representing `OK}.
   *
   * @return The status code.
   */
  def getStatusCode: Int = asJava.getStatusCode

  /**
   * The HTTP status message of the response. If this is not specified a default value will be used depending on what
   * [[org.vertx.scala.core.http.HttpServerResponse.setStatusCode]] has been set to.
   *
   * @return The status message.
   */
  def getStatusMessage: String = asJava.getStatusMessage

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
   * Is the response chunked?
   */
  def isChunked: Boolean = asJava.isChunked

  /**
   * Put an HTTP header - fluent API.
   *
   * @param name    The header name
   * @param values  The header values.
   * @return A reference to this, so multiple method calls can be chained.
   */
  def putHeader(name: String, values: String*): HttpServerResponse =
    wrap(asJava.putHeader(name, values.asJava))

  /**
   * Put an HTTP trailer - fluent API.
   *
   * @param name    The trailer name
   * @param values  The trailer values
   * @return A reference to this, so multiple method calls can be chained.
   */
  def putTrailer(name: String, values: Seq[String]): HttpServerResponse =
    wrap(asJava.putTrailer(name, values.asJava))

  /**
   * Put an HTTP trailer - fluent API.
   *
   * @param name The trailer name
   * @param value The trailer value
   * @return A reference to this, so multiple method calls can be chained.
   */
  def putTrailer(name: String, value: String): HttpServerResponse = wrap(asJava.putTrailer(name, value))

  /**
   * Same as [[org.vertx.scala.core.http.HttpServerResponse.sendFile(String)]] but also takes the path to a resource to serve if the resource is not found.
   */
  def sendFile(filename: String, notFoundFile: String): HttpServerResponse =
    wrap(asJava.sendFile(filename, notFoundFile))

  /**
   * Tell the kernel to stream a file as specified by `filename` directly
   * from disk to the outgoing connection, bypassing userspace altogether
   * (where supported by the underlying operating system.
   * This is a very efficient way to serve files.<p>
   *
   * @param filename The file to send.
   * @return A reference to this for a fluent API.
   */
  def sendFile(filename: String): HttpServerResponse = wrap(asJava.sendFile(filename))

  /**
   * If `chunked` is `true`, this response will use HTTP chunked encoding, and each call to write to the body
   * Same as [[org.vertx.scala.core.http.HttpServerResponse.sendFile(String)]]
   * but also takes a handler that will be called when the send has completed or
   * a failure has occurred
   */
  def sendFile(filename: String, handler: AsyncResult[Void] => Unit): HttpServerResponse =
    wrap(asJava.sendFile(filename, handler))

  /**
   * Same as [[org.vertx.scala.core.http.HttpServerResponse.sendFile(String, String)]]
   * but also takes a handler that will be called when the send has completed or
   * a failure has occurred
   */
  def sendFile(filename: String, notFoundFile: String, handler: AsyncResult[Void] => Unit): HttpServerResponse =
    wrap(asJava.sendFile(filename, notFoundFile, handler))

  /**
   * If `chunked` is `true`, this response will use HTTP chunked encoding, and each call to write to the body
   * will correspond to a new HTTP chunk sent on the wire.<p>
   * If chunked encoding is used the HTTP header `Transfer-Encoding` with a value of `Chunked` will be
   * automatically inserted in the response.<p>
   * If `chunked` is `false`, this response will not use HTTP chunked encoding, and therefore if any data is written the
   * body of the response, the total size of that data must be set in the `Content-Length` header <b>before</b> any
   * data is written to the response body.<p>
   * An HTTP chunked response is typically used when you do not know the total size of the request body up front.
   *
   * @param chunked Sets the mode to chunked (true) or not (false).
   * @return A reference to this, so multiple method calls can be chained.
   */
  def setChunked(chunked: Boolean): HttpServerResponse = wrap(asJava.setChunked(chunked))

  /**
   * Set the status code.
   *
   * @param statusCode The status code.
   * @return A reference to this, so multiple method calls can be chained.
   */
  def setStatusCode(statusCode: Int): HttpServerResponse = wrap(asJava.setStatusCode(statusCode))

  /**
   * Set the status message.
   *
   * @param statusMessage The status message.
   * @return A reference to this, so multiple method calls can be chained.
   */
  def setStatusMessage(statusMessage: String): HttpServerResponse = wrap(asJava.setStatusMessage(statusMessage))

  /**
   * Returns the HTTP trailers.
   *
   * This method converts a Java collection into a Scala collection every
   * time it gets called, so use it sensibly.
   *
   * @return The HTTP trailers.
   */
  def trailers(): MultiMap = multiMapToScalaMultiMap(asJava.trailers())

  /**
   * Write a [[java.lang.String]] to the response body, encoded in UTF-8.
   *
   * @param chunk The String to write.
   * @return A reference to this, so multiple method calls can be chained.
   */
  def write(chunk: String): HttpServerResponse = wrap(asJava.write(chunk))

  /**
   * Write a [[java.lang.String]] to the response body, encoded using the encoding `enc`.
   *
   * @param chunk The String to write.
   * @param enc The encoding to use.
   * @return A reference to this, so multiple method calls can be chained.
   */
  def write(chunk: String, enc: String): HttpServerResponse = wrap(asJava.write(chunk, enc))

}

/**
 * Factory for [[org.vertx.scala.core.http.HttpServerResponse]] instances.
 *
 * @author swilliams
 * @author nfmelendez
 * @author Ranie Jade Ramiso
 * @author <a href="http://www.campudus.com/">Joern Bernhardt</a>
 */
object HttpServerResponse {
  def apply(internal: JHttpServerResponse) = new HttpServerResponse(internal)
}
