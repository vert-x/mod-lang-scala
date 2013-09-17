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

import org.vertx.java.core.buffer.Buffer
import org.vertx.java.core.file.{ FileSystem => JFileSystem }
import org.vertx.scala.core.{ Handler, AsyncResult }
import org.vertx.scala.core.FunctionConverters._
import org.vertx.scala.VertxWrapper

/**
 * @author swilliams
 * @author <a href="http://www.campudus.com/">Joern Bernhardt</a>
 */
class FileSystem(protected[this] val internal: JFileSystem) extends JFileSystem with VertxWrapper {
  override type InternalType = JFileSystem

  /**
   * Copy a file from the path {@code from} to path {@code to}, asynchronously.<p>
   * The copy will fail if the destination already exists.<p>
   */
  override def copy(from: String, to: String, handler: AsyncResult[Void] => Unit): FileSystem =
    wrap(internal.copy(from, to, handler))

  /**
   * Synchronous version of {@link #copy(String, String, Handler)}
   */
  override def copySync(from: String, to: String): FileSystem =
    wrap(internal.copySync(from, to))

  /**
   * Copy a file from the path {@code from} to path {@code to}, asynchronously.<p>
   * If {@code recursive} is {@code true} and {@code from} represents a directory, then the directory and its contents
   * will be copied recursively to the destination {@code to}.<p>
   * The copy will fail if the destination if the destination already exists.<p>
   */
  override def copy(from: String, to: String, recursive: Boolean, handler: Handler[AsyncResult[Void]]): FileSystem =
    wrap(internal.copy(from, to, recursive, handler))

  /**
   * Synchronous version of {@link #copy(String, String, boolean, Handler)}
   */
  override def copySync(from: String, to: String, recursive: Boolean): FileSystem =
    wrap(internal.copySync(from, to, recursive))

  /**
   * Move a file from the path {@code from} to path {@code to}, asynchronously.<p>
   * The move will fail if the destination already exists.<p>
   */
  override def move(from: String, to: String, handler: AsyncResult[Void] => Unit): FileSystem =
    wrap(internal.move(from, to, handler))

  /**
   * Synchronous version of {@link #move(String, String, Handler)}
   */
  override def moveSync(from: String, to: String): FileSystem =
    wrap(internal.moveSync(from, to))

  /**
   * Truncate the file represented by {@code path} to length {@code len} in bytes, asynchronously.<p>
   * The operation will fail if the file does not exist or {@code len} is less than {@code zero}.
   */
  override def truncate(path: String, len: Long, handler: AsyncResult[Void] => Unit): FileSystem =
    wrap(internal.truncate(path, len, handler))

  /**
   * Synchronous version of {@link #truncate(String, long, Handler)}
   */
  override def truncateSync(path: String, len: Long): FileSystem =
    wrap(internal.truncateSync(path, len))

  /**
   * Change the permissions on the file represented by {@code path} to {@code perms}, asynchronously.
   * The permission String takes the form rwxr-x--- as
   * specified <a href="http://download.oracle.com/javase/7/docs/api/java/nio/file/attribute/PosixFilePermissions.html">here</a>.<p>
   */
  override def chmod(path: String, perms: String, handler: AsyncResult[Void] => Unit): FileSystem =
    wrap(internal.chmod(path, perms, handler))

  /**
   * Synchronous version of {@link #chmod(String, String, Handler) }
   */
  override def chmodSync(path: String, perms: String): FileSystem =
    wrap(internal.chmodSync(path, perms))

  /**
   * Change the permissions on the file represented by {@code path} to {@code perms}, asynchronously.
   * The permission String takes the form rwxr-x--- as
   * specified in {<a href="http://download.oracle.com/javase/7/docs/api/java/nio/file/attribute/PosixFilePermissions.html">here</a>}.<p>
   * If the file is directory then all contents will also have their permissions changed recursively. Any directory permissions will
   * be set to {@code dirPerms}, whilst any normal file permissions will be set to {@code perms}.<p>
   */
  override def chmod(path: String, perms: String, dirPerms: String, handler: AsyncResult[Void] => Unit): FileSystem =
    wrap(internal.chmod(path, perms, dirPerms, handler))

  /**
   * Synchronous version of {@link #chmod(String, String, String, Handler)}
   */
  override def chmodSync(path: String, perms: String, dirPerms: String): FileSystem =
    wrap(internal.chmodSync(path, perms, dirPerms))

  /**
   * Obtain properties for the file represented by {@code path}, asynchronously.
   * If the file is a link, the link will be followed.
   */
  override def props(path: String, handler: AsyncResult[FileProps] => Unit): FileSystem =
    wrap(internal.props(path, handler))

  /**
   * Synchronous version of {@link #props(String, Handler)}
   */
  override def propsSync(path: String): FileProps =
    FileProps(internal.propsSync(path))

  /**
   * Obtain properties for the link represented by {@code path}, asynchronously.
   * The link will not be followed.
   */
  override def lprops(path: String, handler: AsyncResult[FileProps] => Unit): FileSystem =
    wrap(internal.lprops(path, handler))

  /**
   * Synchronous version of {@link #lprops(String, Handler)}
   */
  override def lpropsSync(path: String): FileProps =
    FileProps(internal.lpropsSync(path))

  /**
   * Create a hard link on the file system from {@code link} to {@code existing}, asynchronously.
   */
  override def link(link: String, existing: String, handler: AsyncResult[Void] => Unit): FileSystem =
    wrap(internal.link(link, existing, handler))

  /**
   * Synchronous version of {@link #link(String, String, Handler)}
   */
  override def linkSync(link: String, existing: String): FileSystem =
    wrap(internal.linkSync(link, existing))

  /**
   * Create a symbolic link on the file system from {@code link} to {@code existing}, asynchronously.
   */
  override def symlink(link: String, existing: String, handler: AsyncResult[Void] => Unit): FileSystem =
    wrap(internal.symlink(link, existing, handler))

  /**
   * Synchronous version of {@link #link(String, String, Handler)}
   */
  override def symlinkSync(link: String, existing: String): FileSystem =
    wrap(internal.symlinkSync(link, existing))

  /**
   * Unlinks the link on the file system represented by the path {@code link}, asynchronously.
   */
  override def unlink(link: String, handler: AsyncResult[Void] => Unit): FileSystem =
    wrap(internal.unlink(link, handler))

  /**
   * Synchronous version of {@link #unlink(String, Handler)}
   */
  override def unlinkSync(link: String): FileSystem =
    wrap(internal.unlinkSync(link))

  /**
   * Returns the path representing the file that the symbolic link specified by {@code link} points to, asynchronously.
   */
  override def readSymlink(link: String, handler: AsyncResult[String] => Unit): FileSystem =
    wrap(internal.readSymlink(link, handler))

  /**
   * Synchronous version of {@link #readSymlink(String, Handler)}
   */
  override def readSymlinkSync(link: String): String =
    internal.readSymlinkSync(link)

  /**
   * Deletes the file represented by the specified {@code path}, asynchronously.
   */
  override def delete(path: String, handler: Handler[AsyncResult[Void]]): FileSystem =
    wrap(internal.delete(path, handler))

  /**
   * Synchronous version of {@link #delete(String, Handler)}
   */
  override def deleteSync(path: String): FileSystem =
    wrap(internal.deleteSync(path))

  /**
   * Deletes the file represented by the specified {@code path}, asynchronously.<p>
   * If the path represents a directory and {@code recursive = true} then the directory and its contents will be
   * deleted recursively.
   */
  override def delete(path: String, recursive: Boolean, handler: AsyncResult[Void] => Unit): FileSystem =
    wrap(internal.delete(path, recursive, handler))

  /**
   * Synchronous version of {@link #delete(String, boolean, Handler)}
   */
  override def deleteSync(path: String, recursive: Boolean): FileSystem =
    wrap(internal.deleteSync(path, recursive))

  /**
   * Create the directory represented by {@code path}, asynchronously.<p>
   * The operation will fail if the directory already exists.
   */
  override def mkdir(path: String, handler: Handler[AsyncResult[Void]]): FileSystem =
    wrap(internal.mkdir(path, handler))

  /**
   * Synchronous version of {@link #mkdir(String, Handler)}
   */
  override def mkdirSync(path: String): FileSystem =
    wrap(internal.mkdirSync(path))

  /**
   * Create the directory represented by {@code path}, asynchronously.<p>
   * If {@code createParents} is set to {@code true} then any non-existent parent directories of the directory
   * will also be created.<p>
   * The operation will fail if the directory already exists.
   */
  override def mkdir(path: String, createParents: Boolean, handler: AsyncResult[Void] => Unit): FileSystem =
    wrap(internal.mkdir(path, createParents, handler))

  /**
   * Synchronous version of {@link #mkdir(String, boolean, Handler)}
   */
  override def mkdirSync(path: String, createParents: Boolean): FileSystem =
    wrap(internal.mkdirSync(path, createParents))

  /**
   * Create the directory represented by {@code path}, asynchronously.<p>
   * The new directory will be created with permissions as specified by {@code perms}.
   * The permission String takes the form rwxr-x--- as specified
   * in <a href="http://download.oracle.com/javase/7/docs/api/java/nio/file/attribute/PosixFilePermissions.html">here</a>.<p>
   * The operation will fail if the directory already exists.
   */
  override def mkdir(path: String, perms: String, handler: AsyncResult[Void] => Unit): FileSystem =
    wrap(internal.mkdir(path, perms, handler))

  /**
   * Synchronous version of {@link #mkdir(String, String, Handler)}
   */
  override def mkdirSync(path: String, perms: String): FileSystem =
    wrap(internal.mkdirSync(path, perms))

  /**
   * Create the directory represented by {@code path}, asynchronously.<p>
   * The new directory will be created with permissions as specified by {@code perms}.
   * The permission String takes the form rwxr-x--- as specified
   * in <a href="http://download.oracle.com/javase/7/docs/api/java/nio/file/attribute/PosixFilePermissions.html">here</a>.<p>
   * If {@code createParents} is set to {@code true} then any non-existent parent directories of the directory
   * will also be created.<p>
   * The operation will fail if the directory already exists.<p>
   */
  override def mkdir(path: String, perms: String, createParents: Boolean, handler: AsyncResult[Void] => Unit): FileSystem =
    wrap(internal.mkdir(path, perms, createParents, handler))

  /**
   * Synchronous version of {@link #mkdir(String, String, boolean, Handler)}
   */
  override def mkdirSync(path: String, perms: String, createParents: Boolean): FileSystem =
    wrap(internal.mkdirSync(path, perms, createParents))

  /**
   * Read the contents of the directory specified by {@code path}, asynchronously.<p>
   * The result is an array of String representing the paths of the files inside the directory.
   */
  override def readDir(path: String, handler: AsyncResult[Array[String]] => Unit): FileSystem =
    wrap(internal.readDir(path, handler))

  /**
   * Synchronous version of {@link #readDir(String, Handler)}
   */
  override def readDirSync(path: String): Array[String] = internal.readDirSync(path)

  /**
   * Read the contents of the directory specified by {@code path}, asynchronously.<p>
   * The parameter {@code filter} is a regular expression. If {@code filter} is specified then only the paths that
   * match  @{filter}will be returned.<p>
   * The result is an array of String representing the paths of the files inside the directory.
   */
  override def readDir(path: String, filter: String, handler: AsyncResult[Array[String]] => Unit): FileSystem =
    wrap(internal.readDir(path, filter, handler))

  /**
   * Synchronous version of {@link #readDir(String, String, Handler)}
   */
  override def readDirSync(path: String, filter: String): Array[String] = internal.readDirSync(path, filter)

  /**
   * Reads the entire file as represented by the path {@code path} as a {@link Buffer}, asynchronously.<p>
   * Do not user this method to read very large files or you risk running out of available RAM.
   */
  override def readFile(path: String, handler: AsyncResult[org.vertx.java.core.buffer.Buffer] => Unit): FileSystem =
    wrap(internal.readFile(path, handler))

  /**
   * Synchronous version of {@link #readFile(String, Handler)}
   */
  override def readFileSync(path: String): org.vertx.java.core.buffer.Buffer = internal.readFileSync(path)

  /**
   * Creates the file, and writes the specified {@code Buffer data} to the file represented by the path {@code path},
   * asynchronously.
   */
  override def writeFile(path: String, data: org.vertx.java.core.buffer.Buffer, handler: AsyncResult[Void] => Unit): FileSystem =
    wrap(internal.writeFile(path, data, handler))

  /**
   * Synchronous version of {@link #writeFile(String, Buffer, Handler)}
   */
  override def writeFileSync(path: String, data: org.vertx.java.core.buffer.Buffer): FileSystem =
    wrap(internal.writeFileSync(path, data))

  /**
   * Open the file represented by {@code path}, asynchronously.<p>
   * The file is opened for both reading and writing. If the file does not already exist it will be created.
   * Write operations will not automatically flush to storage.
   */
  override def open(path: String, handler: AsyncResult[AsyncFile] => Unit): FileSystem =
    wrap(internal.open(path, handler))

  /**
   * Synchronous version of {@link #open(String, Handler)}
   */
  override def openSync(path: String): AsyncFile =
    AsyncFile(internal.openSync(path))

  /**
   * Open the file represented by {@code path}, asynchronously.<p>
   * The file is opened for both reading and writing. If the file does not already exist it will be created with the
   * permissions as specified by {@code perms}.
   * Write operations will not automatically flush to storage.
   */
  override def open(path: String, perms: String, handler: AsyncResult[AsyncFile] => Unit): FileSystem =
    wrap(internal.open(path, perms, handler))

  /**
   * Synchronous version of {@link #open(String, String, Handler)}
   */
  override def openSync(path: String, perms: String): AsyncFile =
    AsyncFile(internal.openSync(path, perms))

  /**
   * Open the file represented by {@code path}, asynchronously.<p>
   * The file is opened for both reading and writing. If the file does not already exist and
   * {@code createNew} is {@code true} it will be created with the permissions as specified by {@code perms}, otherwise
   * the operation will fail.
   * Write operations will not automatically flush to storage.
   */
  override def open(path: String, perms: String, createNew: Boolean, handler: Handler[AsyncResult[AsyncFile]]): FileSystem =
    wrap(internal.open(path, perms, createNew, handler))

  /**
   * Synchronous version of {@link #open(String, String, boolean, Handler)}
   */
  override def openSync(path: String, perms: String, createNew: Boolean): AsyncFile =
    AsyncFile(internal.openSync(path, perms, createNew))

  /**
   * Open the file represented by {@code path}, asynchronously.<p>
   * If {@code read} is {@code true} the file will be opened for reading. If {@code write} is {@code true} the file
   * will be opened for writing.<p>
   * If the file does not already exist and
   * {@code createNew} is {@code true} it will be created with the permissions as specified by {@code perms}, otherwise
   * the operation will fail.<p>
   * Write operations will not automatically flush to storage.
   */
  override def open(path: String, perms: String, read: Boolean, write: Boolean, createNew: Boolean, handler: AsyncResult[AsyncFile] => Unit): FileSystem =
    wrap(internal.open(path, perms, read, write, createNew, handler))

  /**
   * Synchronous version of {@link #open(String, String, boolean, boolean, boolean, Handler)}
   */
  override def openSync(path: String, perms: String, read: Boolean, write: Boolean, createNew: Boolean): AsyncFile =
    AsyncFile(internal.openSync(path, perms, read, write, createNew))

  /**
   * Open the file represented by {@code path}, asynchronously.<p>
   * If {@code read} is {@code true} the file will be opened for reading. If {@code write} is {@code true} the file
   * will be opened for writing.<p>
   * If the file does not already exist and
   * {@code createNew} is {@code true} it will be created with the permissions as specified by {@code perms}, otherwise
   * the operation will fail.<p>
   * If {@code flush} is {@code true} then all writes will be automatically flushed through OS buffers to the underlying
   * storage on each write.
   */
  override def open(path: String, perms: String, read: Boolean, write: Boolean, createNew: Boolean, flush: Boolean, handler: Handler[AsyncResult[AsyncFile]]): FileSystem =
    wrap(internal.open(path, perms, read, write, createNew, flush, handler))

  /**
   * Synchronous version of {@link #open(String, String, boolean, boolean, boolean, boolean, Handler)}
   */
  override def openSync(path: String, perms: String, read: Boolean, write: Boolean, createNew: Boolean, flush: Boolean): AsyncFile =
    AsyncFile(internal.openSync(path, perms, read, write, createNew, flush))

  /**
   * Creates an empty file with the specified {@code path}, asynchronously.
   */
  override def createFile(path: String, handler: AsyncResult[Void] => Unit): FileSystem =
    wrap(internal.createFile(path, handler))

  /**
   * Synchronous version of {@link #createFile(String, Handler)}
   */
  override def createFileSync(path: String): FileSystem =
    wrap(internal.createFileSync(path))

  /**
   * Creates an empty file with the specified {@code path} and permissions {@code perms}, asynchronously.
   */
  override def createFile(path: String, perms: String, handler: Handler[AsyncResult[Void]]): FileSystem =
    wrap(internal.createFile(path, perms, handler))

  /**
   * Synchronous version of {@link #createFile(String, String, Handler)}
   */
  override def createFileSync(path: String, perms: String): FileSystem =
    wrap(internal.createFileSync(path, perms))

  /**
   * Determines whether the file as specified by the path {@code path} exists, asynchronously.
   */
  override def exists(path: String, handler: AsyncResult[java.lang.Boolean] => Unit): FileSystem =
    wrap(internal.exists(path, handler))

  /**
   * Synchronous version of {@link #exists(String, Handler)}
   */
  override def existsSync(path: String): Boolean =
    internal.existsSync(path)

  /**
   * Returns properties of the file-system being used by the specified {@code path}, asynchronously.
   */
  override def fsProps(path: String, handler: AsyncResult[FileSystemProps] => Unit): FileSystem =
    wrap(internal.fsProps(path, handler))

  /**
   * Synchronous version of {@link #fsProps(String, Handler)}
   */
  override def fsPropsSync(path: String): FileSystemProps =
    FileSystemProps(internal.fsPropsSync(path))

}

object FileSystem {
  def apply(internal: JFileSystem) = new FileSystem(internal)
}