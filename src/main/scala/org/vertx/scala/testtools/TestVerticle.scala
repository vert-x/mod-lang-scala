package org.vertx.scala.testtools

import java.lang.reflect.InvocationTargetException
import org.junit.runner.RunWith
import org.vertx.scala.platform.Verticle
import org.vertx.testtools.VertxAssert
import scala.concurrent.Future

@RunWith(classOf[ScalaClassRunner])
abstract class TestVerticle extends Verticle {

  override final def start() {
    initialize()
    before()
    asyncBefore() map { _ =>
      startTests()
    } recover {
      case ex: Throwable =>
        VertxAssert.handleThrowable(ex)
    }
  }

  /**
   * Override this method if you want to do things synchronously before starting the tests.
   */
  def before(): Unit = {}

  /**
   * Override this method if you want to do things asynchronously before starting the tests.
   */
  def asyncBefore(): Future[Unit] = Future.successful()

  protected final def initialize(): Unit = VertxAssert.initialize(vertx.asJava)

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