/*
 * Copyright 2011-2013 the original author or authors.
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

package org.vertx.scala.core.streams

import org.vertx.java.core.buffer.Buffer
import org.vertx.java.core.streams.{ ReadStream => JReadStream }
import org.vertx.java.core.streams.{ ExceptionSupport => JExceptionSupport }
import org.vertx.java.core.Handler
import org.vertx.scala.VertxWrapper
import org.vertx.scala.core.FunctionConverters._

/**
 * @author swilliams
 * @author <a href="http://www.campudus.com/">Joern Bernhardt</a>
 */
trait ExceptionSupport {
  type InternalType <: JExceptionSupport[_]

  /**
   * Set an exception handler.
   */
  def exceptionHandler(handler: Handler[Throwable]): this.type

  /**
   * Set an exception handler.
   */
  def exceptionHandler(handler: Throwable => Unit): this.type

  def toJava(): InternalType
}

trait WrappedExceptionSupport extends ExceptionSupport with VertxWrapper {
  type InternalType <: JExceptionSupport[_]
  override def exceptionHandler(handler: Handler[Throwable]): this.type = wrap(internal.exceptionHandler(handler))
  override def exceptionHandler(handler: Throwable => Unit): this.type = exceptionHandler(fnToHandler(handler))
  override def toJava(): InternalType = internal
}