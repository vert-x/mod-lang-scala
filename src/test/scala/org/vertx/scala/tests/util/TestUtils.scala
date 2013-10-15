package org.vertx.scala.tests.util

import org.vertx.scala.core.AsyncResult
import org.vertx.testtools.VertxAssert._

object TestUtils {
  def completeWithArFailed[T]: AsyncResult[T] => Unit = { ar =>
    assertTrue(ar.failed())
    assertTrue(ar.cause() != null)
    testComplete()
  }

}