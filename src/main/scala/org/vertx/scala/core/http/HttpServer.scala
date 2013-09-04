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

import org.vertx.java.core.http.{ HttpServer => JHttpServer }
import org.vertx.java.core.http.{ HttpServerRequest => JHttpServerRequest, ServerWebSocket => JServerWebSocket }
import org.vertx.scala.core.{ WrappedServerSSLSupport, WrappedTCPSupport }
import org.vertx.scala.core.WrappedServerTCPSupport
import org.vertx.java.core.AsyncResult
import org.vertx.java.core.Handler
import org.vertx.scala.core.WrappedCloseable

/**
 * @author swilliams
 * @author <a href="http://www.campudus.com/">Joern Bernhardt</a>
 */
object HttpServer {
  def apply(internal: JHttpServer) = new HttpServer(internal)
}

class HttpServer(protected[this] val internal: JHttpServer) extends JHttpServer with WrappedCloseable with WrappedServerTCPSupport with WrappedServerSSLSupport {
  override type InternalType = JHttpServer

  override def listen(port: Int, host: String, listenHandler: Handler[AsyncResult[JHttpServer]]): HttpServer = wrap(internal.listen(port, host, listenHandler))
  override def listen(port: Int, host: String): HttpServer = wrap(internal.listen(port, host))
  override def listen(port: Int, listenHandler: Handler[AsyncResult[JHttpServer]]): HttpServer = wrap(internal.listen(port, listenHandler))
  override def listen(port: Int): HttpServer = wrap(internal.listen(port))
  override def requestHandler(): Handler[JHttpServerRequest] = internal.requestHandler()
  override def requestHandler(requestHandler: Handler[JHttpServerRequest]): HttpServer = wrap(internal.requestHandler(requestHandler))
  override def websocketHandler(): Handler[JServerWebSocket] = internal.websocketHandler()
  override def websocketHandler(wsHandler: Handler[JServerWebSocket]): HttpServer = wrap(internal.websocketHandler(wsHandler))

}