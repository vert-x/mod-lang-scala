package org.vertx.scala.tests.router

import org.vertx.scala.router.{RouterException, Router}
import org.vertx.scala.core.http.{HttpServer, HttpServerRequest}
import org.vertx.scala.core.FunctionConverters._
import org.vertx.scala.router.routing._
import org.vertx.scala.platform.Verticle
import scala.concurrent.Promise
import scala.util.{Try, Failure, Success}
import org.vertx.scala.core.json.Json
import scala.util.matching.Regex

/**
 * @author <a href="http://www.campudus.com/">Joern Bernhardt</a>
 */
class JsonRouter extends Verticle with Router {

  override def start(p: Promise[Unit]) = {
    vertx.createHttpServer().requestHandler(this).listen(8080, {
      case Success(s) => p.success()
      case Failure(x) => p.failure(x)
    }: Try[HttpServer] => Unit)
  }

  override def notFoundFile = "error404.html"

  val IdMatcher = "/get/(\\d+)".r

  override def routes(implicit req: HttpServerRequest): Routing = {
    case Get("/") => SendFile("helloscala.txt")
    case Get("/test.txt") => SendFile("helloscala.txt")
    case Get("/not-found") => SendFile("not-existing.txt")
    case Get("/cookies1") => SetCookie("a", "1", Ok(Json.obj()))
    case Get("/cookies2") => SetCookie("a", "1", SetCookie("b", "2", Ok(Json.obj())))
    case Get(IdMatcher(id)) => Ok(Json.obj("result" -> id))
    case Post("/") => Ok(Json.obj("status" -> "ok"))
    case Post("/post-ok") => Ok(Json.obj("status" -> "ok"))
    case Head("/head") => Header("x-custom-head", "hello", NoBody)
    case Delete("/forbidden") => Error(RouterException(message = "not authorized", statusCode = 403))
    case All("/all-test") => SendFile("helloscala.txt")
  }
}
