package org.vertx.scala.testtools

import java.lang.reflect.InvocationTargetException
import scala.concurrent.Future
import org.junit.Test
import org.junit.runner.RunWith
import org.vertx.scala.platform.Verticle
import org.vertx.scala.core.VertxExecutionContext
import org.vertx.testtools.VertxAssert
import org.vertx.scala.core.Vertx

@RunWith(classOf[ScalaClassRunner])
abstract class TestVerticle extends Verticle with VertxExecutionContext {
  override final def start() {
    initialize()
    before()
    asyncBefore() map { _ =>
      startTests()
    } recover {
      case ex: Throwable => VertxAssert.handleThrowable(ex)
    }
  }

  /**
   * Override this method if you want to do things synchronously before starting the tests.
   */
  def before() {}

  /**
   * Override this method if you want to do things asynchronously before starting the tests.
   */
  def asyncBefore(): Future[Unit] = Future.successful()

  protected final def initialize(): Unit = VertxAssert.initialize(vertx.internal)

  protected final def startTests() {
    val methodName = container.config().getString("methodName")
    try {
      val m = getClass().getDeclaredMethod(methodName)
      m.invoke(this)
    } catch {
      case e: InvocationTargetException =>
        val targetEx = e.getTargetException()
        VertxAssert.handleThrowable(targetEx)
      case t: Throwable =>
        // Problem with invoking
        VertxAssert.handleThrowable(t)
    }
  }

}