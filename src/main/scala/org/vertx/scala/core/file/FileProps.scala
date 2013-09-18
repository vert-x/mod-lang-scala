package org.vertx.scala.core.file

import org.vertx.java.core.file.{ FileProps => JFileProps }
import org.vertx.scala.VertxWrapper
import java.util.Date

/**
 * Represents properties of a file on the file system<p>
 * Instances of FileProps are thread-safe<p>
 *
 * @author <a href="http://tfox.org">Tim Fox</a>
 * @author <a href="http://www.campudus.com/">Joern Bernhardt</a>
 */
class FileProps(protected[this] val internal: JFileProps) extends VertxWrapper {
  override type InternalType = JFileProps

  /**
   * The date the file was created
   */
  def creationTime(): Date = internal.creationTime()

  /**
   * The date the file was last accessed
   */
  def lastAccessTime(): Date = internal.lastAccessTime()

  /**
   * The date the file was last modified
   */
  def lastModifiedTime(): Date = internal.lastModifiedTime()

  /**
   * Is the file a directory?
   */
  def isDirectory(): Boolean = internal.isDirectory()

  /**
   * Is the file some other type? (I.e. not a directory, regular file or symbolic link)
   */
  def isOther(): Boolean = internal.isOther()

  /**
   * Is the file a regular file?
   */
  def isRegularFile(): Boolean = internal.isRegularFile()

  /**
   * Is the file a symbolic link?
   */
  def isSymbolicLink(): Boolean = internal.isSymbolicLink()

  /**
   * The size of the file, in bytes
   */
  def size(): Long = internal.size()
}

/** Factory for [[file.FileProps]] instances. */
object FileProps {
  def apply(internal: JFileProps) = new FileProps(internal)
}