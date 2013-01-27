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

package org.vertx.scala.http

import scala.language.implicitConversions
import org.vertx.java.core.http.{ServerWebSocket => JServerWebSocket}
import org.vertx.java.core.Handler

object ServerWebSocketHandler1 {
  def apply(socket: (ServerWebSocket) => Unit) =
    new ServerWebSocketHandler1(socket)
}

class ServerWebSocketHandler1(delegate: (ServerWebSocket) => Unit) extends Handler[JServerWebSocket] {

  implicit def convertJavaToScalaWebSocket(jsocket: JServerWebSocket):ServerWebSocket = {
    ServerWebSocket(jsocket)
  }

  def handle(jsocket: JServerWebSocket) {
    delegate(jsocket)
  }

}