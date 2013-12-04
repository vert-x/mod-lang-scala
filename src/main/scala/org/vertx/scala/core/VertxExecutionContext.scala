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
import org.vertx.java.core.logging.Logger

/**
 * Vert.x Scala execution context
 *
 * @author <a href="http://www.campudus.com/">Joern Bernhardt</a>
 */
trait VertxExecutionContext extends ExecutionContext {
  type HasLogger = { def logger: Logger }
  def execute(runnable: Runnable): Unit = {
    runnable.run()
  }
  def reportFailure(t: Throwable): Unit = {
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
