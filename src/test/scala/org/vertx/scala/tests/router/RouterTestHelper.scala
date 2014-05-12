package org.vertx.scala.tests.router

import org.vertx.scala.FutureOps
import org.vertx.scala.core.http.{HttpClientRequest, HttpClientResponse}
import org.vertx.testtools.VertxAssert._
import scala.concurrent.{Promise, Future}
import FutureOps._
import org.vertx.scala.testtools.TestVerticle

/**
 * @author <a href="http://www.campudus.com/">Joern Bernhardt</a>
 */
trait RouterTestHelper extends TestVerticle {

  protected def error404FileContents(): String =
    vertx.fileSystem.readFileSync("error404.html").toString("UTF-8")

  protected def testFileContents(): String =
    vertx.fileSystem.readFileSync("helloscala.txt").toString("UTF-8")

  protected def bodyOkCompleter[B](fn: String => B): HttpClientResponse => Unit = { res: HttpClientResponse =>
    assertEquals(200, res.statusCode())
    checkBody(fn)(res)
  }

  protected def checkBody[B](fn: String => B): HttpClientResponse => Unit = { res: HttpClientResponse =>
    res.bodyHandler { body =>
      logger.info("in checkBody test")
      fn(body.toString("UTF-8"))
      testComplete()
    }
    logger.info("set checkBody test")
  }

  protected def doHttp(method: String, uri: String): Future[HttpClientResponse] = {
    doHttpInternal(method, uri) { _ =>}
  }

  protected def doAuthedHttp(method: String, uri: String): Future[HttpClientResponse] = {
    doHttpInternal(method, uri) { request =>
      request.putHeader("X-XSRF-TOKEN", "secret")
    }
  }

  protected def doHttpInternal(method: String, uri: String)(fn: HttpClientRequest => Unit):
  Future[HttpClientResponse] = futurify {
    p: Promise[HttpClientResponse] =>
      val request = vertx.createHttpClient()
        .setPort(8080)
        .request(method, uri, { res =>
        p.success(res)
      })
      fn(request)
      request.end()
  }

}

object RouterTestHelper {
  def customAuthExceptionMessage(): String = "i'm a teapot."
}