/*
 * Copyright 2014 the original author or authors.
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

import org.vertx.java.core.sockjs.{EventBusBridgeHook => JEventBusBridgeHook}
import org.vertx.scala.core.json.JsonObject
import org.vertx.scala.core._
import org.vertx.scala.core.FunctionConverters._

/**
 * A hook that you can use to receive various events on the EventBusBridge.
 *
 * @author Galder Zamarreño
 */
final class EventBusBridgeHook private[scala] (val asJava: JEventBusBridgeHook) extends AnyVal {

  /**
   * Called when a new socket is created
   * You can override this method to do things like check the origin header of a socket before
   * accepting it
   * @param sock The socket
   * @return true to accept the socket, false to reject it
   */
  def handleSocketCreated(sock: SockJSSocket): Boolean =
    asJava.handleSocketCreated(sock.asJava)

  /**
   * The socket has been closed
   * @param sock The socket
   */
  def handleSocketClosed(sock: SockJSSocket): Unit =
    asJava.handleSocketClosed(sock.asJava)

  /**
   * Client is sending or publishing on the socket
   * @param sock The sock
   * @param send if true it's a send else it's a publish
   * @param msg The message
   * @param address The address the message is being sent/published to
   * @return true To allow the send/publish to occur, false otherwise
   */
  def handleSendOrPub(sock: SockJSSocket, send: Boolean, msg: JsonObject, address: String): Boolean =
    asJava.handleSendOrPub(sock.asJava, send, msg, address)

  /**
   * Called before client registers a handler
   * @param sock The socket
   * @param address The address
   * @return true to let the registration occur, false otherwise
   */
  def handlePreRegister(sock: SockJSSocket, address: String): Boolean =
    asJava.handlePreRegister(sock.asJava, address)

  /**
   * Called after client registers a handler
   * @param sock The socket
   * @param address The address
   */
  def handlePostRegister(sock: SockJSSocket, address: String): Unit =
    asJava.handlePostRegister(sock.asJava, address)

  /**
   * Client is unregistering a handler
   * @param sock The socket
   * @param address The address
   */
  def handleUnregister(sock: SockJSSocket, address: String): Boolean =
    asJava.handleUnregister(sock.asJava, address)

  /**
   * Called before authorisation - you can override authorisation here if you don't want the default
   * @param message The auth message
   * @param sessionID The session ID
   * @param handler Handler - call this when authorisation is complete
   * @return true if you wish to override authorisation
   */
  def handleAuthorise(message: JsonObject, sessionID: String, handler: AsyncResult[Boolean] => Unit): Boolean =
    asJava.handleAuthorise(message, sessionID, asyncResultConverter((x: java.lang.Boolean) => x.booleanValue)(handler))

}

object EventBusBridgeHook {
  def apply(internal: JEventBusBridgeHook) = new EventBusBridgeHook(internal)
}