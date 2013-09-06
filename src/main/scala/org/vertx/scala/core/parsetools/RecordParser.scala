/*
 * Copyright 2011-2012 the original author or authors.
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

package org.vertx.scala.core.parsetools

import scala.language.implicitConversions

import org.vertx.java.core.Handler
import org.vertx.java.core.buffer.{Buffer => JBuffer}
import org.vertx.java.core.parsetools.{RecordParser => JRecordParser}
import org.vertx.scala.core.FunctionConverters.convertFunctionToParameterisedHandler
import org.vertx.scala.core.buffer.Buffer

/**
 * A helper class which allows you to easily parse protocols which are delimited by a sequence of bytes, or fixed
 * size records.<p>
 * Instances of this class take as input {@link Buffer} instances containing raw bytes, and output records.<p>
 * For example, if I had a simple ASCII text protocol delimited by '\n' and the input was the following:<p>
 * <pre>
 * buffer1:HELLO\nHOW ARE Y
 * buffer2:OU?\nI AM
 * buffer3: DOING OK
 * buffer4:\n
 * </pre>
 * Then the output would be:<p>
 * <pre>
 * buffer1:HELLO
 * buffer2:HOW ARE YOU?
 * buffer3:I AM DOING OK
 * </pre>
 * Instances of this class can be changed between delimited mode and fixed size record mode on the fly as
 * individual records are read, this allows you to parse protocols where, for example, the first 5 records might
 * all be fixed size (of potentially different sizes), followed by some delimited records, followed by more fixed
 * size records.<p>
 * Instances of this class can't currently be used for protocols where the text is encoded with something other than
 * a 1-1 byte-char mapping. TODO extend this class to cope with arbitrary character encodings<p>
 *
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
object RecordParser {
  /**
   * Helper method to convert a latin-1 String to an array of bytes for use as a delimiter
   * Please do not use this for non latin-1 characters
   *
   * @param str
   * @return The byte[] form of the string
   */
  def latin1StringToBytes(str: String): Array[Byte] = JRecordParser.latin1StringToBytes(str)

  /**
   * Create a new {@code RecordParser} instance, initially in delimited mode, and where the delimiter can be represented
   * by the String {@code} delim endcoded in latin-1 . Don't use this if your String contains other than latin-1 characters.<p>
   * {@code output} Will receive whole records which have been parsed.
   */
  def newDelimited(delim: String, handler: Buffer => Unit) = JRecordParser.newDelimited(delim, handler.compose(Buffer))

  /**
   * Create a new {@code RecordParser} instance, initially in delimited mode, and where the delimiter can be represented
   * by the {@code byte[]} delim.<p>
   * {@code output} Will receive whole records which have been parsed.
   */
  def newDelimited(delim: Array[Byte], handler: Buffer => Unit) = JRecordParser.newDelimited(delim, handler.compose(Buffer))

  /**
   * Create a new {@code RecordParser} instance, initially in fixed size mode, and where the record size is specified
   * by the {@code size} parameter.<p>
   * {@code output} Will receive whole records which have been parsed.
   */
  def newFixed(size: Int, handler: Buffer => Unit) = JRecordParser.newFixed(size, handler.compose(Buffer))
}
