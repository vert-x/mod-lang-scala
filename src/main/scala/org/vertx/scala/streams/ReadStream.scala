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

package org.vertx.scala.streams

import org.vertx.java.core.buffer.Buffer
import org.vertx.java.core.streams.{ReadStream => JReadStream}
import org.vertx.scala.handlers.FunctionHandler1
import org.vertx.scala.handlers.FunctionHandler0

/**
 * @author swilliams
 * 
 */
trait ReadStream {

  def dataHandler(handler: (Buffer) => Unit):ReadStream.this.type

  def endHandler(handler: () => Unit):ReadStream.this.type

  def exceptionHandler(handler: (Exception) => Unit):ReadStream.this.type

  def pause():ReadStream.this.type

  def resume():ReadStream.this.type

}