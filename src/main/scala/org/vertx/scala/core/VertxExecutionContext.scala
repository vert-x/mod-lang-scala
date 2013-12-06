package org.vertx.scala.core

import scala.concurrent.ExecutionContext
import org.vertx.java.core.logging.Logger

trait VertxExecutionContext extends ExecutionContext {
  def execute(runnable: Runnable): Unit = {
    runnable.run()
  }
  def reportFailure(t: Throwable): Unit = {
    // Use vertx java logger explicitly since scala version is an AnyVal.
    // AnyVal instances cannot be used in structural types, but they're
    // interchangeable with the types they
    type HasLogger = { def logger: Logger }

    this match {
      case x: HasLogger =>
        import scala.language.reflectiveCalls
        x.logger.error("Error executing Future in VertxExecutionContext", t)
      case _ => t.printStackTrace()
    }
  }

  implicit val executionContext = VertxExecutionContext
}

object VertxExecutionContext extends VertxExecutionContext
