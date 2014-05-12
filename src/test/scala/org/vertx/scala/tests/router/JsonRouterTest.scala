package org.vertx.scala.tests.router

import org.junit.Test
import org.vertx.scala.FutureOps
import org.vertx.testtools.VertxAssert._
import FutureOps._
import org.vertx.scala.core.FunctionConverters._
import scala.concurrent.{ExecutionContext, Promise, Future}
import scala.util.{Try, Failure, Success}
import org.vertx.scala.core.json.Json
import org.vertx.scala.core.http.HttpClientResponse

/**
 * @author <a href="http://www.campudus.com/">Joern Bernhardt</a>
 */
class JsonRouterTest extends RouterTestHelper {

  override def asyncBefore(): Future[Unit] = futurify { p: Promise[Unit] =>
    container.deployVerticle("scala:org.vertx.scala.tests.router.JsonRouter", handler = {
      case Success(deployId) => p.success()
      case Failure(ex) => p.failure(ex)
    }: Try[String] => Unit)
  }

  @Test def indexGet(): Unit = doHttp("GET", "/") map bodyOkCompleter { body =>
    assertEquals(testFileContents(), body)
  }

  @Test def pathGet(): Unit = doHttp("GET", "/test.txt") map bodyOkCompleter { body =>
    assertEquals(testFileContents(), body)
  }

  @Test def missingFileSend(): Unit = doHttp("GET", "/not-found") map { res: HttpClientResponse =>
    assertEquals(404, res.statusCode())
    checkBody { body =>
      assertEquals(error404FileContents(), body)
    }(res)
  }

  @Test def singleCookie(): Unit = doHttp("GET", "/cookies1") map { res =>
    assertEquals(200, res.statusCode())
    assertEquals(Set("a=1"), res.headers().get("Set-Cookie").get)
    res.endHandler(testComplete())
  }

  @Test def multipleCookies(): Unit = doHttp("GET", "/cookies2") map { res =>
    assertEquals(200, res.statusCode())
    assertEquals(Set("a=1", "b=2"), res.headers().get("Set-Cookie").get)
    res.endHandler(testComplete())
  }

  @Test def indexPost(): Unit = doHttp("POST", "/") map bodyOkCompleter({ body =>
    assertEquals("ok", Json.fromObjectString(body).getString("status"))
  })

  @Test def pathPost(): Unit = doHttp("POST", "/post-ok") map bodyOkCompleter({ body =>
    assertEquals("ok", Json.fromObjectString(body).getString("status"))
  })

  @Test def patchNotFound(): Unit = doHttp("PATCH", "/not-found") map { res =>
    assertEquals(404, res.statusCode())
    testComplete()
  }

  @Test def deleteForbidden(): Unit = doHttp("DELETE", "/forbidden") map { res =>
    assertEquals(403, res.statusCode())
    testComplete()
  }

  @Test def customHead(): Unit = doHttp("HEAD", "/head") map { res =>
    assertEquals(200, res.statusCode())
    assertTrue(res.headers().entryExists("x-custom-head", _ == "hello"))
    testComplete()
  }

  @Test def allMatchesGet(): Unit = doHttp("GET", "/all-test") map bodyOkCompleter { body =>
    assertEquals(testFileContents(), body)
  }

  @Test def allMatchesPost(): Unit = doHttp("POST", "/all-test") map bodyOkCompleter { body =>
    assertEquals(testFileContents(), body)
  }

}
