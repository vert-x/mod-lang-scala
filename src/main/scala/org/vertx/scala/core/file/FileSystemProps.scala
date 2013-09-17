package org.vertx.scala.core.file

// FIXME java types
import org.vertx.java.core.file.{ FileSystemProps => JFileSystemProps }
import org.vertx.scala.VertxWrapper

/**
 * Represents properties of the file system.<p>
 * Instances of FileSystemProps are thread-safe.<p>
 *
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
class FileSystemProps(protected[this] val internal: JFileSystemProps) extends JFileSystemProps with VertxWrapper {
  override type InternalType = JFileSystemProps

  /**
   * The total space on the file system, in bytes
   */
  def totalSpace(): Long = internal.totalSpace()

  /**
   * The total un-allocated space on the file system, in bytes
   */
  def unallocatedSpace(): Long = internal.unallocatedSpace()

  /**
   * The total usable space on the file system, in bytes
   */
  def usableSpace(): Long = internal.usableSpace()

}

object FileSystemProps {
  def apply(internal: JFileSystemProps) = new FileSystemProps(internal)
}