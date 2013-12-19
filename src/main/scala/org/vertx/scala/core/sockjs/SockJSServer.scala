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
import org.vertx.java.core.sockjs.{ SockJSServer => JSockJSServer }
import org.vertx.scala.Self
import org.vertx.scala.core.FunctionConverters._
import org.vertx.java.core.sockjs.EventBusBridgeHook

/**
 * This is an implementation of the server side part of <a href="https://github.com/sockjs">SockJS</a>.<p>
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
 * On the server side, you interact using instances of [[org.vertx.scala.core.sockjs.SockJSSocket]] - this allows you to send data to the
 * client or receive data via the [[org.vertx.scala.core.sockjs.SockJSSocket.dataHandler()]].<p>
 *
 * You can register multiple applications with the same SockJSServer, each using different path prefixes, each
 * application will have its own handler, and configuration.<p>
 *
 * Instances of this class are not thread-safe.<p>
 *
 * @author <a href="http://tfox.org">Tim Fox</a>
 * @author swilliams
 * @author <a href="http://www.campudus.com/">Joern Bernhardt</a>
 * @author Galder Zamarreño
 */
final class SockJSServer private[scala] (val asJava: JSockJSServer) extends Self {

  /**
   * Install an application
   * @param config The application configuration
   * @param sockHandler A handler that will be called when new SockJS sockets are created
   */
  def installApp(config: JsonObject, sockHandler: SockJSSocket => Unit): SockJSServer =
    wrap(asJava.installApp(config, fnToHandler(sockHandler.compose(SockJSSocket.apply))))

  /**
   * Install an app which bridges the SockJS server to the event bus
   * @param sjsConfig The config for the app
   * @param inboundPermitted A list of JSON objects which define permitted matches for inbound (client->server) traffic
   * @param outboundPermitted A list of JSON objects which define permitted matches for outbound (server->client)
   * traffic
   */
  def bridge(sjsConfig: JsonObject, inboundPermitted: JsonArray, outboundPermitted: JsonArray): SockJSServer =
    wrap(asJava.bridge(sjsConfig, inboundPermitted, outboundPermitted))

  /**
   * Install an app which bridges the SockJS server to the event bus
   * @param sjsConfig The config for the app
   * @param inboundPermitted A list of JSON objects which define permitted matches for inbound (client->server) traffic
   * @param outboundPermitted A list of JSON objects which define permitted matches for outbound (server->client)
   * traffic
   * @param authTimeout Default time an authorisation will be cached for in the bridge (defaults to 5 minutes)
   */
  def bridge(sjsConfig: JsonObject, inboundPermitted: JsonArray, outboundPermitted: JsonArray, authTimeout: Long): SockJSServer =
    wrap(asJava.bridge(sjsConfig, inboundPermitted, outboundPermitted, authTimeout))

  /**
   * Install an app which bridges the SockJS server to the event bus
   * @param sjsConfig The config for the app
   * @param inboundPermitted A list of JSON objects which define permitted matches for inbound (client->server) traffic
   * @param outboundPermitted A list of JSON objects which define permitted matches for outbound (server->client)
   * traffic
   * @param authTimeout Default time an authorisation will be cached for in the bridge (defaults to 5 minutes)
   * @param authAddress Address of auth manager. Defaults to 'vertx.basicauthmanager.authorise'
   */
  def bridge(sjsConfig: JsonObject, inboundPermitted: JsonArray, outboundPermitted: JsonArray, authTimeout: Long, authAddress: String): SockJSServer =
    wrap(asJava.bridge(sjsConfig, inboundPermitted, outboundPermitted, authTimeout, authAddress))

  /**
   * Install an app which bridges the SockJS server to the event bus
   * @param sjsConfig The config for the app
   * @param inboundPermitted A list of JSON objects which define permitted matches for inbound (client->server) traffic
   * @param outboundPermitted A list of JSON objects which define permitted matches for outbound (server->client)
   * traffic
   * @param bridgeConfig JSON object holding config for the EventBusBridge
   */
  def bridge(sjsConfig: JsonObject, inboundPermitted: JsonArray, outboundPermitted: JsonArray, bridgeConfig: JsonObject): SockJSServer =
    wrap(asJava.bridge(sjsConfig, inboundPermitted, outboundPermitted, bridgeConfig))

  /**
   * Set a EventBusBridgeHook on the SockJS server
   * @param hook The hook
   */
  def setHook(hook: EventBusBridgeHook): SockJSServer = wrap(asJava.setHook(hook))
}

/** Factory for [[org.vertx.scala.core.sockjs.SockJSServer]] instances. */
object SockJSServer {
  def apply(internal: JSockJSServer) = new SockJSServer(internal)
}
