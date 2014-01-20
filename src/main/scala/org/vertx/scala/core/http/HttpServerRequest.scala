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

import org.vertx.java.core.http.{ HttpServerRequest => JHttpServerRequest }
import org.vertx.scala.core.MultiMap
import org.vertx.scala.core.net.NetSocket
import org.vertx.scala.core.buffer._
import org.vertx.scala.core.Handler
import org.vertx.scala.Self
import org.vertx.scala.core.streams.ReadStream

/**
 * Represents a server-side HTTP request.<p>
 * Instances are created for each request that is handled by the server
 * and is passed to the user via the [[org.vertx.java.core.Handler]] instance
 * registered with the [[org.vertx.scala.core.http.HttpServer]] using the method [[org.vertx.scala.core.http.HttpServer.requestHandler(org.vertx.java.core.Handler)]].<p>
 * Each instance of this class is associated with a corresponding [[org.vertx.scala.core.http.HttpServerResponse]] instance via
 * the `response` field.<p>
 * It implements [[org.vertx.scala.core.streams.ReadStream]] so it can be used with
 * [[org.vertx.scala.core.streams.Pump]] to pump data with flow control.<p>
 * Instances of this class are not thread-safe<p>
 *
 * @author <a href="http://tfox.org">Tim Fox</a>
 * @author swilliams
 * @author nfmelendez
 * @author Ranie Jade Ramiso
 * @author <a href="http://www.campudus.com/">Joern Bernhardt</a>
 * @author Galder Zamarreño
 */
final class HttpServerRequest private[scala] (val asJava: JHttpServerRequest) extends Self
  with ReadStream {

  override type J = JHttpServerRequest

  /**
   * The HTTP version of the request
   */
  def version(): HttpVersion = asJava.version()

  /**
   * The HTTP method for the request. One of GET, PUT, POST, DELETE, TRACE, CONNECT, OPTIONS or HEAD
   */
  def method(): String = asJava.method

  /**
   * The uri of the request. For example
   * http://www.somedomain.com/somepath/somemorepath/someresource.foo?someparam=32&someotherparam=x
   */
  def uri(): String = asJava.uri

  /**
   * The path part of the uri. For example /somepath/somemorepath/someresource.foo
   */
  def path(): String = asJava.path

  /**
   * The query part of the uri. For example someparam=32&someotherparam=x
   */
  def query(): String = asJava.query

  /**
   * The response. Each instance of this class has an [[org.vertx.scala.core.http.HttpServerResponse]] instance attached to it. This is used
   * to send the response back to the client.
   */
  def response(): HttpServerResponse = HttpServerResponse(asJava.response)

  /**
   * A map of all headers in the request, If the request contains multiple headers with the same key, the values
   * will be concatenated together into a single header with the same key value, with each value separated by a comma,
   * as specified <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec4.html#sec4.2">here</a>.
   * The headers will be automatically lower-cased when they reach the server
   */
  def headers(): MultiMap = asJava.headers

  /**
   * Returns a map of all the parameters in the request.
   *
   * @return A map of all the parameters in the request.
   */
  def params(): MultiMap = asJava.params

  /**
   * Return the remote (client side) address of the request.
   */
  def remoteAddress(): java.net.InetSocketAddress = asJava.remoteAddress()

  /**
   * Return the local (server side) address of the server that handles the request
   */
  def localAddress(): java.net.InetSocketAddress = asJava.localAddress()

  /**
   * @return an array of the peer certificates.  Returns null if connection is
   *         not SSL.
   * @throws SSLPeerUnverifiedException SSL peer's identity has not been verified.
   */
  def peerCertificateChain(): Array[javax.security.cert.X509Certificate] = asJava.peerCertificateChain()

  /**
   * Get the absolute URI corresponding to the the HTTP request
   * @return the URI
   */
  def absoluteURI(): java.net.URI = asJava.absoluteURI()

  /**
   * Convenience method for receiving the entire request body in one piece. This saves the user having to manually
   * set a data and end handler and append the chunks of the body until the whole body received.
   * Don't use this if your request body is large - you could potentially run out of RAM.
   *
   * @param handler This handler will be called after all the body has been received
   */
  def bodyHandler(handler: Buffer => Unit): HttpServerRequest = wrap(asJava.bodyHandler(bufferHandlerToJava(handler)))

  /**
   * Get a net socket for the underlying connection of this request. USE THIS WITH CAUTION!
   * Writing to the socket directly if you don't know what you're doing can easily break the HTTP protocol
   * @return the net socket
   */
  def netSocket(): NetSocket = NetSocket(asJava.netSocket())

  /**
   * Call this with true if you are expecting a multi-part form to be submitted in the request
   * This must be called before the body of the request has been received.
   * @param expect `true` if expecting multi-part form, `false` otherwise
   */
  def expectMultiPart(expect: Boolean): HttpServerRequest = wrap(asJava.expectMultiPart(expect))

  /**
   * Set the upload handler. The handler will get notified once a new file upload was received and so allow to
   * get notified by the upload in progress.
   */
  def uploadHandler(handler: Handler[HttpServerFileUpload]): HttpServerRequest = wrap(asJava.uploadHandler(handler))

  /**
   * Returns a map of all form attributes which was found in the request. Be aware that this message should only get
   * called after the endHandler was notified as the map will be filled on-the-fly.
   * [[org.vertx.scala.core.http.HttpServerRequest.expectMultiPart(boolean)]] must be called first before trying to get the formAttributes
   */
  def formAttributes(): MultiMap = asJava.formAttributes()
}

object HttpServerRequest {
  def apply(internal: JHttpServerRequest) = new HttpServerRequest(internal)
  def unapply(request: HttpServerRequest): JHttpServerRequest = request.asJava
}
