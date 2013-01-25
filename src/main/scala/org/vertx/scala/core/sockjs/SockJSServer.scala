/*
 * Copyright 2011-2012 the original author or authors.
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
import org.vertx.java.core.sockjs.{SockJSServer => JSockJSServer}
import org.vertx.java.core.sockjs.{SockJSServer => JSockJSServer}
import org.vertx.scala.core.http.SockJSSocket
iimport org.vertx.scala.core.sockjs.SockJSSocketHandler1
import org.vertx.scala.core.sockjs.SockJSSocket
mport org.vertx.scala.core.http.SockJSSocketHandler1

class SockJSServer(internal: JSockJSServer) {

  def bridge(sjsConfig: JsonObject, inboundPermitted: JsonArray, outboundPermitted: JsonArray): Unit = {
    internal.bridge(sjsConfig, inboundPermitted, outboundPermitted)
  }

  def bridge(sjsConfig: JsonObject, inboundPermitted: JsonArray, outboundPermitted: JsonArray, authTimeout: Int): Unit = {
    internal.bridge(sjsConfig, inboundPermitted, outboundPermitted, authTimeout)
  }

  def bridge(sjsConfig: JsonObject, inboundPermitted: JsonArray, outboundPermitted: JsonArray, authTimeout: Int, authAddress: String): Unit = {
    internal.bridge(sjsConfig, inboundPermitted, outboundPermitted, authTimeout, authAddress)
  }

  def installApp(config: JsonObject, handler: (SockJSSocket) => Unit): Unit = {
    internal.installApp(config, new SockJSSocketHandler1(handler))
  }

}