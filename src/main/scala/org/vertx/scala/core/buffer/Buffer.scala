package org.vertx.scala.core.buffer

import org.vertx.java.core.buffer.{ Buffer => JBuffer }
import org.vertx.scala.Self
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
 * @author <a href="http://www.campudus.com/">Joern Bernhardt</a>
 * @author Galder Zamarreño
 */
// constructor is private because users should use apply in companion
// extends AnyVal to avoid object allocation and improve performance
final class Buffer private[scala] (val asJava: JBuffer) extends AnyVal with Self[Buffer] {

  override protected[this] def self = this

  /**
   * Appends the specified {@code T} to the end of the Buffer. The buffer will expand as necessary
   * to accommodate any bytes written.<p>
   * Returns a reference to {@code this} so multiple operations can be appended together.
   *
   * @param value The value to append to the Buffer.
   * @return A reference to {@code this} so multiple operations can be appended together.
   */
  def append[T: BufferType](value: T): Buffer = wrap(implicitly[BufferType[T]].appendToBuffer(asJava, value))

  /**
   * Returns a {@code String} representation of the Buffer assuming it contains a {@code String} encoding in UTF-8.
   * @return A string representation of the Buffer.
   */
  override def toString(): String = asJava.toString()

  /**
   * Returns a {@code String} representation of the Buffer with the encoding specified by {@code enc}
   * @param enc The encoding to use to compute the String.
   * @return A string representation of the Buffer.
   */
  def toString(enc: String): String = asJava.toString(enc)

  /**
   * Returns the {@code byte} at position {@code pos} in the Buffer.
   *
   * @throws IndexOutOfBoundsException if the specified {@code pos} is less than {@code 0} or {@code pos + 1} is greater than the length of the Buffer.
   */
  def getByte(pos: Int): Byte = asJava.getByte(pos)

  /**
   * Returns the {@code int} at position {@code pos} in the Buffer.
   *
   * @throws IndexOutOfBoundsException if the specified {@code pos} is less than {@code 0} or {@code pos + 4} is greater than the length of the Buffer.
   */
  def getInt(pos: Int): Int = asJava.getInt(pos)

  /**
   * Returns the {@code long} at position {@code pos} in the Buffer.
   *
   * @throws IndexOutOfBoundsException if the specified {@code pos} is less than {@code 0} or {@code pos + 8} is greater than the length of the Buffer.
   */
  def getLong(pos: Int): Long = asJava.getLong(pos)

  /**
   * Returns the {@code double} at position {@code pos} in the Buffer.
   *
   * @throws IndexOutOfBoundsException if the specified {@code pos} is less than {@code 0} or {@code pos + 8} is greater than the length of the Buffer.
   */
  def getDouble(pos: Int): Double = asJava.getDouble(pos)

  /**
   * Returns the {@code float} at position {@code pos} in the Buffer.
   *
   * @throws IndexOutOfBoundsException if the specified {@code pos} is less than {@code 0} or {@code pos + 4} is greater than the length of the Buffer.
   */
  def getFloat(pos: Int): Float = asJava.getFloat(pos)

  /**
   * Returns the {@code short} at position {@code pos} in the Buffer.
   *
   * @throws IndexOutOfBoundsException if the specified {@code pos} is less than {@code 0} or {@code pos + 2} is greater than the length of the Buffer.
   */
  def getShort(pos: Int): Short = asJava.getShort(pos)

  /**
   * Returns a copy of the entire Buffer as a {@code byte[]}
   */
  def getBytes(): Array[Byte] = asJava.getBytes()

  /**
   * Returns a copy of a sub-sequence the Buffer as a {@code byte[]} starting at position {@code start}
   * and ending at position {@code end - 1}
   */
  def getBytes(start: Int, end: Int): Array[Byte] = asJava.getBytes(start, end)

  /**
   * Returns a copy of a sub-sequence the Buffer as a {@link Buffer} starting at position {@code start}
   * and ending at position {@code end - 1}
   */
  def getBuffer(start: Int, end: Int): Buffer = wrap(asJava.getBuffer(start, end))

  /**
   * Returns a copy of a sub-sequence the Buffer as a {@code String} starting at position {@code start}
   * and ending at position {@code end - 1} interpreted as a String in the specified encoding
   */
  def getString(start: Int, end: Int, enc: String): String = asJava.getString(start, end, enc)

  /**
   * Returns a copy of a sub-sequence the Buffer as a {@code String} starting at position {@code start}
   * and ending at position {@code end - 1} interpreted as a String in UTF-8 encoding
   */
  def getString(start: Int, end: Int): String = asJava.getString(start, end)

  /**
   * Sets the {@code byte} at position {@code pos} in the Buffer to the value {@code b}.<p>
   * The buffer will expand as necessary to accommodate any value written.
   */
  def setByte(pos: Int, b: Byte): Buffer = wrap(asJava.setByte(pos, b))

  /**
   * Sets the {@code int} at position {@code pos} in the Buffer to the value {@code i}.<p>
   * The buffer will expand as necessary to accommodate any value written.
   */
  def setInt(pos: Int, i: Int): Buffer = wrap(asJava.setInt(pos, i))

  /**
   * Sets the {@code long} at position {@code pos} in the Buffer to the value {@code l}.<p>
   * The buffer will expand as necessary to accommodate any value written.
   */
  def setLong(pos: Int, l: Long): Buffer = wrap(asJava.setLong(pos, l))

  /**
   * Sets the {@code double} at position {@code pos} in the Buffer to the value {@code d}.<p>
   * The buffer will expand as necessary to accommodate any value written.
   */
  def setDouble(pos: Int, d: Double): Buffer = wrap(asJava.setDouble(pos, d))

  /**
   * Sets the {@code float} at position {@code pos} in the Buffer to the value {@code f}.<p>
   * The buffer will expand as necessary to accommodate any value written.
   */
  def setFloat(pos: Int, f: Float): Buffer = wrap(asJava.setFloat(pos, f))

  /**
   * Sets the {@code short} at position {@code pos} in the Buffer to the value {@code s}.<p>
   * The buffer will expand as necessary to accommodate any value written.
   */
  def setShort(pos: Int, s: Short): Buffer = wrap(asJava.setShort(pos, s))

  /**
   * Sets the bytes at position {@code pos} in the Buffer to the bytes represented by the {@code Buffer b}.<p>
   * The buffer will expand as necessary to accommodate any value written.
   */
  def setBuffer(pos: Int, b: Buffer): Buffer = wrap(asJava.setBuffer(pos, b.asJava))

  /**
   * Sets the bytes at position {@code pos} in the Buffer to the bytes represented by the {@code ByteBuffer b}.<p>
   * The buffer will expand as necessary to accommodate any value written.
   */
  def setBytes(pos: Int, b: ByteBuffer): Buffer = wrap(asJava.setBytes(pos, b))

  /**
   * Sets the bytes at position {@code pos} in the Buffer to the bytes represented by the {@code byte[] b}.<p>
   * The buffer will expand as necessary to accommodate any value written.
   */
  def setBytes(pos: Int, b: Array[Byte]): Buffer = wrap(asJava.setBytes(pos, b))

  /**
   * Sets the bytes at position {@code pos} in the Buffer to the value of {@code str} encoded in UTF-8.<p>
   * The buffer will expand as necessary to accommodate any value written.
   */
  def setString(pos: Int, str: String): Buffer = wrap(asJava.setString(pos, str))

  /**
   * Sets the bytes at position {@code pos} in the Buffer to the value of {@code str} encoded in encoding {@code enc}.<p>
   * The buffer will expand as necessary to accommodate any value written.
   */
  def setString(pos: Int, str: String, enc: String): Buffer = wrap(asJava.setString(pos, str, enc))

  /**
   * Returns the length of the buffer, measured in bytes.
   * All positions are indexed from zero.
   */
  def length(): Int = asJava.length()

  /**
   * Returns a copy of the entire Buffer.
   */
  def copy(): Buffer = new Buffer(asJava.copy())

  /**
   * Returns the Buffer as a Netty {@code ByteBuf}.<p>
   * This method is meant for internal use only.
   */
  def getByteBuf(): ByteBuf = asJava.getByteBuf()

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
