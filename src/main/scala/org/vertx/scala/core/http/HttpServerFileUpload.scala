/*
 * Copyright 2014 the original author or authors.
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
package org.vertx.scala.core.http

import org.vertx.java.core.http.{ HttpServerFileUpload => JHttpServerFileUpload }
import java.nio.charset.Charset
import org.vertx.scala.Self
import org.vertx.scala.core.streams.ReadStream

/**
 * @author Galder Zamarreño
 */
final class HttpServerFileUpload private[scala] (val asJava: JHttpServerFileUpload) extends Self
  with ReadStream {

  override type J = JHttpServerFileUpload

  /**
   * Stream the content of this upload to the given filename.
   */
  def streamToFileSystem(filename: String): HttpServerFileUpload =
    wrap(asJava.streamToFileSystem(filename))

  /**
   * Returns the filename which was used when upload the file.
   */
  def filename(): String = asJava.filename()

  /**
   * Returns the name of the attribute
   */
  def name(): String = asJava.name()

  /**
   * Returns the contentType for the upload
   */
  def contentType(): String = asJava.contentType()

  /**
   * Returns the contentTransferEncoding for the upload
   */
  def contentTransferEncoding(): String = asJava.contentTransferEncoding()

  /**
   * Returns the charset for the upload
   */
  def charset(): Charset = asJava.charset()

  /**
   * Returns the size of the upload (in bytes)
   */
  def size(): Long = asJava.size()

}

object HttpServerFileUpload {
  def apply(internal: JHttpServerFileUpload) = new HttpServerFileUpload(internal)
}
