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

import org.vertx.java.core.buffer.{ Buffer => JBuffer }
import org.vertx.java.core.file.{ FileSystem => JFileSystem }
import org.vertx.scala.{Self, AsJava}
import org.vertx.scala.core.AsyncResult
import org.vertx.scala.core.FunctionConverters.{ asyncResultConverter, convertFunctionToParameterisedAsyncHandler }
import org.vertx.scala.core.buffer.Buffer

/**
 * Contains a broad set of operations for manipulating files.<p>
 * An asynchronous and a synchronous version of each operation is provided.<p>
 * The asynchronous versions take a handler which is called when the operation completes or an error occurs.<p>
 * The synchronous versions return the results, or throw exceptions directly.<p>
 * It is highly recommended the asynchronous versions are used unless you are sure the operation
 * will not block for a significant period of time.<p>
 * Instances of FileSystem are thread-safe.<p>
 *
 * @author <a href="http://tfox.org">Tim Fox</a>
 * @author swilliams
 * @author <a href="http://www.campudus.com/">Joern Bernhardt</a>
 * @author Galder Zamarreño
 */
final class FileSystem private[scala] (val asJava: JFileSystem) extends AnyVal
  with Self[FileSystem]
  with AsJava {

  override type J = JFileSystem
  override protected[this] def self: FileSystem = this

  /**
   * Copy a file from the path {@code from} to path {@code to}, asynchronously.<p>
   * The copy will fail if the destination already exists.<p>
   */
  def copy(from: String, to: String, handler: AsyncResult[Void] => Unit): FileSystem =
    wrap(asJava.copy(from, to, handler))

  /**
   * Synchronous version of {@link #copy(String, String, Handler)}
   */
  def copySync(from: String, to: String): FileSystem =
    wrap(asJava.copySync(from, to))

  /**
   * Copy a file from the path {@code from} to path {@code to}, asynchronously.<p>
   * If {@code recursive} is {@code true} and {@code from} represents a directory, then the directory and its contents
   * will be copied recursively to the destination {@code to}.<p>
   * The copy will fail if the destination if the destination already exists.<p>
   */
  def copy(from: String, to: String, recursive: Boolean, handler: AsyncResult[Void] => Unit): FileSystem =
    wrap(asJava.copy(from, to, recursive, handler))

  /**
   * Synchronous version of {@link #copy(String, String, boolean, Handler)}
   */
  def copySync(from: String, to: String, recursive: Boolean): FileSystem =
    wrap(asJava.copySync(from, to, recursive))

  /**
   * Move a file from the path {@code from} to path {@code to}, asynchronously.<p>
   * The move will fail if the destination already exists.<p>
   */
  def move(from: String, to: String, handler: AsyncResult[Void] => Unit): FileSystem =
    wrap(asJava.move(from, to, handler))

  /**
   * Synchronous version of {@link #move(String, String, Handler)}
   */
  def moveSync(from: String, to: String): FileSystem =
    wrap(asJava.moveSync(from, to))

  /**
   * Truncate the file represented by {@code path} to length {@code len} in bytes, asynchronously.<p>
   * The operation will fail if the file does not exist or {@code len} is less than {@code zero}.
   */
  def truncate(path: String, len: Long, handler: AsyncResult[Void] => Unit): FileSystem =
    wrap(asJava.truncate(path, len, handler))

  /**
   * Synchronous version of {@link #truncate(String, long, Handler)}
   */
  def truncateSync(path: String, len: Long): FileSystem =
    wrap(asJava.truncateSync(path, len))

  /**
   * Change the permissions on the file represented by {@code path} to {@code perms}, asynchronously.
   * The permission String takes the form rwxr-x--- as
   * specified <a href="http://download.oracle.com/javase/7/docs/api/java/nio/file/attribute/PosixFilePermissions.html">here</a>.<p>
   */
  def chmod(path: String, perms: String, handler: AsyncResult[Void] => Unit): FileSystem =
    wrap(asJava.chmod(path, perms, handler))

  /**
   * Synchronous version of {@link #chmod(String, String, Handler) }
   */
  def chmodSync(path: String, perms: String): FileSystem =
    wrap(asJava.chmodSync(path, perms))

  /**
   * Change the permissions on the file represented by {@code path} to {@code perms}, asynchronously.
   * The permission String takes the form rwxr-x--- as
   * specified in {<a href="http://download.oracle.com/javase/7/docs/api/java/nio/file/attribute/PosixFilePermissions.html">here</a>}.<p>
   * If the file is directory then all contents will also have their permissions changed recursively. Any directory permissions will
   * be set to {@code dirPerms}, whilst any normal file permissions will be set to {@code perms}.<p>
   */
  def chmod(path: String, perms: String, dirPerms: String, handler: AsyncResult[Void] => Unit): FileSystem =
    wrap(asJava.chmod(path, perms, dirPerms, handler))

  /**
   * Synchronous version of {@link #chmod(String, String, String, Handler)}
   */
  def chmodSync(path: String, perms: String, dirPerms: String): FileSystem =
    wrap(asJava.chmodSync(path, perms, dirPerms))

  /**
   * Obtain properties for the file represented by {@code path}, asynchronously.
   * If the file is a link, the link will be followed.
   */
  def props(path: String, handler: AsyncResult[FileProps] => Unit): FileSystem =
    wrap(asJava.props(path, arFilePropsConverter(handler)))

  /**
   * Synchronous version of {@link #props(String, Handler)}
   */
  def propsSync(path: String): FileProps =
    FileProps(asJava.propsSync(path))

  /**
   * Obtain properties for the link represented by {@code path}, asynchronously.
   * The link will not be followed.
   */
  def lprops(path: String, handler: AsyncResult[FileProps] => Unit): FileSystem =
    wrap(asJava.lprops(path, arFilePropsConverter(handler)))

  /**
   * Synchronous version of {@link #lprops(String, Handler)}
   */
  def lpropsSync(path: String): FileProps =
    FileProps(asJava.lpropsSync(path))

  /**
   * Create a hard link on the file system from {@code link} to {@code existing}, asynchronously.
   */
  def link(link: String, existing: String, handler: AsyncResult[Void] => Unit): FileSystem =
    wrap(asJava.link(link, existing, handler))

  /**
   * Synchronous version of {@link #link(String, String, Handler)}
   */
  def linkSync(link: String, existing: String): FileSystem =
    wrap(asJava.linkSync(link, existing))

  /**
   * Create a symbolic link on the file system from {@code link} to {@code existing}, asynchronously.
   */
  def symlink(link: String, existing: String, handler: AsyncResult[Void] => Unit): FileSystem =
    wrap(asJava.symlink(link, existing, handler))

  /**
   * Synchronous version of {@link #link(String, String, Handler)}
   */
  def symlinkSync(link: String, existing: String): FileSystem =
    wrap(asJava.symlinkSync(link, existing))

  /**
   * Unlinks the link on the file system represented by the path {@code link}, asynchronously.
   */
  def unlink(link: String, handler: AsyncResult[Void] => Unit): FileSystem =
    wrap(asJava.unlink(link, handler))

  /**
   * Synchronous version of {@link #unlink(String, Handler)}
   */
  def unlinkSync(link: String): FileSystem =
    wrap(asJava.unlinkSync(link))

  /**
   * Returns the path representing the file that the symbolic link specified by {@code link} points to, asynchronously.
   */
  def readSymlink(link: String, handler: AsyncResult[String] => Unit): FileSystem =
    wrap(asJava.readSymlink(link, handler))

  /**
   * Synchronous version of {@link #readSymlink(String, Handler)}
   */
  def readSymlinkSync(link: String): String =
    asJava.readSymlinkSync(link)

  /**
   * Deletes the file represented by the specified {@code path}, asynchronously.
   */
  def delete(path: String, handler: AsyncResult[Void] => Unit): FileSystem =
    wrap(asJava.delete(path, handler))

  /**
   * Synchronous version of {@link #delete(String, Handler)}
   */
  def deleteSync(path: String): FileSystem =
    wrap(asJava.deleteSync(path))

  /**
   * Deletes the file represented by the specified {@code path}, asynchronously.<p>
   * If the path represents a directory and {@code recursive = true} then the directory and its contents will be
   * deleted recursively.
   */
  def delete(path: String, recursive: Boolean, handler: AsyncResult[Void] => Unit): FileSystem =
    wrap(asJava.delete(path, recursive, handler))

  /**
   * Synchronous version of {@link #delete(String, boolean, Handler)}
   */
  def deleteSync(path: String, recursive: Boolean): FileSystem =
    wrap(asJava.deleteSync(path, recursive))

  /**
   * Create the directory represented by {@code path}, asynchronously.<p>
   * The operation will fail if the directory already exists.
   */
  def mkdir(path: String, handler: AsyncResult[Void] => Unit): FileSystem =
    wrap(asJava.mkdir(path, handler))

  /**
   * Synchronous version of {@link #mkdir(String, Handler)}
   */
  def mkdirSync(path: String): FileSystem =
    wrap(asJava.mkdirSync(path))

  /**
   * Create the directory represented by {@code path}, asynchronously.<p>
   * If {@code createParents} is set to {@code true} then any non-existent parent directories of the directory
   * will also be created.<p>
   * The operation will fail if the directory already exists.
   */
  def mkdir(path: String, createParents: Boolean, handler: AsyncResult[Void] => Unit): FileSystem =
    wrap(asJava.mkdir(path, createParents, handler))

  /**
   * Synchronous version of {@link #mkdir(String, boolean, Handler)}
   */
  def mkdirSync(path: String, createParents: Boolean): FileSystem =
    wrap(asJava.mkdirSync(path, createParents))

  /**
   * Create the directory represented by {@code path}, asynchronously.<p>
   * The new directory will be created with permissions as specified by {@code perms}.
   * The permission String takes the form rwxr-x--- as specified
   * in <a href="http://download.oracle.com/javase/7/docs/api/java/nio/file/attribute/PosixFilePermissions.html">here</a>.<p>
   * The operation will fail if the directory already exists.
   */
  def mkdir(path: String, perms: String, handler: AsyncResult[Void] => Unit): FileSystem =
    wrap(asJava.mkdir(path, perms, handler))

  /**
   * Synchronous version of {@link #mkdir(String, String, Handler)}
   */
  def mkdirSync(path: String, perms: String): FileSystem =
    wrap(asJava.mkdirSync(path, perms))

  /**
   * Create the directory represented by {@code path}, asynchronously.<p>
   * The new directory will be created with permissions as specified by {@code perms}.
   * The permission String takes the form rwxr-x--- as specified
   * in <a href="http://download.oracle.com/javase/7/docs/api/java/nio/file/attribute/PosixFilePermissions.html">here</a>.<p>
   * If {@code createParents} is set to {@code true} then any non-existent parent directories of the directory
   * will also be created.<p>
   * The operation will fail if the directory already exists.<p>
   */
  def mkdir(path: String, perms: String, createParents: Boolean, handler: AsyncResult[Void] => Unit): FileSystem =
    wrap(asJava.mkdir(path, perms, createParents, handler))

  /**
   * Synchronous version of {@link #mkdir(String, String, boolean, Handler)}
   */
  def mkdirSync(path: String, perms: String, createParents: Boolean): FileSystem =
    wrap(asJava.mkdirSync(path, perms, createParents))

  /**
   * Read the contents of the directory specified by {@code path}, asynchronously.<p>
   * The result is an array of String representing the paths of the files inside the directory.
   */
  def readDir(path: String, handler: AsyncResult[Array[String]] => Unit): FileSystem =
    wrap(asJava.readDir(path, handler))

  /**
   * Synchronous version of {@link #readDir(String, Handler)}
   */
  def readDirSync(path: String): Array[String] = asJava.readDirSync(path)

  /**
   * Read the contents of the directory specified by {@code path}, asynchronously.<p>
   * The parameter {@code filter} is a regular expression. If {@code filter} is specified then only the paths that
   * match  @{filter}will be returned.<p>
   * The result is an array of String representing the paths of the files inside the directory.
   */
  def readDir(path: String, filter: String, handler: AsyncResult[Array[String]] => Unit): FileSystem =
    wrap(asJava.readDir(path, filter, handler))

  /**
   * Synchronous version of {@link #readDir(String, String, Handler)}
   */
  def readDirSync(path: String, filter: String): Array[String] = asJava.readDirSync(path, filter)

  /**
   * Reads the entire file as represented by the path {@code path} as a {@link Buffer}, asynchronously.<p>
   * Do not user this method to read very large files or you risk running out of available RAM.
   */
  def readFile(path: String, handler: AsyncResult[Buffer] => Unit): FileSystem =
    wrap(asJava.readFile(path, arBufferConverter(handler)))

  /**
   * Synchronous version of {@link #readFile(String, Handler)}
   */
  def readFileSync(path: String): Buffer = Buffer(asJava.readFileSync(path))

  /**
   * Creates the file, and writes the specified {@code Buffer data} to the file represented by the path {@code path},
   * asynchronously.
   */
  def writeFile(path: String, data: Buffer, handler: AsyncResult[Void] => Unit): FileSystem =
    wrap(asJava.writeFile(path, data.asJava, handler))

  /**
   * Synchronous version of {@link #writeFile(String, Buffer, Handler)}
   */
  def writeFileSync(path: String, data: Buffer): FileSystem =
    wrap(asJava.writeFileSync(path, data.asJava))

  /**
   * Open the file represented by {@code path}, asynchronously.<p>
   * The file is opened for both reading and writing. If the file does not already exist it will be created.
   * Write operations will not automatically flush to storage.
   */
  def open(path: String, handler: AsyncResult[AsyncFile] => Unit): FileSystem =
    wrap(asJava.open(path, arAsyncFileConverter(handler)))

  /**
   * Synchronous version of {@link #open(String, Handler)}
   */
  def openSync(path: String): AsyncFile =
    AsyncFile(asJava.openSync(path))

  /**
   * Open the file represented by {@code path}, asynchronously.<p>
   * The file is opened for both reading and writing. If the file does not already exist it will be created with the
   * permissions as specified by {@code perms}.
   * Write operations will not automatically flush to storage.
   */
  def open(path: String, perms: String, handler: AsyncResult[AsyncFile] => Unit): FileSystem =
    wrap(asJava.open(path, perms, arAsyncFileConverter(handler)))

  /**
   * Synchronous version of {@link #open(String, String, Handler)}
   */
  def openSync(path: String, perms: String): AsyncFile =
    AsyncFile(asJava.openSync(path, perms))

  /**
   * Open the file represented by {@code path}, asynchronously.<p>
   * The file is opened for both reading and writing. If the file does not already exist and
   * {@code createNew} is {@code true} it will be created with the permissions as specified by {@code perms}, otherwise
   * the operation will fail.
   * Write operations will not automatically flush to storage.
   */
  def open(path: String, perms: String, createNew: Boolean, handler: AsyncResult[AsyncFile] => Unit): FileSystem =
    wrap(asJava.open(path, perms, createNew, arAsyncFileConverter(handler)))

  /**
   * Synchronous version of {@link #open(String, String, boolean, Handler)}
   */
  def openSync(path: String, perms: String, createNew: Boolean): AsyncFile =
    AsyncFile(asJava.openSync(path, perms, createNew))

  /**
   * Open the file represented by {@code path}, asynchronously.<p>
   * If {@code read} is {@code true} the file will be opened for reading. If {@code write} is {@code true} the file
   * will be opened for writing.<p>
   * If the file does not already exist and
   * {@code createNew} is {@code true} it will be created with the permissions as specified by {@code perms}, otherwise
   * the operation will fail.<p>
   * Write operations will not automatically flush to storage.
   */
  def open(path: String, perms: String, read: Boolean, write: Boolean, createNew: Boolean, handler: AsyncResult[AsyncFile] => Unit): FileSystem =
    wrap(asJava.open(path, perms, read, write, createNew, arAsyncFileConverter(handler)))

  /**
   * Synchronous version of {@link #open(String, String, boolean, boolean, boolean, Handler)}
   */
  def openSync(path: String, perms: String, read: Boolean, write: Boolean, createNew: Boolean): AsyncFile =
    AsyncFile(asJava.openSync(path, perms, read, write, createNew))

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
  def open(path: String, perms: String, read: Boolean, write: Boolean, createNew: Boolean, flush: Boolean, handler: AsyncResult[AsyncFile] => Unit): FileSystem =
    wrap(asJava.open(path, perms, read, write, createNew, flush, arAsyncFileConverter(handler)))

  /**
   * Synchronous version of {@link #open(String, String, boolean, boolean, boolean, boolean, Handler)}
   */
  def openSync(path: String, perms: String, read: Boolean, write: Boolean, createNew: Boolean, flush: Boolean): AsyncFile =
    AsyncFile(asJava.openSync(path, perms, read, write, createNew, flush))

  /**
   * Creates an empty file with the specified {@code path}, asynchronously.
   */
  def createFile(path: String, handler: AsyncResult[Void] => Unit): FileSystem =
    wrap(asJava.createFile(path, handler))

  /**
   * Synchronous version of {@link #createFile(String, Handler)}
   */
  def createFileSync(path: String): FileSystem =
    wrap(asJava.createFileSync(path))

  /**
   * Creates an empty file with the specified {@code path} and permissions {@code perms}, asynchronously.
   */
  def createFile(path: String, perms: String, handler: AsyncResult[Void] => Unit): FileSystem =
    wrap(asJava.createFile(path, perms, handler))

  /**
   * Synchronous version of {@link #createFile(String, String, Handler)}
   */
  def createFileSync(path: String, perms: String): FileSystem =
    wrap(asJava.createFileSync(path, perms))

  /**
   * Determines whether the file as specified by the path {@code path} exists, asynchronously.
   */
  def exists(path: String, handler: AsyncResult[Boolean] => Unit): FileSystem =
    wrap(asJava.exists(path, asyncResultConverter((x: java.lang.Boolean) => x.booleanValue)(handler)))

  /**
   * Synchronous version of {@link #exists(String, Handler)}
   */
  def existsSync(path: String): Boolean = asJava.existsSync(path)

  /**
   * Returns properties of the file-system being used by the specified {@code path}, asynchronously.
   */
  def fsProps(path: String, handler: AsyncResult[FileSystemProps] => Unit): FileSystem =
    wrap(asJava.fsProps(path, arFileSystemPropsConverter(handler)))

  /**
   * Synchronous version of {@link #fsProps(String, Handler)}
   */
  def fsPropsSync(path: String): FileSystemProps = FileSystemProps(asJava.fsPropsSync(path))

  private def arFilePropsConverter(handler: AsyncResult[FileProps] => Unit) =
    asyncResultConverter(FileProps.apply)(handler)

  private def arFileSystemPropsConverter(handler: AsyncResult[FileSystemProps] => Unit) =
    asyncResultConverter(FileSystemProps.apply)(handler)

  private def arAsyncFileConverter(handler: AsyncResult[AsyncFile] => Unit) =
    asyncResultConverter(AsyncFile.apply)(handler)

  private def arBufferConverter(handler: AsyncResult[Buffer] => Unit) =
    asyncResultConverter({ jbuf: JBuffer => Buffer(jbuf) })(handler)

}

/** Factory for [[org.vertx.scala.core.file.FileSystem]] instances. */
object FileSystem {
  def apply(internal: JFileSystem) = new FileSystem(internal)
}