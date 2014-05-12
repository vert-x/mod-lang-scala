package org.vertx.scala.router

import java.io.FileNotFoundException
import java.net.URLEncoder

import org.vertx.scala.FutureOps
import org.vertx.scala.core._
import org.vertx.scala.core.file.FileProps
import org.vertx.scala.core.http.{HttpServerRequest, HttpServerResponse}
import FutureOps._
import org.vertx.scala.router.routing._

import scala.concurrent.Future
import scala.util.{Failure, Success}

/**
 * The Router trait can be extended to give access to an easy way to write nice routes for your
 * HTTP server.
 *
 * @author <a href="http://www.campudus.com/">Joern Bernhardt</a>
 */
trait Router extends (HttpServerRequest => Unit) {
  this: VertxAccess =>

  type Routing = PartialFunction[RouteMatch, Reply]

  /**
   * Override this method to define your routes for the request handler.
   *
   * @param req The HttpServerRequest that came in.
   * @return A partial function that matches all routes.
   */
  def routes(implicit req: HttpServerRequest): Routing

  /**
   * Override this method for your custom authentication check.
   *
   * @param req The HttpServerRequest to check.
   * @return A future telling whether the request is authorized (true) or not (false / Exception).
   */
  protected def checkAuthentication(req: HttpServerRequest): Future[Boolean] = {
    Future.successful(false)
  }

  /** The working directory */
  protected def workingDirectory: String = "./"

  /** File to send if the given file in SendFile was not found. */
  protected def notFoundFile: String = "404.html"

  /**
   * Use this to check for authentication in your routes.
   *
   * @param replyIfAuthed The reply to send if the request is successfully authed.
   * @param req The HttpServerRequest that was sent.
   * @return The given replyIfAuthed or an Error reply that the request wasn't authed.
   */
  final protected def authed(replyIfAuthed: => Reply)(implicit req: HttpServerRequest): Reply = {
    def unauthorized(ex: Option[Throwable]): Reply = ex match {
      case Some(cause: RouterException) => Error(cause)
      case Some(cause) => Error(RouterException(message = "Unauthorized", cause = cause, id = "UNAUTHORIZED", statusCode = 401))
      case None => Error(RouterException(message = "Unauthorized", id = "UNAUTHORIZED", statusCode = 401))
    }

    req.pause()
    AsyncReply(checkAuthentication(req) map { authenticated =>
      req.resume()
      if (authenticated) replyIfAuthed
      else unauthorized(None)
    } recover {
      case x =>
        req.resume()
        unauthorized(Some(x))
    })
  }

  private val noRouteMatch: RouteMatch => Reply =
    _ => Error(RouterException(message = "No route matched.", id = "NO_ROUTE", statusCode = 404))

  private def matcherFor(routeMatch: RouteMatch, req: HttpServerRequest): Reply = {
    val pf: PartialFunction[RouteMatch, Reply] = routes(req)
    val tryAllThenNoRouteMatch: Function[RouteMatch, Reply] = _ => pf.applyOrElse(All(req.path()), noRouteMatch)
    pf.applyOrElse(routeMatch, tryAllThenNoRouteMatch)
  }

  private def fileExists(file: String): Future[String] = asyncResultToFuture {
    tryFn: ResultHandler[Boolean] => vertx.fileSystem.exists(file, tryFn)
  } map {
    case true => file
    case false => throw new FileNotFoundException(file)
  }

  private def addIndexToDirName(path: String): String =
    if (path.endsWith("/")) path + "index.html"
    else path + "/index.html"

  private def directoryToIndexFile(path: String): Future[String] = asyncResultToFuture {
    tryFn: ResultHandler[FileProps] => vertx.fileSystem.lprops(path, tryFn)
  } flatMap { fp =>
    if (fp.isDirectory) fileExists(addIndexToDirName(path))
    else Future.successful(path)
  }

  private def urlEncode(str: String) = URLEncoder.encode(str, "UTF-8")

  private def endResponse(resp: HttpServerResponse, reply: SyncReply): Unit = {
    reply match {
      case NoBody =>
        resp.end()
      case Ok(js) =>
        resp.setStatusCode(200)
        resp.setStatusMessage("OK")
        resp.putHeader("Content-type", "application/json")
        resp.end(js.encode())
      case SendFile(path, absolute) =>
        (for {
          exists <- fileExists(if (absolute) path else s"$workingDirectory/$path")
          file <- directoryToIndexFile(exists)
        } yield {
          logger.info(s"Serving file $file after receiving request for: $path")
          resp.sendFile(file, notFoundFile)
        }) recover {
          case ex: FileNotFoundException =>
            endResponse(resp, Error(RouterException("File not found", ex, "errors.routing.fileNotFound", 404)))
          case ex =>
            endResponse(resp, Error(RouterException("send file exception", ex, "errors.routing.sendFile", 500)))
        }
      case Error(RouterException(_, cause, id, 404)) =>
        logger.info(s"File not found", cause)
        resp.setStatusCode(404)
        resp.setStatusMessage("NOT FOUND")
        resp.sendFile(notFoundFile)
      case Error(RouterException(message, cause, id, statusCode)) =>
        logger.warn(s"Error $statusCode: $message", cause)
        resp.setStatusCode(statusCode)
        resp.setStatusMessage(id)
        message match {
          case null => resp.end()
          case msg => resp.end(msg)
        }
    }
  }

  private def sendReply(req: HttpServerRequest, reply: Reply): Unit = {
    logger.debug(s"Sending back reply as response: $reply")

    reply match {
      case AsyncReply(future) =>
        future.onComplete {
          case Success(r) => sendReply(req, r)
          case Failure(x: RouterException) => endResponse(req.response(), errorReplyFromException(x))
          case Failure(x: Throwable) => endResponse(req.response(), Error(routerException(x)))
        }
      case SetCookie(key, value, nextReply) =>
        req.response().headers().addBinding("Set-Cookie", s"${urlEncode(key)}=${urlEncode(value)}")
        sendReply(req, nextReply)
      case Header(key, value, nextReply) =>
        req.response().putHeader(key, value)
        sendReply(req, nextReply)
      case x: SyncReply => endResponse(req.response(), x)
    }
  }

  private def routerException(ex: Throwable): RouterException = ex match {
    case x: RouterException => x
    case x => RouterException(message = x.getMessage, cause = x)
  }

  private def errorReplyFromException(ex: RouterException) = Error(ex)

  /**
   * To be able to use this in `HttpServer.requestHandler()`, the Router needs to be a `HttpServerRequest => Unit`. This
   * apply method starts the magic to be able to use `override def request() = ...` for the routes.
   *
   * @param req The HttpServerRequest that comes in.
   */
  override final def apply(req: HttpServerRequest): Unit = {
    logger.info(s"${req.method()}-Request: ${req.uri()}")

    val reply = try {
      val path = req.path()
      val routeMatch: RouteMatch = req.method() match {
        case "GET" => Get(path)
        case "PUT" => Put(path)
        case "POST" => Post(path)
        case "DELETE" => Delete(path)
        case "OPTIONS" => Options(path)
        case "HEAD" => Head(path)
        case "TRACE" => Trace(path)
        case "PATCH" => Patch(path)
        case "CONNECT" => Connect(path)
      }
      matcherFor(routeMatch, req)
    } catch {
      case ex: RouterException =>
        errorReplyFromException(ex)
      case ex: Throwable =>
        logger.warn(s"Uncaught Exception for request ${req.absoluteURI()}", ex)
        errorReplyFromException(routerException(ex))
    }

    sendReply(req, reply)
  }

}