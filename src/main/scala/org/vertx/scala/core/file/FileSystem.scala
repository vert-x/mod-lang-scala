/*
 * Copyright 2011-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License")
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

import org.vertx.java.core.{ Handler, AsyncResult }
import org.vertx.java.core.buffer.Buffer
import org.vertx.java.core.file.{ FileProps, FileSystemProps, AsyncFile => JAsyncFile }
import org.vertx.java.core.file.{ FileSystem => JFileSystem }
import org.vertx.scala.VertxWrapper

/**
 * @author swilliams
 * @author <a href="http://www.campudus.com/">Joern Bernhardt</a>
 */
object FileSystem {
  def apply(actual: JFileSystem) = new FileSystem(actual)
}

class FileSystem(protected[this] val internal: JFileSystem) extends JFileSystem with VertxWrapper {
  override type InternalType = JFileSystem

  override def chmod(x$1: String, x$2: String, x$3: String, x$4: org.vertx.java.core.Handler[org.vertx.java.core.AsyncResult[Void]]): org.vertx.java.core.file.FileSystem = ???
  override def chmod(x$1: String, x$2: String, x$3: org.vertx.java.core.Handler[org.vertx.java.core.AsyncResult[Void]]): org.vertx.java.core.file.FileSystem = ???
  override def chmodSync(x$1: String, x$2: String, x$3: String): org.vertx.java.core.file.FileSystem = ???
  override def chmodSync(x$1: String, x$2: String): org.vertx.java.core.file.FileSystem = ???
  override def copy(x$1: String, x$2: String, x$3: Boolean, x$4: org.vertx.java.core.Handler[org.vertx.java.core.AsyncResult[Void]]): org.vertx.java.core.file.FileSystem = ???
  override def copy(x$1: String, x$2: String, x$3: org.vertx.java.core.Handler[org.vertx.java.core.AsyncResult[Void]]): org.vertx.java.core.file.FileSystem = ???
  override def copySync(x$1: String, x$2: String, x$3: Boolean): org.vertx.java.core.file.FileSystem = ???
  override def copySync(x$1: String, x$2: String): org.vertx.java.core.file.FileSystem = ???
  override def createFile(x$1: String, x$2: String, x$3: org.vertx.java.core.Handler[org.vertx.java.core.AsyncResult[Void]]): org.vertx.java.core.file.FileSystem = ???
  override def createFile(x$1: String, x$2: org.vertx.java.core.Handler[org.vertx.java.core.AsyncResult[Void]]): org.vertx.java.core.file.FileSystem = ???
  override def createFileSync(x$1: String, x$2: String): org.vertx.java.core.file.FileSystem = ???
  override def createFileSync(x$1: String): org.vertx.java.core.file.FileSystem = ???
  override def delete(x$1: String, x$2: Boolean, x$3: org.vertx.java.core.Handler[org.vertx.java.core.AsyncResult[Void]]): org.vertx.java.core.file.FileSystem = ???
  override def delete(x$1: String, x$2: org.vertx.java.core.Handler[org.vertx.java.core.AsyncResult[Void]]): org.vertx.java.core.file.FileSystem = ???
  override def deleteSync(x$1: String, x$2: Boolean): org.vertx.java.core.file.FileSystem = ???
  override def deleteSync(x$1: String): org.vertx.java.core.file.FileSystem = ???
  override def exists(x$1: String, x$2: org.vertx.java.core.Handler[org.vertx.java.core.AsyncResult[java.lang.Boolean]]): org.vertx.java.core.file.FileSystem = ???
  override def existsSync(x$1: String): Boolean = ???
  override def fsProps(x$1: String, x$2: org.vertx.java.core.Handler[org.vertx.java.core.AsyncResult[org.vertx.java.core.file.FileSystemProps]]): org.vertx.java.core.file.FileSystem = ???
  override def fsPropsSync(x$1: String): org.vertx.java.core.file.FileSystemProps = ???
  override def link(x$1: String, x$2: String, x$3: org.vertx.java.core.Handler[org.vertx.java.core.AsyncResult[Void]]): org.vertx.java.core.file.FileSystem = ???
  override def linkSync(x$1: String, x$2: String): org.vertx.java.core.file.FileSystem = ???
  override def lprops(x$1: String, x$2: org.vertx.java.core.Handler[org.vertx.java.core.AsyncResult[org.vertx.java.core.file.FileProps]]): org.vertx.java.core.file.FileSystem = ???
  override def lpropsSync(x$1: String): org.vertx.java.core.file.FileProps = ???
  override def mkdir(x$1: String, x$2: String, x$3: Boolean, x$4: org.vertx.java.core.Handler[org.vertx.java.core.AsyncResult[Void]]): org.vertx.java.core.file.FileSystem = ???
  override def mkdir(x$1: String, x$2: String, x$3: org.vertx.java.core.Handler[org.vertx.java.core.AsyncResult[Void]]): org.vertx.java.core.file.FileSystem = ???
  override def mkdir(x$1: String, x$2: Boolean, x$3: org.vertx.java.core.Handler[org.vertx.java.core.AsyncResult[Void]]): org.vertx.java.core.file.FileSystem = ???
  override def mkdir(x$1: String, x$2: org.vertx.java.core.Handler[org.vertx.java.core.AsyncResult[Void]]): org.vertx.java.core.file.FileSystem = ???
  override def mkdirSync(x$1: String, x$2: String, x$3: Boolean): org.vertx.java.core.file.FileSystem = ???
  override def mkdirSync(x$1: String, x$2: String): org.vertx.java.core.file.FileSystem = ???
  override def mkdirSync(x$1: String, x$2: Boolean): org.vertx.java.core.file.FileSystem = ???
  override def mkdirSync(x$1: String): org.vertx.java.core.file.FileSystem = ???
  override def move(x$1: String, x$2: String, x$3: org.vertx.java.core.Handler[org.vertx.java.core.AsyncResult[Void]]): org.vertx.java.core.file.FileSystem = ???
  override def moveSync(x$1: String, x$2: String): org.vertx.java.core.file.FileSystem = ???
  override def open(x$1: String, x$2: String, x$3: Boolean, x$4: Boolean, x$5: Boolean, x$6: Boolean, x$7: org.vertx.java.core.Handler[org.vertx.java.core.AsyncResult[org.vertx.java.core.file.AsyncFile]]): org.vertx.java.core.file.FileSystem = ???
  override def open(x$1: String, x$2: String, x$3: Boolean, x$4: Boolean, x$5: Boolean, x$6: org.vertx.java.core.Handler[org.vertx.java.core.AsyncResult[org.vertx.java.core.file.AsyncFile]]): org.vertx.java.core.file.FileSystem = ???
  override def open(x$1: String, x$2: String, x$3: Boolean, x$4: org.vertx.java.core.Handler[org.vertx.java.core.AsyncResult[org.vertx.java.core.file.AsyncFile]]): org.vertx.java.core.file.FileSystem = ???
  override def open(x$1: String, x$2: String, x$3: org.vertx.java.core.Handler[org.vertx.java.core.AsyncResult[org.vertx.java.core.file.AsyncFile]]): org.vertx.java.core.file.FileSystem = ???
  override def open(x$1: String, x$2: org.vertx.java.core.Handler[org.vertx.java.core.AsyncResult[org.vertx.java.core.file.AsyncFile]]): org.vertx.java.core.file.FileSystem = ???
  override def openSync(x$1: String, x$2: String, x$3: Boolean, x$4: Boolean, x$5: Boolean, x$6: Boolean): org.vertx.java.core.file.AsyncFile = ???
  override def openSync(x$1: String, x$2: String, x$3: Boolean, x$4: Boolean, x$5: Boolean): org.vertx.java.core.file.AsyncFile = ???
  override def openSync(x$1: String, x$2: String, x$3: Boolean): org.vertx.java.core.file.AsyncFile = ???
  override def openSync(x$1: String, x$2: String): org.vertx.java.core.file.AsyncFile = ???
  override def openSync(x$1: String): org.vertx.java.core.file.AsyncFile = ???
  override def props(x$1: String, x$2: org.vertx.java.core.Handler[org.vertx.java.core.AsyncResult[org.vertx.java.core.file.FileProps]]): org.vertx.java.core.file.FileSystem = ???
  override def propsSync(x$1: String): org.vertx.java.core.file.FileProps = ???
  override def readDir(x$1: String, x$2: String, x$3: org.vertx.java.core.Handler[org.vertx.java.core.AsyncResult[Array[String]]]): org.vertx.java.core.file.FileSystem = ???
  override def readDir(x$1: String, x$2: org.vertx.java.core.Handler[org.vertx.java.core.AsyncResult[Array[String]]]): org.vertx.java.core.file.FileSystem = ???
  override def readDirSync(x$1: String, x$2: String): Array[String] = ???
  override def readDirSync(x$1: String): Array[String] = ???
  override def readFile(x$1: String, x$2: org.vertx.java.core.Handler[org.vertx.java.core.AsyncResult[org.vertx.java.core.buffer.Buffer]]): org.vertx.java.core.file.FileSystem = ???
  override def readFileSync(x$1: String): org.vertx.java.core.buffer.Buffer = ???
  override def readSymlink(x$1: String, x$2: org.vertx.java.core.Handler[org.vertx.java.core.AsyncResult[String]]): org.vertx.java.core.file.FileSystem = ???
  override def readSymlinkSync(x$1: String): String = ???
  override def symlink(x$1: String, x$2: String, x$3: org.vertx.java.core.Handler[org.vertx.java.core.AsyncResult[Void]]): org.vertx.java.core.file.FileSystem = ???
  override def symlinkSync(x$1: String, x$2: String): org.vertx.java.core.file.FileSystem = ???
  override def truncate(x$1: String, x$2: Long, x$3: org.vertx.java.core.Handler[org.vertx.java.core.AsyncResult[Void]]): org.vertx.java.core.file.FileSystem = ???
  override def truncateSync(x$1: String, x$2: Long): org.vertx.java.core.file.FileSystem = ???
  override def unlink(x$1: String, x$2: org.vertx.java.core.Handler[org.vertx.java.core.AsyncResult[Void]]): org.vertx.java.core.file.FileSystem = ???
  override def unlinkSync(x$1: String): org.vertx.java.core.file.FileSystem = ???
  override def writeFile(x$1: String, x$2: org.vertx.java.core.buffer.Buffer, x$3: org.vertx.java.core.Handler[org.vertx.java.core.AsyncResult[Void]]): org.vertx.java.core.file.FileSystem = ???
  override def writeFileSync(x$1: String, x$2: org.vertx.java.core.buffer.Buffer): org.vertx.java.core.file.FileSystem = ???
}