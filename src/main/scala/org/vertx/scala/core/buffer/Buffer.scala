package org.vertx.scala.core.buffer

import org.vertx.java.core.buffer.{ Buffer => JBuffer }
import org.vertx.scala.Self
import io.netty.buffer.ByteBuf
import java.nio.ByteBuffer

/**
 * A Buffer represents a sequence of zero or more bytes that can be written to or read from, and which expands as
 * necessary to accommodate any bytes written to it.<p>
 * There are two ways to write data to a Buffer: The first method involves methods that take the form `setXXX`.
 * These methods write data into the buffer starting at the specified position. The position does not have to be inside data that
 * has already been written to the buffer; the buffer will automatically expand to encompass the position plus any data that needs
 * to be written. All positions are measured in bytes and start with zero.<p>
 * The second method involves methods that take the form `appendXXX`; these methods append data
 * at the end of the buffer.<p>
 * Methods exist to both `set` and `append` all primitive types, [[java.lang.String]]}, [[java.nio.ByteBuffer]] and
 * other instances of Buffer.<p>
 * Data can be read from a buffer by invoking methods which take the form `getXXX`. These methods take a parameter
 * representing the position in the Buffer from where to read data.<p>
 * Once a buffer has been written to a socket or other write stream, the same buffer instance can't be written again to another WriteStream.<p>
 * Instances of this class are not thread-safe.<p>
 *
 * @author <a href="http://tfox.org">Tim Fox</a>
 * @author <a href="http://www.campudus.com/">Joern Bernhardt</a>
 * @author Galder Zamarreño
 */
final class Buffer private[scala] (val asJava: JBuffer) extends Self {

  /**
   * Appends the specified `T` to the end of the Buffer. The buffer will
   * expand as necessary to accommodate any bytes written.<p>
   * Returns a reference to `this` so multiple operations can be appended together.
   *
   * @param value The value to append to the Buffer.
   * @return A reference to `this` so multiple operations can be appended together.
   */
  def append[T: BufferType](value: T): Buffer =
    wrap(implicitly[BufferType[T]].appendToBuffer(asJava, value))

  /**
   * Appends the specified `T` starting at the `offset` using `len` to the end of this Buffer. The buffer will
   * expand as necessary to accommodate any bytes written.<p>
   * Returns a reference to `this` so multiple operations can be appended together.
   *
   * @throws IndexOutOfBoundsException
   *         if the specified `offset` is less than `0`,
   *         if `offset` + `len` is greater than the buffer's capacity
   * @throws IllegalArgumentException if `len` is less than `0`
   */
  def append[T: BufferSeekType](value: T, offset: Int, len: Int): Buffer =
    wrap(implicitly[BufferSeekType[T]].appendToBuffer(asJava, value, offset, len))

  /**
   * Returns a `String` representation of the Buffer assuming it contains a
   * `String` encoding in UTF-8.
   * @return A string representation of the Buffer.
   */
  override def toString: String = asJava.toString

  /**
   * Returns a `String` representation of the Buffer with the encoding specified by `enc`
   * @param enc The encoding to use to compute the String.
   * @return A string representation of the Buffer.
   */
  def toString(enc: String): String = asJava.toString(enc)

  /**
   * Returns the `byte` at position `pos` in the Buffer.
   *
   * @throws IndexOutOfBoundsException if the specified `pos` is less than `0`
   *                                   or `pos + 1` is greater than the length of the Buffer.
   */
  def getByte(pos: Int): Byte = asJava.getByte(pos)

  /**
   * Returns the `int` at position `pos` in the Buffer.
   *
   * @throws IndexOutOfBoundsException if the specified `pos` is less than `0`
   *                                   or `pos + 4` is greater than the length of the Buffer.
   */
  def getInt(pos: Int): Int = asJava.getInt(pos)

  /**
   * Returns the `long` at position `pos` in the Buffer.
   *
   * @throws IndexOutOfBoundsException if the specified `pos` is less than `0`
   *                                   or `pos + 8` is greater than the length of the Buffer.
   */
  def getLong(pos: Int): Long = asJava.getLong(pos)

  /**
   * Returns the `double` at position `pos` in the Buffer.
   *
   * @throws IndexOutOfBoundsException if the specified `pos` is less than `0`
   *                                   or `pos + 8` is greater than the length of the Buffer.
   */
  def getDouble(pos: Int): Double = asJava.getDouble(pos)

  /**
   * Returns the `float` at position `pos` in the Buffer.
   *
   * @throws IndexOutOfBoundsException if the specified `pos` is less than `0`
   *                                   or `pos + 4` is greater than the length of the Buffer.
   */
  def getFloat(pos: Int): Float = asJava.getFloat(pos)

  /**
   * Returns the `short` at position `pos` in the Buffer.
   *
   * @throws IndexOutOfBoundsException if the specified `pos` is less than `0`
   *                                   or `pos + 2` is greater than the length of the Buffer.
   */
  def getShort(pos: Int): Short = asJava.getShort(pos)

  /**
   * Returns a copy of the entire Buffer as a `byte[]`
   */
  def getBytes: Array[Byte] = asJava.getBytes

  /**
   * Returns a copy of a sub-sequence the Buffer as a `byte[]` starting at position `start`
   * and ending at position `end - 1`
   */
  def getBytes(start: Int, end: Int): Array[Byte] = asJava.getBytes(start, end)

  /**
   * Returns a copy of a sub-sequence the Buffer as a [[org.vertx.scala.core.buffer.Buffer]] starting
   * at position `start` and ending at position `end - 1`
   */
  def getBuffer(start: Int, end: Int): Buffer = wrap(asJava.getBuffer(start, end))

  /**
   * Returns a copy of a sub-sequence the Buffer as a `String` starting at position `start`
   * and ending at position `end - 1` interpreted as a String in the specified encoding
   */
  def getString(start: Int, end: Int, enc: String): String = asJava.getString(start, end, enc)

  /**
   * Returns a copy of a sub-sequence the Buffer as a `String` starting at position `start`
   * and ending at position `end - 1` interpreted as a String in UTF-8 encoding
   */
  def getString(start: Int, end: Int): String = asJava.getString(start, end)

  /**
   * Sets the `byte` at position `pos` in the Buffer to the value `b`.<p>
   * The buffer will expand as necessary to accommodate any value written.
   */
  def setByte(pos: Int, b: Byte): Buffer = wrap(asJava.setByte(pos, b))

  /**
   * Sets the `int` at position `pos` in the Buffer to the value `i`.<p>
   * The buffer will expand as necessary to accommodate any value written.
   */
  def setInt(pos: Int, i: Int): Buffer = wrap(asJava.setInt(pos, i))

  /**
   * Sets the `long` at position `pos` in the Buffer to the value `l`.<p>
   * The buffer will expand as necessary to accommodate any value written.
   */
  def setLong(pos: Int, l: Long): Buffer = wrap(asJava.setLong(pos, l))

  /**
   * Sets the `double` at position `pos` in the Buffer to the value `d`.<p>
   * The buffer will expand as necessary to accommodate any value written.
   */
  def setDouble(pos: Int, d: Double): Buffer = wrap(asJava.setDouble(pos, d))

  /**
   * Sets the `float` at position `pos` in the Buffer to the value `f`.<p>
   * The buffer will expand as necessary to accommodate any value written.
   */
  def setFloat(pos: Int, f: Float): Buffer = wrap(asJava.setFloat(pos, f))

  /**
   * Sets the `short` at position `pos` in the Buffer to the value `s`.<p>
   * The buffer will expand as necessary to accommodate any value written.
   */
  def setShort(pos: Int, s: Short): Buffer = wrap(asJava.setShort(pos, s))

  /**
   * Sets the bytes at position `pos` in the Buffer to the bytes represented by the `Buffer b`.<p>
   * The buffer will expand as necessary to accommodate any value written.
   */
  def setBuffer(pos: Int, b: Buffer): Buffer = wrap(asJava.setBuffer(pos, b.asJava))

  /**
   * Sets the bytes at position `pos` in the Buffer to the bytes represented by the `Buffer b`
   * on the given `offset` and `len`.<p>
   * The buffer will expand as necessary to accommodate any value written.
   */
  def setBuffer(pos: Int, b: Buffer, offset: Int, len: Int): Buffer =
    wrap(asJava.setBuffer(pos, b.asJava, offset, len))

  /**
   * Sets the bytes at position `pos` in the Buffer to the bytes represented by the `ByteBuffer b`.<p>
   * The buffer will expand as necessary to accommodate any value written.
   */
  def setBytes(pos: Int, b: ByteBuffer): Buffer = wrap(asJava.setBytes(pos, b))

  /**
   * Sets the bytes at position `pos` in the Buffer to the bytes represented by the `byte[] b`.<p>
   * The buffer will expand as necessary to accommodate any value written.
   */
  def setBytes(pos: Int, b: Array[Byte]): Buffer = wrap(asJava.setBytes(pos, b))

  /**
   * Sets the given number of bytes at position `pos` in the Buffer to
   * the bytes represented by the `byte[] b`.<p></p>
   * The buffer will expand as necessary to accommodate any value written.
   *
   * @throws IndexOutOfBoundsException
   *         if the specified `offset` is less than `0`,
   *         if `offset` + `len` is greater than the buffer's capacity, or
   *         if `pos` + `length` is greater than `b.length`
   * @throws IllegalArgumentException if `len` is less than `0`
   */
  def setBytes(pos: Int, b: Array[Byte], offset: Int, len: Int): Buffer =
    wrap(asJava.setBytes(pos, b, offset, len))

  /**
   * Sets the bytes at position `pos` in the Buffer to the value of `str` encoded in UTF-8.<p>
   * The buffer will expand as necessary to accommodate any value written.
   */
  def setString(pos: Int, str: String): Buffer = wrap(asJava.setString(pos, str))

  /**
   * Sets the bytes at position `pos` in the Buffer to the value of `str` encoded in encoding `enc`.<p>
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
   * Returns the Buffer as a Netty `ByteBuf`.<p>
   * This method is meant for internal use only.
   */
  def getByteBuf: ByteBuf = asJava.getByteBuf

  override def equals(other: Any): Boolean = other match {
    case that: JBuffer => that.isInstanceOf[JBuffer] && this.asJava == that
    case that: Buffer => (that canEqual this) && (this.asJava == that.asJava)
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
   * Creates a new empty Buffer that is expected to have a size of `initialSizeHint` after data has been
   * written to it.<p>
   * Please note that `length` of the Buffer immediately after creation will be zero.<p>
   * The `initialSizeHint` is merely a hint to the system for how much memory to initially allocate to the buffer to prevent excessive
   * automatic re-allocations as data is written to it.
   */
  def apply(initialSizeHint: Int) = new Buffer(new JBuffer(initialSizeHint))

  /**
   * Create a new Buffer that contains the contents of a `byte[]`
   */
  def apply(bytes: Array[Byte]) = new Buffer(new JBuffer(bytes))

  /**
   * Create a new Buffer that contains the contents of a `String str` encoded according to the encoding `enc`
   */
  def apply(str: String, enc: String) = new Buffer(new JBuffer(str, enc))

  /**
   * Create a new Buffer that contains the contents of `String str` encoded with UTF-8 encoding
   */
  def apply(str: String) = new Buffer(new JBuffer(str))

  /**
   * Create a new Buffer from a Netty `ByteBuf` instance.
   * This method is meant for internal use only.
   */
  def apply(buffer: ByteBuf) = new Buffer(new JBuffer(buffer))

}
