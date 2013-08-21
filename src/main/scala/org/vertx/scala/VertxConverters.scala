/*
 * Copyright 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.vertx.scala

import org.vertx.java.core.file.{AsyncFile => JAsyncFile}
import org.vertx.scala.core.file.AsyncFile
import org.vertx.java.core.eventbus.{EventBus => JEventBus}
import org.vertx.scala.core.eventbus.EventBus
import org.vertx.java.core.http.{ServerWebSocket => JServerWebSocket, WebSocket => JWebSocket}
import org.vertx.scala.core.http.{WebSocket, ServerWebSocket}


trait VertxScalaConverters{

  implicit def toScalaAsycFile(jcomponent:JAsyncFile):AsyncFile = AsyncFile(jcomponent)

  implicit def toScalaEventBus(jbus: JEventBus): EventBus = EventBus(jbus)

  implicit def convertJavaToScalaWebSocket(jsocket: JServerWebSocket):ServerWebSocket = {
    ServerWebSocket(jsocket)
  }

  implicit def convertJavaToScalaWebSocket(jsocket: JWebSocket):WebSocket = {
    WebSocket(jsocket)
  }

}

object VertxConverters extends VertxScalaConverters
