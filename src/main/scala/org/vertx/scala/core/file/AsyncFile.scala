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
package org.vertx.scala.core.file

import org.vertx.java.core.file.{ AsyncFile => JAsyncFile }
import org.vertx.java.core.buffer.Buffer
import org.vertx.scala.core.streams.{ WriteStream, ReadStream }
import org.vertx.java.core.AsyncResult
import org.vertx.scala.core.FunctionConverters._
import org.vertx.scala.core.streams.WrappedReadAndWriteStream

/**
 * @author Edgar Chan
 * @author swilliams
 * @author <a href="http://www.campudus.com/">Joern Bernhardt</a>
 */
object AsyncFile {
  def apply(internal: JAsyncFile) = new AsyncFile(internal)
}

class AsyncFile(protected[this] val internal: JAsyncFile) extends WrappedReadAndWriteStream[AsyncFile, JAsyncFile] {

  def close(): Unit = internal.close()

  def close(handler: (AsyncResult[Unit]) => Unit): Unit = internal.close(voidAsyncHandler(handler))

  def write(data: Buffer, p: Int, handler: () => Unit): AsyncFile = wrap(internal.write(data, p, handler))

  def read(buffer: Buffer, offset: Int, position: Int, length: Int, handler: (AsyncResult[Buffer]) => Unit): AsyncFile =
    wrap(internal.read(buffer, offset, position, length, handler))

  def flush(): AsyncFile = wrap(internal.flush())

  def flush(handler: () => Unit): AsyncFile = wrap(flush(handler))

}
