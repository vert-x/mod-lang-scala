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

package org.vertx.scala.core.sockjs

import org.vertx.java.core.json.JsonArray
import org.vertx.java.core.json.JsonObject
import org.vertx.java.core.sockjs.{ SockJSSocket => JSockJSSocket }
import org.vertx.java.core.sockjs.{ SockJSServer => JSockJSServer }
import org.vertx.scala.Wrap
import org.vertx.scala.core.FunctionConverters._
import org.vertx.java.core.sockjs.EventBusBridgeHook
import org.vertx.java.core.Handler
import org.vertx.scala.VertxWrapper

/**
 * @author swilliams
 */
object SockJSServer {
  def apply(internal: JSockJSServer) = new SockJSServer(internal)
}

/**
 *
 * This is an implementation of the server side part of <a href="https://github.com/sockjs">SockJS</a><p>
 *
 * <p>SockJS enables browsers to communicate with the server using a simple WebSocket-like api for sending
 * and receiving messages. Under the bonnet SockJS chooses to use one of several protocols depending on browser
 * capabilities and what appears to be working across the network.<p>
 *
 * Available protocols include:<p>
 *
 * <ul>
 *   <li>WebSockets</li>
 *   <li>xhr-polling</li>
 *   <li>xhr-streaming</li>
 *   <li>json-polling</li>
 *   <li>event-source</li>
 *   <li>html-file</li>
 * </ul><p>
 *
 * This means, it should <i>just work</i> irrespective of what browser is being used, and whether there are nasty
 * things like proxies and load balancers between the client and the server.<p>
 *
 * For more detailed information on SockJS, see their website.<p>
 *
 * On the server side, you interact using instances of {@link SockJSSocket} - this allows you to send data to the
 * client or receive data via the {@link SockJSSocket#dataHandler}.<p>
 *
 * You can register multiple applications with the same SockJSServer, each using different path prefixes, each
 * application will have its own handler, and configuration.<p>
 *
 * Instances of this class are not thread-safe.<p>
 *
 * @author <a href="http://tfox.org">Tim Fox</a>
 * @author <a href="http://www.campudus.com/">Joern Bernhardt</a>
 */
class SockJSServer(protected[this] val internal: JSockJSServer) extends JSockJSServer with VertxWrapper {
  override type InternalType = JSockJSServer

  override def installApp(config: JsonObject, sockHandler: Handler[JSockJSSocket]): SockJSServer =
    wrap(internal.installApp(config, sockHandler))

  override def bridge(sjsConfig: JsonObject, inboundPermitted: JsonArray, outboundPermitted: JsonArray) =
    wrap(internal.bridge(sjsConfig, inboundPermitted, outboundPermitted))

  override def bridge(sjsConfig: JsonObject, inboundPermitted: JsonArray, outboundPermitted: JsonArray, authTimeout: Long) =
    wrap(internal.bridge(sjsConfig, inboundPermitted, outboundPermitted, authTimeout))

  override def bridge(sjsConfig: JsonObject, inboundPermitted: JsonArray, outboundPermitted: JsonArray, authTimeout: Long, authAddress: String) =
    wrap(internal.bridge(sjsConfig, inboundPermitted, outboundPermitted, authTimeout, authAddress))

  override def setHook(hook: EventBusBridgeHook): SockJSServer = wrap(internal.setHook(hook))
}
