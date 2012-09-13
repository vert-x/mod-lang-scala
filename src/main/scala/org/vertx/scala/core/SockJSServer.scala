package org.vertx.scala.core

import org.vertx.java.core.json.JsonObject
import org.vertx.scala.deploy.FunctionHandler1
import org.vertx.java.core.sockjs.SockJSSocket
import org.vertx.java.core.json.JsonArray

class SockJSServer(internal: org.vertx.java.core.sockjs.SockJSServer) {

  def bridge(sjsConfig: JsonObject, inboundPermitted: JsonArray, outboundPermitted: JsonArray): Unit = {
    internal.bridge(sjsConfig, inboundPermitted, outboundPermitted)
  }

  def bridge(sjsConfig: JsonObject, inboundPermitted: JsonArray, outboundPermitted: JsonArray, authTimeout: Int): Unit = {
    internal.bridge(sjsConfig, inboundPermitted, outboundPermitted, authTimeout)
  }

  def bridge(sjsConfig: JsonObject, inboundPermitted: JsonArray, outboundPermitted: JsonArray, authTimeout: Int, authAddress: String): Unit = {
    internal.bridge(sjsConfig, inboundPermitted, outboundPermitted, authTimeout, authAddress)
  }

  def meg(config: JsonObject, handler: (SockJSSocket) => Unit): Unit = {
    internal.installApp(config, new FunctionHandler1(handler))
  }

}