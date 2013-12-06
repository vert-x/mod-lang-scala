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

import org.vertx.java.core.streams.{ ExceptionSupport => JExceptionSupport }
import org.vertx.scala.{Self, AsJava}
import org.vertx.scala.core.FunctionConverters._

/**
 * Exception handler.
 *
 * @author swilliams
 * @author <a href="http://www.campudus.com/">Joern Bernhardt</a>
 * @author Galder Zamarreño
 */
trait ExceptionSupport[+S <: ExceptionSupport[S]] extends Any
  with Self[S]
  with AsJava {

  override type J <: JExceptionSupport[_]

  /**
   * Set an exception handler.
   */
  def exceptionHandler(handler: Throwable => Unit): S = wrap(asJava.exceptionHandler(handler))

}

