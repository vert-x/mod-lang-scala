/*
 * Copyright 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.vertx.scala.core

import scala.concurrent.ExecutionContext
import org.vertx.scala.core.logging.Logger

/**
 * Vert.x Scala execution context
 *
 * @author <a href="http://www.campudus.com/">Joern Bernhardt</a>
 * @author Galder Zamarreño
 */
object VertxExecutionContext {

  /**
   * Vert.x execution context for use in verticles extending [[VertxAccess]]
   * trait. Scala verticles do extend this trait to facilitate access to
   * internal components.
   */
  def fromVertxAccess(vertxAccess: VertxAccess): ExecutionContext =
    new VertxExecutionContextImpl(vertxAccess.vertx, vertxAccess.logger)

  /**
   * Vert.x execution context for situations where verticles are written in
   * other languages except Scala, where there's no access to [[VertxAccess]],
   * but access to [[Logger]] and [[Vertx]] class instances are available.
   */
  def fromVertx(vertx: => Vertx, logger: => Logger): ExecutionContext =
    new VertxExecutionContextImpl(vertx, logger)

  private final class VertxExecutionContextImpl(vertx: => Vertx, logger: => Logger) extends ExecutionContext {
    override def reportFailure(t: Throwable): Unit =
      logger.error("Error executing Future in VertxExecutionContext", t)
    override def execute(runnable: Runnable): Unit =
      runnable.run()
  }

}
