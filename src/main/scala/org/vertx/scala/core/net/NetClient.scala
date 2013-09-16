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

package org.vertx.scala.core.net

import org.vertx.java.core.net.{ NetClient => JNetClient }
import org.vertx.java.core.net.{ NetSocket => JNetSocket }
import org.vertx.java.core.{ TCPSupport, ClientSSLSupport, Handler, AsyncResult }
import org.vertx.java.core.impl.DefaultFutureResult
import org.vertx.scala.core.WrappedTCPSupport
import org.vertx.scala.core.WrappedClientSSLSupport

/**
 *
 * @author swilliams
 * @author Edgar Chan
 * @author <a href="http://www.campudus.com/">Joern Bernhardt</a>
 */
object NetClient {
  def apply(actual: JNetClient) = new NetClient(actual)
}

class NetClient(protected[this] val internal: JNetClient) extends JNetClient with WrappedTCPSupport with WrappedClientSSLSupport {
  override type InternalType = JNetClient

  override def close(): Unit = internal.close()
  override def connect(port: Int, host: String, connectHandler: org.vertx.java.core.Handler[org.vertx.java.core.AsyncResult[org.vertx.java.core.net.NetSocket]]): this.type = wrap(internal.connect(port, host, connectHandler))
  override def connect(port: Int, connectCallback: org.vertx.java.core.Handler[org.vertx.java.core.AsyncResult[org.vertx.java.core.net.NetSocket]]): this.type = wrap(internal.connect(port, connectCallback))
  override def getConnectTimeout(): Int = internal.getConnectTimeout()
  override def getReconnectAttempts(): Int = internal.getReconnectAttempts()
  override def getReconnectInterval(): Long = internal.getReconnectInterval()
  override def setConnectTimeout(timeout: Int): this.type = wrap(internal.setConnectTimeout(timeout))
  override def setReconnectAttempts(attempts: Int): this.type = wrap(internal.setReconnectAttempts(attempts))
  override def setReconnectInterval(interval: Long): this.type = wrap(internal.setReconnectInterval(interval))

}
