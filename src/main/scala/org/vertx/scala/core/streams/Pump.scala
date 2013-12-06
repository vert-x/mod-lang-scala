/*
 * Copyright 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.vertx.scala.core.streams

import org.vertx.java.core.streams.{ Pump => JPump }
import org.vertx.scala.Self

/**
 * Pumps data from a {@link ReadStream} to a {@link WriteStream} and performs flow control where necessary to
 * prevent the write stream buffer from getting overfull.<p>
 * Instances of this class read bytes from a {@link ReadStream} and write them to a {@link WriteStream}. If data
 * can be read faster than it can be written this could result in the write queue of the {@link WriteStream} growing
 * without bound, eventually causing it to exhaust all available RAM.<p>
 * To prevent this, after each write, instances of this class check whether the write queue of the {@link
 * WriteStream} is full, and if so, the {@link ReadStream} is paused, and a {@code drainHandler} is set on the
 * {@link WriteStream}. When the {@link WriteStream} has processed half of its backlog, the {@code drainHandler} will be
 * called, which results in the pump resuming the {@link ReadStream}.<p>
 * This class can be used to pump from any {@link ReadStream} to any {@link WriteStream},
 * e.g. from an {@link org.vertx.java.core.http.HttpServerRequest} to an {@link org.vertx.java.core.file.AsyncFile},
 * or from {@link org.vertx.java.core.net.NetSocket} to a {@link org.vertx.java.core.http.WebSocket}.<p>
 *
 * Instances of this class are not thread-safe.<p>
 *
 * @author <a href="http://tfox.org">Tim Fox</a>
 * @author swilliams
 * @author <a href="http://www.campudus.com/">Joern Bernhardt</a>
 */
// constructor is private because users should use apply in companion
// extends AnyVal to avoid object allocation and improve performance
final class Pump private[scala] (val asJava: JPump) extends AnyVal
  with Self[Pump] {

  override protected[this] def self: Pump = this

  /**
   * Set the write queue max size to {@code maxSize}
   */
  def setWriteQueueMaxSize(maxSize: Int): Pump = wrap(asJava.setWriteQueueMaxSize(maxSize))

  /**
   * Start the Pump. The Pump can be started and stopped multiple times.
   */
  def start(): Pump = wrap(asJava.start())

  /**
   * Stop the Pump. The Pump can be started and stopped multiple times.
   */
  def stop(): Pump = wrap(asJava.stop())

  /**
   * Return the total number of bytes pumped by this pump.
   */
  def bytesPumped(): Int = asJava.bytesPumped()

}

object Pump {

  /**
   * Create a new {@code Pump} with the given {@code ReadStream} and {@code WriteStream}
   */
  def apply[A <: ReadStream[A], B <: WriteStream[B]](rs: ReadStream[A], ws: WriteStream[B]) = createPump(rs, ws)

  /**
   * Create a new {@code Pump} with the given {@code ReadStream} and {@code WriteStream} and
   * {@code writeQueueMaxSize}
   */
  def apply[A <: ReadStream[A], B <: WriteStream[B]](rs: ReadStream[A], ws: WriteStream[B], writeQueueMaxSize: Int) =
    createPump(rs, ws, writeQueueMaxSize)

  /**
   * Create a new {@code Pump} with the given {@code ReadStream} and {@code WriteStream}
   */
  def createPump[A <: ReadStream[A], B <: WriteStream[B]](rs: ReadStream[A], ws: WriteStream[B]) =
    new Pump(JPump.createPump(rs.asJava, ws.asJava))

  /**
   * Create a new {@code Pump} with the given {@code ReadStream} and {@code WriteStream} and
   * {@code writeQueueMaxSize}
   */
  def createPump[A <: ReadStream[A], B <: WriteStream[B]](rs: ReadStream[A], ws: WriteStream[B], writeQueueMaxSize: Int) =
    new Pump(JPump.createPump(rs.asJava, ws.asJava, writeQueueMaxSize))

}