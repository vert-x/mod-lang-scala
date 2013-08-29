package org.vertx.scala.core

import scala.concurrent.ExecutionContext

trait VertxExecutionContext extends ExecutionContext {
  def execute(runnable: Runnable): Unit = runnable.run()
  def reportFailure(t: Throwable): Unit = println("failed while executing in vertx context", t)

  implicit val executionContext = VertxExecutionContext
}

object VertxExecutionContext extends VertxExecutionContext
