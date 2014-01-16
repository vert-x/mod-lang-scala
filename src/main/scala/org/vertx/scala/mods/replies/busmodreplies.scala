/*
 * Copyright 2013 the original author or authors.
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
package org.vertx.scala.mods.replies

import org.vertx.scala.core.json._
import scala.concurrent.Future
import org.vertx.scala.mods.ScalaBusMod._

sealed trait BusModReceiveEnd
sealed trait BusModReply extends BusModReceiveEnd

case class AsyncReply(replyWhenDone: Future[BusModReply]) extends BusModReply

sealed trait ReplyReceiver {
  val handler: Receive
}
case class Receiver(handler: Receive) extends ReplyReceiver
case class ReceiverWithTimeout(handler: Receive, timeout: Long, timeoutHandler: () => Unit) extends ReplyReceiver

sealed trait SyncReply extends BusModReply {
  val replyHandler: Option[ReplyReceiver]
  def toJson: JsonObject
}
case class Ok(x: JsonObject = Json.obj(), replyHandler: Option[ReplyReceiver] = None) extends SyncReply {
  def toJson = x.putString("status", "ok")
}

case class Error(message: String, id: String = "MODULE_EXCEPTION", obj: JsonObject = Json.obj()) extends SyncReply {
  val replyHandler = None
  def toJson = Json.obj("status" -> "error", "message" -> message, "error" -> id).mergeIn(obj)
}

case object NoReply extends BusModReceiveEnd
