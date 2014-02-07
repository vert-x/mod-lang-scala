package org.vertx.scala.tests.util

import org.vertx.scala.core.AsyncResult
import org.vertx.testtools.VertxAssert._
import org.vertx.scala.platform.Container
import org.vertx.scala.core.buffer.Buffer
import scala.annotation.tailrec
import java.io.{FileOutputStream, OutputStreamWriter, BufferedWriter, File}

/**
 * Port of some of the original TestUtils Helper methods.
 *
 * @author Edgar Chan
 * @author Galder Zamarreño
 */
object TestUtils {

  def completeWithArFailed[T]: AsyncResult[T] => Unit = { ar =>
    assertTrue(ar.failed())
    assertTrue(ar.cause() != null)
    testComplete()
  }

  def startTests(script: AnyRef, container: Container): Unit = {
    val methodName = container.config().getString("methodName")
    script.getClass.getMethod(methodName).invoke(script)
  }

  def generateRandomBuffer(length: Int): Buffer =
    generateRandomBuffer(length, avoid = false, 0.toByte)

  def generateRandomBuffer(length: Int, avoid: Boolean, avoidByte: Byte): Buffer =
    Buffer(generateRandomByteArray(length, avoid, avoidByte))

  def generateRandomByteArray(length: Int): Array[Byte] =
    generateRandomByteArray(length, avoid = false,  0.toByte)

  def generateRandomByteArray(length: Int, avoid: Boolean, avoidByte: Byte): Array[Byte] = {
    val line  = new Array[Byte](length)
    var i = 0
    do {
      var rand:Byte = 0.toByte
      do {
        rand = ((Math.random() * 255) - 128).toByte
      } while(avoid && rand == avoidByte)
      line(i) = rand
      i += 1
    } while(i < length)
    line
  }

  def generateRandomUnicodeString(length: Int): String = {
    val builder = new StringBuilder(length)
    for (i <- 0 until length) {
      @tailrec def generateChar(): Char = {
        val c = (0xFFFF * Math.random()).toChar
        // Skip illegal chars
        if ((c >= 0xFFFE && c <= 0xFFFF) || (c >= 0xD800 && c <= 0xDFFF))
          generateChar()
        else
          c
      }

      builder.append(generateChar())
    }
    builder.toString()
  }

  def generateRandomContentFile(filename: String, len: Int): (File, String) = {
    val content = TestUtils.generateRandomUnicodeString(len)
    val file = createFile(filename, content)
    (file, content)
  }

  def generateFile(filename: String, content: String): (File, String) = {
    val file = createFile(filename, content)
    (file, content)
  }

  private def createFile(filename: String, content: String): File = {
    val file = new File(System.getProperty("java.io.tmpdir"), filename)
    if (file.exists())
      file.delete()

    val out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"))
    try {
      out.write(content)
    } finally {
      out.close()
    }

    file
  }

}