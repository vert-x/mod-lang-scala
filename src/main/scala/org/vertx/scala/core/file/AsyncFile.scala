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
import org.vertx.scala.core.buffer._
import org.vertx.java.core.buffer.{ Buffer => JBuffer }
import org.vertx.scala.core.streams.{ WriteStream, ReadStream }
import org.vertx.scala.core.AsyncResult
import org.vertx.scala.core.FunctionConverters._
import org.vertx.scala.core.streams.WrappedReadWriteStream

/**
 * Represents a file on the file-system which can be read from, or written to asynchronously.<p>
 * This class also implements {@link org.vertx.java.core.streams.ReadStream} and
 * {@link org.vertx.java.core.streams.WriteStream}. This allows the data to be pumped to and from
 * other streams, e.g. an {@link org.vertx.java.core.http.HttpClientRequest} instance,
 * using the {@link org.vertx.java.core.streams.Pump} class<p>
 * Instances of AsyncFile are not thread-safe<p>
 *
 * @author <a href="http://tfox.org">Tim Fox</a>
 * @author Edgar Chan
 * @author swilliams
 * @author <a href="http://www.campudus.com/">Joern Bernhardt</a>
 */
class AsyncFile(protected[this] val internal: JAsyncFile) extends WrappedReadWriteStream {
  override type InternalType = JAsyncFile

  /**
   * Close the file. The actual close happens asynchronously.
   */
  def close(): Unit = internal.close()

  /**
   * Close the file. The actual close happens asynchronously.
   * The handler will be called when the close is complete, or an error occurs.
   */
  def close(handler: AsyncResult[Void] => Unit): Unit = internal.close(handler)

  /**
   * Write a {@link Buffer} to the file at position {@code position} in the file, asynchronously.
   * If {@code position} lies outside of the current size
   * of the file, the file will be enlarged to encompass it.<p>
   * When multiple writes are invoked on the same file
   * there are no guarantees as to order in which those writes actually occur.<p>
   * The handler will be called when the write is complete, or if an error occurs.
   */
  def write(buffer: Buffer, position: Int, handler: AsyncResult[Void] => Unit): AsyncFile =
    wrap(internal.write(buffer.toJava, position, handler))

  /**
   * Reads {@code length} bytes of data from the file at position {@code position} in the file, asynchronously.
   * The read data will be written into the specified {@code Buffer buffer} at position {@code offset}.<p>
   * If data is read past the end of the file then zero bytes will be read.<p>
   * When multiple reads are invoked on the same file there are no guarantees as to order in which those reads actually occur.<p>
   * The handler will be called when the close is complete, or if an error occurs.
   */
  def read(buffer: Buffer, offset: Int, position: Int, length: Int, handler: AsyncResult[Buffer] => Unit): AsyncFile =
    wrap(internal.read(buffer.toJava, offset, position, length, asyncResultConverter { jbuf: JBuffer => Buffer.apply(jbuf) }(handler)))

  /**
   * Flush any writes made to this file to underlying persistent storage.<p>
   * If the file was opened with {@code flush} set to {@code true} then calling this method will have no effect.<p>
   * The actual flush will happen asynchronously.
   */
  def flush(): AsyncFile = wrap(internal.flush())

  /**
   * Same as {@link #flush} but the handler will be called when the flush is complete or if an error occurs
   */
  def flush(handler: AsyncResult[Void] => Unit): AsyncFile = wrap(internal.flush(handler))

}

/** Factory for [[file.AsyncFile]] instances. */
object AsyncFile {
  def apply(internal: JAsyncFile) = new AsyncFile(internal)
}
