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
import org.vertx.scala.core.streams.WrappedReadWriteStream
import org.vertx.java.core.Handler

/**
 * @author Edgar Chan
 * @author swilliams
 * @author <a href="http://www.campudus.com/">Joern Bernhardt</a>
 */
object AsyncFile {
  def apply(internal: JAsyncFile) = new AsyncFile(internal)
}

class AsyncFile(protected[this] val internal: JAsyncFile) extends JAsyncFile with WrappedReadWriteStream {
  override type InternalType = JAsyncFile

  override def close(): Unit = internal.close()
  override def close(handler: Handler[AsyncResult[Void]]): Unit = internal.close(handler)
  override def flush(): AsyncFile = wrap(internal.flush())
  override def flush(handler: Handler[org.vertx.java.core.AsyncResult[Void]]): org.vertx.java.core.file.AsyncFile = internal.flush(handler)
  override def read(buffer: Buffer, offset: Int, position: Int, length: Int, handler: Handler[AsyncResult[Buffer]]): AsyncFile = wrap(internal.read(buffer, offset, position, length, handler))
  override def write(buffer: Buffer, position: Int, handler: Handler[AsyncResult[Void]]): AsyncFile = wrap(internal.write(buffer, position, handler))

}
