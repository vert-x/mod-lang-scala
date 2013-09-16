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
import org.vertx.java.core.http.{ HttpServerResponse => JHttpServerResponse }
import org.vertx.java.core.buffer.Buffer
import org.vertx.scala.core.FunctionConverters._
import org.vertx.scala.core.Handler
import org.vertx.scala.core.streams.WrappedWriteStream
import org.vertx.scala.core.MultiMap

/**
 * @author swilliams
 * @author nfmelendez
 * @author Ranie Jade Ramiso
 * @author <a href="http://www.campudus.com/">Joern Bernhardt</a>
 */
object HttpServerResponse {
  def apply(internal: JHttpServerResponse) = new HttpServerResponse(internal)
}

/**
 * Represents a server-side HTTP response.<p>
 * Instances of this class are created and associated to every instance of
 * {@link HttpServerRequest} that is created.<p>
 * It allows the developer to control the HTTP response that is sent back to the
 * client for a particular HTTP request. It contains methods that allow HTTP
 * headers and trailers to be set, and for a body to be written out to the response.<p>
 * It also allows files to be streamed by the kernel directly from disk to the
 * outgoing HTTP connection, bypassing user space altogether (where supported by
 * the underlying operating system). This is a very efficient way of
 * serving files from the server since buffers do not have to be read one by one
 * from the file and written to the outgoing socket.<p>
 * It implements {@link WriteStream} so it can be used with
 * {@link org.vertx.java.core.streams.Pump} to pump data with flow control.<p>
 * Instances of this class are not thread-safe<p>
 *
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
class HttpServerResponse(protected[this] val internal: JHttpServerResponse) extends JHttpServerResponse with WrappedWriteStream {
  override type InternalType = JHttpServerResponse

  /**
   * Close the underlying TCP connection
   */
  override def close(): Unit = internal.close()

  /**
   * Set a close handler for the response. This will be called if the underlying connection closes before the response
   * is complete.
   * @param handler
   */
  override def closeHandler(handler: Handler[Void]): HttpServerResponse = wrap(internal.closeHandler(handler))

  /**
   * Set a close handler for the response. This will be called if the underlying connection closes before the response
   * is complete.
   * @param handler
   */
  def closeHandler(handler: () => Unit): HttpServerResponse = wrap(internal.closeHandler(handler))

  /**
   * Ends the response. If no data has been written to the response body,
   * the actual response won't get written until this method gets called.<p>
   * Once the response has ended, it cannot be used any more.
   */
  override def end(): Unit = internal.end()

  /**
   * Same as {@link #end()} but writes some data to the response body before ending. If the response is not chunked and
   * no other data has been written then the Content-Length header will be automatically set
   */
  override def end(chunk: org.vertx.java.core.buffer.Buffer): Unit = internal.end(chunk)

  /**
   * Same as {@link #end(Buffer)} but writes a String with the specified encoding before ending the response.
   */
  override def end(chunk: String, enc: String): Unit = internal.end(chunk, enc)

  /**
   * Same as {@link #end(Buffer)} but writes a String with the default encoding before ending the response.
   */
  override def end(chunk: String): Unit = internal.end(chunk)

  /**
   * The HTTP status code of the response. The default is {@code 200} representing {@code OK}.
   */
  override def getStatusCode(): Int = internal.getStatusCode()

  /**
   * The HTTP status message of the response. If this is not specified a default value will be used depending on what
   * {@link #setStatusCode} has been set to.
   */
  override def getStatusMessage(): String = internal.getStatusMessage()

  /**
   * @return The HTTP headers
   */
  override def headers(): org.vertx.java.core.MultiMap = internal.headers()

  /**
   * Is the response chunked?
   */
  override def isChunked(): Boolean = internal.isChunked()

  /**
   * Put an HTTP header - fluent API
   * @param name    The header name
   * @param values  The header values.
   * @return A reference to this, so multiple method calls can be chained.
   */
  override def putHeader(name: String, values: java.lang.Iterable[String]): HttpServerResponse = wrap(internal.putHeader(name, values))

  /**
   * Put an HTTP header - fluent API
   * @param name The header name
   * @param value The header value.
   * @return A reference to this, so multiple method calls can be chained.
   */
  override def putHeader(name: String, value: String): HttpServerResponse = wrap(internal.putHeader(name, value))

  /**
   * Put an HTTP trailer - fluent API
   * @param name    The trailer name
   * @param values  The trailer values
   * @return A reference to this, so multiple method calls can be chained.
   */
  override def putTrailer(name: String, values: java.lang.Iterable[String]): HttpServerResponse = wrap(internal.putTrailer(name, values))

  /**
   * Put an HTTP trailer - fluent API
   * @param name The trailer name
   * @param value The trailer value
   * @return A reference to this, so multiple method calls can be chained.
   */
  override def putTrailer(name: String, value: String): HttpServerResponse = wrap(internal.putTrailer(name, value))

  /**
   * Same as {@link #sendFile(String)} but also takes the path to a resource to serve if the resource is not found
   */
  override def sendFile(filename: String, notFoundFile: String): HttpServerResponse = wrap(internal.sendFile(filename, notFoundFile))

  /**
   * Tell the kernel to stream a file as specified by {@code filename} directly
   * from disk to the outgoing connection, bypassing userspace altogether
   * (where supported by the underlying operating system.
   * This is a very efficient way to serve files.<p>
   */
  override def sendFile(filename: String): HttpServerResponse = wrap(internal.sendFile(filename))

  /**
   * If {@code chunked} is {@code true}, this response will use HTTP chunked encoding, and each call to write to the body
   * will correspond to a new HTTP chunk sent on the wire.<p>
   * If chunked encoding is used the HTTP header {@code Transfer-Encoding} with a value of {@code Chunked} will be
   * automatically inserted in the response.<p>
   * If {@code chunked} is {@code false}, this response will not use HTTP chunked encoding, and therefore if any data is written the
   * body of the response, the total size of that data must be set in the {@code Content-Length} header <b>before</b> any
   * data is written to the response body.<p>
   * An HTTP chunked response is typically used when you do not know the total size of the request body up front.
   *
   * @return A reference to this, so multiple method calls can be chained.
   */
  override def setChunked(chunked: Boolean): HttpServerResponse = wrap(internal.setChunked(chunked))

  /**
   * Set the status code
   * @return A reference to this, so multiple method calls can be chained.
   */
  override def setStatusCode(statusCode: Int): HttpServerResponse = wrap(internal.setStatusCode(statusCode))

  /**
   * Set the status message
   * @return A reference to this, so multiple method calls can be chained.
   */
  override def setStatusMessage(statusMessage: String): HttpServerResponse = wrap(internal.setStatusMessage(statusMessage))

  /**
   * @return The HTTP trailers
   */
  override def trailers(): MultiMap = internal.trailers()

  /**
   * Write a {@link String} to the response body, encoded in UTF-8.
   *
   * @return A reference to this, so multiple method calls can be chained.
   */
  override def write(chunk: String): HttpServerResponse = wrap(internal.write(chunk))

  /**
   * Write a {@link String} to the response body, encoded using the encoding {@code enc}.
   *
   * @return A reference to this, so multiple method calls can be chained.
   */
  override def write(chunk: String, enc: String): HttpServerResponse = wrap(internal.write(chunk, enc))

}
