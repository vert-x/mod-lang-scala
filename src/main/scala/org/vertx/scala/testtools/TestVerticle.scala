package org.vertx.scala.testtools

import java.lang.reflect.{Method, InvocationTargetException}
import org.junit.runner.RunWith
import org.vertx.scala.platform.Verticle
import org.vertx.testtools.VertxAssert
import scala.concurrent.{ExecutionContext, Future}
import org.vertx.java.core.impl.{WorkerContext, DefaultContext, VertxInternal}
import org.vertx.scala.core.VertxExecutionContext

@RunWith(classOf[ScalaClassRunner])
abstract class TestVerticle extends Verticle {

  implicit val executionContext = VertxExecutionContext.fromVertxAccess(this)

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

  /**
   * Expected thread
   */
  var th: Thread = _

  /**
   * Vertx internal context
   */
  var context: DefaultContext = _

  protected final def initialize(): Unit = {
    VertxAssert.initialize(vertx.asJava)
    th = Thread.currentThread
    context = vertx.asJava.asInstanceOf[VertxInternal].getContext
  }

  protected final def startTests() {
    val methodName = container.config().getString("methodName")
    try {
      val m = findMethod(getClass, methodName)
      m.invoke(this)
    } catch {
      case e: InvocationTargetException =>
        val targetEx = e.getTargetException
        VertxAssert.handleThrowable(targetEx)
      case t: Throwable =>
        // Problem with invoking
        VertxAssert.handleThrowable(t)
    }
  }

  protected final def assertThread() {
    if (!context.isInstanceOf[WorkerContext] && th != Thread.currentThread)
      throw new IllegalStateException(
        s"Expected: $th Actual: ${Thread.currentThread}")

    if (context != vertx.asJava.asInstanceOf[VertxInternal].getContext)
      throw new IllegalStateException(
        s"Wrong context: Expected: $context Actual: ${vertx.asInstanceOf[VertxInternal].getContext}")
  }

  def findMethod(clazz: Class[_], methodName: String): Method = {
    try {
      logger.info(s"Find method '$methodName' in $clazz")
      clazz.getDeclaredMethod(methodName)
    } catch {
      case e: NoSuchMethodException =>
        if ((clazz == classOf[AnyRef]) || clazz.isInterface)
          throw e

        val superclass = clazz.getSuperclass
        logger.info(s"Method not found, try super-class $superclass")
        findMethod(superclass, methodName)
    }
  }

}