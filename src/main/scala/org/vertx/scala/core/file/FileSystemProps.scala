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

import org.vertx.java.core.file.{ FileSystemProps => JFileSystemProps }
import org.vertx.scala.AsJava

/**
 * Represents properties of the file system.<p>
 * Instances of FileSystemProps are thread-safe.<p>
 *
 * @author <a href="http://tfox.org">Tim Fox</a>
 * @author <a href="http://www.campudus.com/">Joern Bernhardt</a>
 * @author Galder Zamarreño
 */
final class FileSystemProps private[scala] (val asJava: JFileSystemProps) extends AnyVal
  with AsJava {

  override type J = JFileSystemProps

  /**
   * The total space on the file system, in bytes
   */
  def totalSpace(): Long = asJava.totalSpace()

  /**
   * The total un-allocated space on the file system, in bytes
   */
  def unallocatedSpace(): Long = asJava.unallocatedSpace()

  /**
   * The total usable space on the file system, in bytes
   */
  def usableSpace(): Long = asJava.usableSpace()

}

/** Factory for [[org.vertx.scala.core.file.FileSystemProps]] instances. */
object FileSystemProps {
  def apply(internal: JFileSystemProps) = new FileSystemProps(internal)
}