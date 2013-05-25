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
import org.vertx.java.core.streams.{WriteStream => JWriteStream}
import org.vertx.java.core.Handler


/**
 * @author swilliams
 * 
 */
trait WriteStream {

  def drainHandler(handler: () => Unit):WriteStream.this.type

  def exceptionHandler(handler: Handler[Throwable]):WriteStream.this.type

  def setWriteQueueMaxSize(maxSize: Int):WriteStream.this.type

  def writeBuffer(data: Buffer):WriteStream.this.type

  def writeQueueFull():Boolean

}