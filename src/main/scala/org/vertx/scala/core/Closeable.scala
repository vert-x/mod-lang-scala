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

import org.vertx.scala.AsJava
import org.vertx.scala.core.FunctionConverters._

/**
 * Signals that an instance can be closed.
 *
 * @author <a href="http://www.campudus.com/">Joern Bernhardt</a>
 * @author Galder Zamarreño
 */
trait Closeable extends Any with AsJava {

  override type J <: CloseType

  type CloseType = {
    def close(): Unit
    def close(handler: Handler[AsyncResult[Void]]): Unit
  }

  /**
   * Close this [[org.vertx.scala.core.Closeable]] instance asynchronously.
   */
  def close(): Unit = asJava.close()

  /**
   * Close this [[org.vertx.scala.core.Closeable]] instance asynchronously
   * and notifies the handler once done.
   */
  def close(handler: AsyncResult[Void] => Unit): Unit = asJava.close(fnToHandler(handler))

}