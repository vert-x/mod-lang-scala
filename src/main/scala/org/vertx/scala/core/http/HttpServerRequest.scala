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
import org.vertx.java.core.http.{ HttpServerRequest => JHttpServerRequest }
import org.vertx.scala.core.FunctionConverters._
import org.vertx.scala.core.streams.WrappedReadStream
import org.vertx.java.core.MultiMap
import org.vertx.scala.core.net.NetSocket
import org.vertx.java.core.Handler
import org.vertx.java.core.http.HttpServerFileUpload
import org.vertx.java.core.http.HttpVersion

/**
 * Represents a server-side HTTP request.<p>
 * Instances are created for each request that is handled by the server
 * and is passed to the user via the {@link org.vertx.java.core.Handler} instance
 * registered with the {@link HttpServer} using the method {@link HttpServer#requestHandler(org.vertx.java.core.Handler)}.<p>
 * Each instance of this class is associated with a corresponding {@link HttpServerResponse} instance via
 * the {@code response} field.<p>
 * It implements {@link org.vertx.java.core.streams.ReadStream} so it can be used with
 * {@link org.vertx.java.core.streams.Pump} to pump data with flow control.<p>
 * Instances of this class are not thread-safe<p>
 *
 * @author <a href="http://tfox.org">Tim Fox</a>
 * @author swilliams
 * @author nfmelendez
 * @author Ranie Jade Ramiso
 * @author <a href="http://www.campudus.com/">Joern Bernhardt</a>
 */
class HttpServerRequest(val internal: JHttpServerRequest) extends JHttpServerRequest with WrappedReadStream {
  override type InternalType = JHttpServerRequest

  override def absoluteURI(): java.net.URI = internal.absoluteURI()

  override def bodyHandler(handler: Handler[Buffer]): HttpServerRequest = wrap(internal.bodyHandler(handler))

  def bodyHandler(handler: (Buffer) => Unit): HttpServerRequest = wrap(internal.bodyHandler(handler))

  override def expectMultiPart(expect: Boolean): HttpServerRequest = wrap(internal.expectMultiPart(expect))

  override def formAttributes(): MultiMap = internal.formAttributes()

  override def headers(): MultiMap = internal.headers

  override def method(): String = internal.method

  override def netSocket(): NetSocket = NetSocket(internal.netSocket())

  override def params(): MultiMap = internal.params

  override def path(): String = internal.path

  override def peerCertificateChain(): Array[javax.security.cert.X509Certificate] = internal.peerCertificateChain()

  override def query(): String = internal.query

  override def remoteAddress(): java.net.InetSocketAddress = internal.remoteAddress()

  override def response(): HttpServerResponse = HttpServerResponse(internal.response)

  override def uploadHandler(handler: Handler[HttpServerFileUpload]): HttpServerRequest = wrap(internal.uploadHandler(handler))

  override def uri(): String = internal.uri

  override def version(): HttpVersion = internal.version()
}

object HttpServerRequest {
  def apply(internal: JHttpServerRequest) =
    new HttpServerRequest(internal)
}
