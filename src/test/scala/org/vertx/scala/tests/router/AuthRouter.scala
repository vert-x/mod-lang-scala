package org.vertx.scala.tests.router

import org.vertx.scala.platform.Verticle
import org.vertx.scala.core.FunctionConverters._
import scala.concurrent.{Future, Promise}
import scala.util.{Try, Failure, Success}
import org.vertx.scala.core.http.{HttpServerRequest, HttpServer}
import org.vertx.scala.router.{RouterException, Router}
import org.vertx.scala.router.routing.{SendFile, Get}

/**
 * @author <a href="http://www.campudus.com/">Joern Bernhardt</a>
 */
class AuthRouter extends Verticle with Router {
  override def start(p: Promise[Unit]) = {
    vertx.createHttpServer().requestHandler(this).listen(8080, {
      case Success(s) => p.success()
      case Failure(x) => p.failure(x)
    }: Try[HttpServer] => Unit)
  }

  override def checkAuthentication(req: HttpServerRequest): Future[Boolean] = {
    req.path() match {
      case "/teapot" =>
        Future.failed(
          RouterException(message = RouterTestHelper.customAuthExceptionMessage(), id = "TEAPOT", statusCode = 418))
      case _ =>
        val option = for {
          tokenSet <- req.headers().get("X-XSRF-TOKEN")
          token <- tokenSet.headOption
        } yield token == "secret"

        Future.successful(option.getOrElse(false))
    }
  }

  override def routes(implicit req: HttpServerRequest): Routing = {
    case Get("/member")
         | Get("/teapot") => authed {
      SendFile("helloscala.txt")
    }
  }

}