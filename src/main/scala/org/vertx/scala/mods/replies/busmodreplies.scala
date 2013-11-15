package org.vertx.scala.mods.replies

import org.vertx.scala.core.json._
import scala.concurrent.Future

sealed trait BusModReply

case class AsyncReply(replyWhenDone: Future[BusModReply]) extends BusModReply

sealed trait SyncReply extends BusModReply {
  def toJson: JsonObject
}
case class Ok(x: JsonObject = Json.obj()) extends SyncReply {
  def toJson = x.putString("status", "ok")
}

case class Error(message: String, id: String = "MODULE_EXCEPTION", obj: JsonObject = Json.obj()) extends SyncReply {
  def toJson = Json.obj("status" -> "error", "message" -> message, "error" -> id).mergeIn(obj)
}
