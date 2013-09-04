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

import org.vertx.scala.core.FunctionConverters._
import org.vertx.java.core.net.{ NetServer => JNetServer, NetSocket => JNetSocket }
import org.vertx.java.core.{ Handler, AsyncResult, ServerTCPSupport, ServerSSLSupport }
import org.vertx.java.core.impl.DefaultFutureResult
import org.vertx.scala.core.WrappedServerSSLSupport
import org.vertx.scala.core.WrappedServerTCPSupport
import org.vertx.scala.core.WrappedCloseable

/**
 * @author swilliams
 *
 */
object NetServer {

  def apply(actual: JNetServer) = new NetServer(actual)

}

class NetServer(protected[this] val internal: JNetServer) extends JNetServer with WrappedCloseable with WrappedServerSSLSupport with WrappedServerTCPSupport {
  override type InternalType = JNetServer

  override def connectHandler(connectHandler: Handler[JNetSocket]): NetServer = wrap(internal.connectHandler(connectHandler))
  override def host(): String = internal.host()
  override def listen(port: Int, host: String, listenHandler: Handler[AsyncResult[JNetServer]]): NetServer = wrap(internal.listen(port, host, listenHandler))
  override def listen(port: Int, host: String): NetServer = wrap(internal.listen(port, host))
  override def listen(port: Int, listenHandler: Handler[AsyncResult[JNetServer]]): org.vertx.java.core.net.NetServer = wrap(internal.listen(port, listenHandler))
  override def listen(port: Int): NetServer = wrap(internal.listen(port))
  override def port(): Int = internal.port()

}