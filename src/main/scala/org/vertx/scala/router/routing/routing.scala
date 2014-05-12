package org.vertx.scala.router.routing

import org.vertx.scala.core.json._
import org.vertx.scala.router.RouterException

import scala.concurrent.Future

sealed trait Reply

sealed trait SyncReply extends Reply

case object NoBody extends SyncReply

case class Ok(json: JsonObject) extends SyncReply

case class SendFile(file: String, absolute: Boolean = false) extends SyncReply

case class Error(ex: RouterException) extends SyncReply

case class AsyncReply(replyWhenDone: Future[Reply]) extends Reply

case class Header(key: String, value: String, endReply: Reply) extends Reply

case class SetCookie(key: String, value: String, endReply: Reply) extends Reply

sealed trait RouteMatch

case class All(path: String) extends RouteMatch

case class Connect(path: String) extends RouteMatch

case class Delete(path: String) extends RouteMatch

case class Get(path: String) extends RouteMatch

case class Head(path: String) extends RouteMatch

case class Options(path: String) extends RouteMatch

case class Patch(path: String) extends RouteMatch

case class Post(path: String) extends RouteMatch

case class Put(path: String) extends RouteMatch

case class Trace(path: String) extends RouteMatch
