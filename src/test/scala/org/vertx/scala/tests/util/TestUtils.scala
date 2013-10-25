package org.vertx.scala.tests.util

import org.vertx.scala.core.AsyncResult
import org.vertx.testtools.VertxAssert._
import org.vertx.scala.platform.Container

object TestUtils {

  def completeWithArFailed[T]: AsyncResult[T] => Unit = { ar =>
    assertTrue(ar.failed())
    assertTrue(ar.cause() != null)
    testComplete()
  }

  def startTests(script: AnyRef, container: Container): Unit = {
    val methodName = container.config().getString("methodName")
    script.getClass.getMethod(methodName).invoke(script)
  }

}