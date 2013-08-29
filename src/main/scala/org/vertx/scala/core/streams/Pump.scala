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

import org.vertx.java.core.buffer.Buffer
import org.vertx.java.core.streams.{Pump => JPump}

/**
 * @author swilliams
 * @author <a href="http://www.campudus.com/">Joern Bernhardt</a>
 */
object Pump {
  def createPump(rs: ReadStream[_], ws: WriteStream[_]) = {
    new Pump(JPump.createPump(rs, ws))
  }
  def createPump(rs: ReadStream[_], ws: WriteStream[_], writeQueueMaxSize: Int) = {
    new Pump(JPump.createPump(rs, ws, writeQueueMaxSize))
  }
}

class Pump(protected[this] val internal: JPump) extends WrappedReadAndWriteStream[Pump, JPump] {

  def start(): Pump = wrap(internal.start())

  def stop(): Pump = wrap(internal.stop())

}