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
import org.vertx.java.core.streams.{ WriteStream => JWriteStream }
import org.vertx.scala.VertxWrapper
import org.vertx.scala.core.buffer.Buffer
import org.vertx.scala.core.FunctionConverters._
import org.vertx.scala.core.Handler

/**
 * @author <a href="http://www.campudus.com/">Joern Bernhardt</a>
 */
trait WrappedWriteStream extends WrappedExceptionSupport with WriteStream {
  override def drainHandler(handler: Handler[Void]): this.type = wrap(internal.drainHandler(handler))

  override def drainHandler(handler: => Unit): this.type = drainHandler(lazyToVoidHandler(handler))

  override def setWriteQueueMaxSize(maxSize: Int): this.type = wrap(internal.setWriteQueueMaxSize(maxSize))

  override def write(data: Buffer): this.type = wrap(internal.write(data.toJava))

  override def writeQueueFull(): Boolean = internal.writeQueueFull()

  override def toJava(): InternalType = internal
}