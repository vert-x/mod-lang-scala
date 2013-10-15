package org.vertx.scala.core.buffer

import org.vertx.java.core.buffer.{ Buffer => JBuffer }
import org.vertx.scala.VertxWrapper
import io.netty.buffer.ByteBuf
import java.nio.ByteBuffer

/**
 * A Buffer represents a sequence of zero or more bytes that can be written to or read from, and which expands as
 * necessary to accommodate any bytes written to it.<p>
 * There are two ways to write data to a Buffer: The first method involves methods that take the form {@code setXXX}.
 * These methods write data into the buffer starting at the specified position. The position does not have to be inside data that
 * has already been written to the buffer; the buffer will automatically expand to encompass the position plus any data that needs
 * to be written. All positions are measured in bytes and start with zero.<p>
 * The second method involves methods that take the form {@code appendXXX}; these methods append data
 * at the end of the buffer.<p>
 * Methods exist to both {@code set} and {@code append} all primitive types, {@link java.lang.String}, {@link java.nio.ByteBuffer} and
 * other instances of Buffer.<p>
 * Data can be read from a buffer by invoking methods which take the form {@code getXXX}. These methods take a parameter
 * representing the position in the Buffer from where to read data.<p>
 * Once a buffer has been written to a socket or other write stream, the same buffer instance can't be written again to another WriteStream.<p>
 * Instances of this class are not thread-safe.<p>
 *
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
class Buffer(protected val internal: JBuffer) extends VertxWrapper {
  override type InternalType = JBuffer

  /**
   * Appends the specified {@code T} to the end of the Buffer. The buffer will expand as necessary
   * to accommodate any bytes written.<p>
   * Returns a reference to {@code this} so multiple operations can be appended together.
   *
   * @param value The value to append to the Buffer.
   * @return A reference to {@code this} so multiple operations can be appended together.
   */
  def append[T: BufferType](value: T): Buffer = wrap(implicitly[BufferType[T]].appendToBuffer(internal, value))

  /**
   * Returns a {@code String} representation of the Buffer assuming it contains a {@code String} encoding in UTF-8.
   * @return A string representation of the Buffer.
   */
  override def toString(): String = internal.toString()

  /**
   * Returns a {@code String} representation of the Buffer with the encoding specified by {@code enc}
   * @param enc The encoding to use to compute the String.
   * @return A string representation of the Buffer.
   */
  def toString(enc: String): String = internal.toString(enc)

  /**
   * Returns the {@code byte} at position {@code pos} in the Buffer.
   *
   * @throws IndexOutOfBoundsException if the specified {@code pos} is less than {@code 0} or {@code pos + 1} is greater than the length of the Buffer.
   */
  def getByte(pos: Int): Byte = internal.getByte(pos)

  /**
   * Returns the {@code int} at position {@code pos} in the Buffer.
   *
   * @throws IndexOutOfBoundsException if the specified {@code pos} is less than {@code 0} or {@code pos + 4} is greater than the length of the Buffer.
   */
  def getInt(pos: Int): Int = internal.getInt(pos)

  /**
   * Returns the {@code long} at position {@code pos} in the Buffer.
   *
   * @throws IndexOutOfBoundsException if the specified {@code pos} is less than {@code 0} or {@code pos + 8} is greater than the length of the Buffer.
   */
  def getLong(pos: Int): Long = internal.getLong(pos)

  /**
   * Returns the {@code double} at position {@code pos} in the Buffer.
   *
   * @throws IndexOutOfBoundsException if the specified {@code pos} is less than {@code 0} or {@code pos + 8} is greater than the length of the Buffer.
   */
  def getDouble(pos: Int): Double = internal.getDouble(pos)

  /**
   * Returns the {@code float} at position {@code pos} in the Buffer.
   *
   * @throws IndexOutOfBoundsException if the specified {@code pos} is less than {@code 0} or {@code pos + 4} is greater than the length of the Buffer.
   */
  def getFloat(pos: Int): Float = internal.getFloat(pos)

  /**
   * Returns the {@code short} at position {@code pos} in the Buffer.
   *
   * @throws IndexOutOfBoundsException if the specified {@code pos} is less than {@code 0} or {@code pos + 2} is greater than the length of the Buffer.
   */
  def getShort(pos: Int): Short = internal.getShort(pos)

  /**
   * Returns a copy of the entire Buffer as a {@code byte[]}
   */
  def getBytes(): Array[Byte] = internal.getBytes()

  /**
   * Returns a copy of a sub-sequence the Buffer as a {@code byte[]} starting at position {@code start}
   * and ending at position {@code end - 1}
   */
  def getBytes(start: Int, end: Int): Array[Byte] = internal.getBytes(start, end)

  /**
   * Returns a copy of a sub-sequence the Buffer as a {@link Buffer} starting at position {@code start}
   * and ending at position {@code end - 1}
   */
  def getBuffer(start: Int, end: Int): Buffer = wrap(internal.getBuffer(start, end))

  /**
   * Returns a copy of a sub-sequence the Buffer as a {@code String} starting at position {@code start}
   * and ending at position {@code end - 1} interpreted as a String in the specified encoding
   */
  def getString(start: Int, end: Int, enc: String): String = internal.getString(start, end, enc)

  /**
   * Returns a copy of a sub-sequence the Buffer as a {@code String} starting at position {@code start}
   * and ending at position {@code end - 1} interpreted as a String in UTF-8 encoding
   */
  def getString(start: Int, end: Int): String = internal.getString(start, end)

  /**
   * Sets the {@code byte} at position {@code pos} in the Buffer to the value {@code b}.<p>
   * The buffer will expand as necessary to accommodate any value written.
   */
  def setByte(pos: Int, b: Byte): Buffer = wrap(internal.setByte(pos, b))

  /**
   * Sets the {@code int} at position {@code pos} in the Buffer to the value {@code i}.<p>
   * The buffer will expand as necessary to accommodate any value written.
   */
  def setInt(pos: Int, i: Int): Buffer = wrap(internal.setInt(pos, i))

  /**
   * Sets the {@code long} at position {@code pos} in the Buffer to the value {@code l}.<p>
   * The buffer will expand as necessary to accommodate any value written.
   */
  def setLong(pos: Int, l: Long): Buffer = wrap(internal.setLong(pos, l))

  /**
   * Sets the {@code double} at position {@code pos} in the Buffer to the value {@code d}.<p>
   * The buffer will expand as necessary to accommodate any value written.
   */
  def setDouble(pos: Int, d: Double): Buffer = wrap(internal.setDouble(pos, d))

  /**
   * Sets the {@code float} at position {@code pos} in the Buffer to the value {@code f}.<p>
   * The buffer will expand as necessary to accommodate any value written.
   */
  def setFloat(pos: Int, f: Float): Buffer = wrap(internal.setFloat(pos, f))

  /**
   * Sets the {@code short} at position {@code pos} in the Buffer to the value {@code s}.<p>
   * The buffer will expand as necessary to accommodate any value written.
   */
  def setShort(pos: Int, s: Short): Buffer = wrap(internal.setShort(pos, s))

  /**
   * Sets the bytes at position {@code pos} in the Buffer to the bytes represented by the {@code Buffer b}.<p>
   * The buffer will expand as necessary to accommodate any value written.
   */
  def setBuffer(pos: Int, b: Buffer): Buffer = wrap(internal.setBuffer(pos, b.internal))

  /**
   * Sets the bytes at position {@code pos} in the Buffer to the bytes represented by the {@code ByteBuffer b}.<p>
   * The buffer will expand as necessary to accommodate any value written.
   */
  def setBytes(pos: Int, b: ByteBuffer): Buffer = wrap(internal.setBytes(pos, b))

  /**
   * Sets the bytes at position {@code pos} in the Buffer to the bytes represented by the {@code byte[] b}.<p>
   * The buffer will expand as necessary to accommodate any value written.
   */
  def setBytes(pos: Int, b: Array[Byte]): Buffer = wrap(internal.setBytes(pos, b))

  /**
   * Sets the bytes at position {@code pos} in the Buffer to the value of {@code str} encoded in UTF-8.<p>
   * The buffer will expand as necessary to accommodate any value written.
   */
  def setString(pos: Int, str: String): Buffer = wrap(internal.setString(pos, str))

  /**
   * Sets the bytes at position {@code pos} in the Buffer to the value of {@code str} encoded in encoding {@code enc}.<p>
   * The buffer will expand as necessary to accommodate any value written.
   */
  def setString(pos: Int, str: String, enc: String): Buffer = wrap(internal.setString(pos, str, enc))

  /**
   * Returns the length of the buffer, measured in bytes.
   * All positions are indexed from zero.
   */
  def length(): Int = internal.length()

  /**
   * Returns a copy of the entire Buffer.
   */
  def copy(): Buffer = new Buffer(internal.copy())

  /**
   * Returns the Buffer as a Netty {@code ByteBuf}.<p>
   * This method is meant for internal use only.
   */
  def getByteBuf(): ByteBuf = internal.getByteBuf()

  override def equals(other: Any): Boolean = other match {
    case that: JBuffer => that.isInstanceOf[JBuffer] && this.internal == that
    case that: Buffer => (that canEqual this) && (this.internal == that.internal)
    case _ => false
  }

  def canEqual(other: Any) = other.isInstanceOf[Buffer]
}

object Buffer {
  /**
   * Create an empty buffer
   */
  def apply() = new Buffer(new JBuffer())

  /**
   * Create a buffer from a java buffer
   */
  def apply(jbuffer: JBuffer) = new Buffer(jbuffer)

  /**
   * Creates a new empty Buffer that is expected to have a size of {@code initialSizeHint} after data has been
   * written to it.<p>
   * Please note that {@code length} of the Buffer immediately after creation will be zero.<p>
   * The {@code initialSizeHint} is merely a hint to the system for how much memory to initially allocate to the buffer to prevent excessive
   * automatic re-allocations as data is written to it.
   */
  def apply(initialSizeHint: Int) = new Buffer(new JBuffer(initialSizeHint))

  /**
   * Create a new Buffer that contains the contents of a {@code byte[]}
   */
  def apply(bytes: Array[Byte]) = new Buffer(new JBuffer(bytes))

  /**
   * Create a new Buffer that contains the contents of a {@code String str} encoded according to the encoding {@code enc}
   */
  def apply(str: String, enc: String) = new Buffer(new JBuffer(str, enc))

  /**
   * Create a new Buffer that contains the contents of {@code String str} encoded with UTF-8 encoding
   */
  def apply(str: String) = new Buffer(new JBuffer(str))

  /**
   * Create a new Buffer from a Netty {@code ByteBuf} instance.
   * This method is meant for internal use only.
   */
  def apply(buffer: ByteBuf) = new Buffer(new JBuffer(buffer))

}
