package org.vertx.scala.core

import scala.concurrent.ExecutionContext
import org.vertx.scala.core.logging.Logger

trait VertxExecutionContext extends ExecutionContext {
  type HasLogger = { def logger: Logger }
  def execute(runnable: Runnable): Unit = {
    runnable.run()
  }
  def reportFailure(t: Throwable): Unit = {
    this match {
      case x: HasLogger => x.logger.error("Error executing Future in VertxExecutionContext", t)
      case _ => t.printStackTrace()
    }
  }

  implicit val executionContext = VertxExecutionContext
}

object VertxExecutionContext extends VertxExecutionContext
