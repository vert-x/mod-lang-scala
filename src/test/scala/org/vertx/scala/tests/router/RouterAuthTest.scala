package org.vertx.scala.tests.router

import org.vertx.scala.FutureOps

import scala.concurrent.{ExecutionContext, Promise, Future}
import FutureOps._
import org.vertx.scala.core.FunctionConverters._
import scala.util.Try
import org.junit.Test
import org.vertx.testtools.VertxAssert._
import scala.util.Failure
import scala.util.Success
import org.vertx.scala.core.http.HttpClientResponse

/**
 * @author <a href="http://www.campudus.com/">Joern Bernhardt</a>
 */
class RouterAuthTest extends RouterTestHelper {
  override def asyncBefore(): Future[Unit] = futurify { p: Promise[Unit] =>
    container.deployVerticle("scala:org.vertx.scala.tests.router.AuthRouter", handler = {
      case Success(deployId) => p.success()
      case Failure(ex) => p.failure(ex)
    }: Try[String] => Unit)
  }

  @Test def unauthorizedGet(): Unit = doHttp("GET", "/member") map { res =>
    assertEquals("Should get a 401-UNAUTHORIZED status code", 401, res.statusCode())
    testComplete()
  }

  @Test def authorizedGet(): Unit = doAuthedHttp("GET", "/member") map bodyOkCompleter({ body =>
    assertEquals("Should receive the real test file contents", testFileContents(), body)
  })

  @Test def wrongAuthTokenGet(): Unit = doHttpInternal("GET", "/member") { req =>
    req.putHeader("X-XSRF-TOKEN", "wrong")
  } map { res: HttpClientResponse =>
    assertEquals("Should get a 401-UNAUTHORIZED status code", 401, res.statusCode())
    testComplete()
  }

  @Test def shouldGetOwnException(): Unit = doHttpInternal("GET", "/teapot") { req =>
    req.putHeader("X-XSRF-TOKEN", "whatever")
  } map { res =>
    res.bodyHandler { buffer =>
      assertEquals("Should get the customized auth message", RouterTestHelper.customAuthExceptionMessage(), buffer.toString("UTF-8"))
      logger.info("got right body")
      testComplete()
    }
    assertEquals("Should get a 418 status code", 418, res.statusCode())
    assertEquals("Should get a TEAPOT status message", "TEAPOT", res.statusMessage())
    vertx.setTimer(2000, { id =>
      fail("Test didn't handle the body -> broken!")
    })
  }
}
