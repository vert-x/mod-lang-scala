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
package org.vertx.scala.core.streams

import org.vertx.java.core.streams.{ DrainSupport => JDrainSupport }
import org.vertx.scala.{Self, AsJava}
import org.vertx.scala.core.FunctionConverters._

/**
 * Allows to set a `Handler` which is notified once the write queue is
 * drained again. This way you can stop writing once the write queue consumes
 * to much memory and so prevent an OutOfMemoryError.
 *
 * @author <a href="mailto:nmaurer@redhat.com">Norman Maurer</a>
 * @author Galder Zamarreño
 */
trait DrainSupport[+S <: DrainSupport[S]] extends Any
  with Self[S]
  with AsJava {

  override type J <: JDrainSupport[_]

  /**
   * Set the maximum size of the write queue to `maxSize`. You will still be
   * able to write to the stream even if there is more than `maxSize` bytes in
   * the write queue. This is used as an indicator by classes such as `Pump`
   * to provide flow control.
   */
  def setWriteQueueMaxSize(maxSize: Int): S = wrap(asJava.setWriteQueueMaxSize(maxSize))

  /**
   * This will return `true` if there are more bytes in the write queue than
   * the value set using [[org.vertx.scala.core.streams.DrainSupport.setWriteQueueMaxSize]]
   */
  def writeQueueFull: Boolean = asJava.writeQueueFull()

  /**
   * Set a drain handler on the stream. If the write queue is full, then the
   * handler will be called when the write queue has been reduced to
   * maxSize / 2. See `Pump` for an example of this being used.
   */
  def drainHandler(handler: => Unit): S = wrap(asJava.drainHandler(lazyToVoidHandler(handler)))

}
