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

import org.vertx.java.core.streams.{ WriteStream => JWriteStream }
import org.vertx.scala.core.buffer.Buffer
import org.vertx.scala.{Self, AsJava}
import org.vertx.scala.core.FunctionConverters._

/**
 * Represents a stream of data that can be written to<p>
 * Any class that implements this interface can be used by a [[org.vertx.scala.core.streams.Pump]]
 * to pump data from a [[org.vertx.scala.core.streams.ReadStream]]
 * to it.<p>
 * This interface exposes a fluent api and the type T represents the type of
 * the object that implements the interface to allow method chaining
 *
 * @author <a href="http://tfox.org">Tim Fox</a>
 * @author swilliams
 * @author <a href="http://www.campudus.com/">Joern Bernhardt</a>
 * @author Galder Zamarreño
 */
trait WriteStream[+S <: WriteStream[S]] extends Any
  with ExceptionSupport[S]
  with Self[S]
  with AsJava {

  override type J <: JWriteStream[_]

  /**
   * Write some data to the stream. The data is put on an internal write queue, and the write actually happens
   * asynchronously. To avoid running out of memory by putting too much on the write queue,
   * check the [[org.vertx.scala.core.streams.WriteStream.writeQueueFull()]]
   * method before writing. This is done automatically if using a [[org.vertx.scala.core.streams.Pump]].
   */
  def write(data: Buffer): S = wrap(asJava.write(data.asJava))

  /**
   * Set the maximum size of the write queue to `maxSize`. You will still be able to write to the stream even
   * if there is more than `maxSize` bytes in the write queue. This is used as an indicator by classes such as
   * `Pump` to provide flow control.
   */
  def setWriteQueueMaxSize(maxSize: Int): S = wrap(asJava.setWriteQueueMaxSize(maxSize))

  /**
   * This will return `true` if there are more bytes in the write queue than the value set using
   * [[org.vertx.scala.core.streams.WriteStream.setWriteQueueMaxSize()]]
   */
  def writeQueueFull(): Boolean = asJava.writeQueueFull()

  /**
   * Set a drain handler on the stream. If the write queue is full, then the handler will be called when the write
   * queue has been reduced to maxSize / 2. See [[org.vertx.scala.core.streams.Pump]] for an example of this being used.
   */
  def drainHandler(handler: => Unit): S = wrap(asJava.drainHandler(lazyToVoidHandler(handler)))

}
