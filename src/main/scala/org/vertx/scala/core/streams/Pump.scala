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
 * Pumps data from a [[org.vertx.scala.core.streams.ReadStream]] to a
 * [[org.vertx.scala.core.streams.WriteStream]] and performs flow control where necessary to
 * prevent the write stream buffer from getting overfull.<p>
 * Instances of this class read bytes from a [[org.vertx.scala.core.streams.ReadStream]]
 * and write them to a [[org.vertx.scala.core.streams.WriteStream]]. If data
 * can be read faster than it can be written this could result in the write
 * queue of the [[org.vertx.scala.core.streams.WriteStream]] growing
 * without bound, eventually causing it to exhaust all available RAM.<p>
 * To prevent this, after each write, instances of this class check whether the write queue of the
 * [[org.vertx.scala.core.streams.WriteStream]] is full, and if so, the
 * [[org.vertx.scala.core.streams.ReadStream]] is paused, and a `drainHandler` is set on the
 * [[org.vertx.scala.core.streams.WriteStream]]. When the [[org.vertx.scala.core.streams.WriteStream]]
 * has processed half of its backlog, the `drainHandler` will be
 * called, which results in the pump resuming the [[org.vertx.scala.core.streams.ReadStream]].<p>
 * This class can be used to pump from any [[org.vertx.scala.core.streams.ReadStream]]
 * to any [[org.vertx.scala.core.streams.WriteStream]], e.g. from an
 * [[org.vertx.scala.core.http.HttpServerRequest]] to an [[org.vertx.scala.core.file.AsyncFile]],
 * or from [[org.vertx.scala.core.net.NetSocket]] to a [[org.vertx.scala.core.http.WebSocket]].<p>
 *
 * Instances of this class are not thread-safe.<p>
 *
 * @author <a href="http://tfox.org">Tim Fox</a>
 * @author swilliams
 * @author <a href="http://www.campudus.com/">Joern Bernhardt</a>
 */
final class Pump private[scala] (val asJava: JPump) extends Self {

  /**
   * Set the write queue max size to `maxSize`
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
   * Create a new `Pump` with the given `ReadStream` and `WriteStream`
   */
  def apply[A <: ReadStream, B <: WriteStream](rs: ReadStream, ws: WriteStream) = createPump(rs, ws)

  /**
   * Create a new `Pump` with the given `ReadStream` and `WriteStream` and
   * `writeQueueMaxSize`
   */
  def apply[A <: ReadStream, B <: WriteStream](rs: ReadStream, ws: WriteStream, writeQueueMaxSize: Int) =
    createPump(rs, ws, writeQueueMaxSize)

  /**
   * Create a new `Pump` with the given `ReadStream` and `WriteStream`
   */
  def createPump[A <: ReadStream, B <: WriteStream](rs: ReadStream, ws: WriteStream) =
    new Pump(JPump.createPump(rs.asJava, ws.asJava))

  /**
   * Create a new `Pump` with the given `ReadStream` and `WriteStream` and
   * `writeQueueMaxSize`
   */
  def createPump[A <: ReadStream, B <: WriteStream](rs: ReadStream, ws: WriteStream, writeQueueMaxSize: Int) =
    new Pump(JPump.createPump(rs.asJava, ws.asJava, writeQueueMaxSize))

}