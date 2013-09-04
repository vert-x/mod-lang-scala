/*
 * Copyright 2011-2013 the original author or authors.
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

package org.vertx.scala.tests

import org.vertx.testtools.TestVerticle
import org.vertx.testtools.VertxAssert.testComplete
import org.vertx.testtools.VertxAssert.assertEquals
import org.junit.{ Before, Test }
import java.nio.file.{ FileSystems, Files }
import java.io.File
import org.vertx.scala.testframework.TestUtils
import org.vertx.scala.core.streams.Pump
import org.vertx.scala.core.file.AsyncFile
import org.vertx.java.core.AsyncResult

/**
 * @author Edgar Chan
 */
class FileSystemTest extends TestVerticle {

  import org.vertx.scala.core._
  import org.vertx.scala.core.FunctionConverters._

  lazy val sVertx = Vertx(getVertx)
  lazy val fileDir = System.getProperty("java.io.tmpdir") + "/vertx"
  lazy val path = FileSystems.getDefault.getPath(fileDir)

  @Before
  override def initialize() {
    deleteTestDir()
    Files.createDirectory(path)
    FileSystemTest.super.initialize()
  }

  @Test
  def copyTest() {
    // FIXME test
    testComplete()
//    val fs = sVertx.fileSystem
//    val from = fileDir + "/foo.tmp"
//    val to = fileDir + "/bar.tmp"
//    val content = "some-data"
//
//    fs.writeFile(from, content, () => {
//      fs.copy(from, to, ares => {
//        assertEquals(true, Option(ares.cause).isEmpty)
//        fs.readFile(to, ares2 => {
//          assertEquals(true, Option(ares2.cause).isEmpty)
//          assertEquals(content, ares2.result.toString)
//          testComplete()
//        })
//      })
//    })
  }

  @Test
  def moveTest() {
    // FIXME test
    testComplete()
//    val fs = sVertx.fileSystem
//    val from = fileDir + "/foo1.tmp"
//    val to = fileDir + "/bar2.tmp"
//    val content = "some-data"
//
//    fs.writeFile(from, content, () => {
//      fs.move(from, to, ares => {
//        assertEquals(true, Option(ares.cause).isEmpty)
//        fs.readFile(to, ares2 => {
//          assertEquals(true, Option(ares2.cause).isEmpty)
//          assertEquals(content, ares2.result.toString)
//          fs.exists(from, ares3 => {
//            assertEquals(true, Option(ares3.cause).isEmpty)
//            testComplete()
//          })
//        })
//      })
//    })
//
  }

  @Test
  def readDirTest() {
    // FIXME test
    testComplete()
//    val fs = sVertx.fileSystem
//    val f1 = fileDir + "/foo.tmp"
//    val f2 = fileDir + "/bar.tmp"
//    val f3 = fileDir + "/baz.tmp"
//    val content = "some-data"
//
//    fs.writeFile(f1, content, () => {
//      fs.writeFile(f2, content, () => {
//        fs.writeFile(f3, content, () => {
//          fs.readDir(fileDir, ares => {
//            assertEquals(true, Option(ares.cause).isEmpty)
//            assertEquals(3, ares.result.length)
//            testComplete()
//          })
//        })
//      })
//    })
//
  }

  @Test
  def propTest() {
    // FIXME test
    testComplete()
//    val fs = sVertx.fileSystem
//    val f1 = fileDir + "/baz.tmp"
//    val content = "some-data"
//    fs.writeFile(f1, content, () => {
//      fs.props(f1, ares => {
//        assertEquals(true, Option(ares.cause).isEmpty)
//        assertEquals(true, ares.result.isRegularFile)
//        testComplete()
//      })
//    })
  }

  @Test
  def pumpFileTest() {
    // FIXME test
    testComplete()
//    val fs = sVertx.fileSystem
//    val from = fileDir + "/foo.tmp"
//    val to = fileDir + "/bar.tmp"
//    val content = TestUtils.generateRandomBuffer(10000)
//
//    fs.writeFile(from, content, () => {
//      fs.open(from, ares1 => {
//        assertEquals(true, Option(ares1.cause).isEmpty)
//        fs.open(to, ares2 => {
//          assertEquals(true, Option(ares2.cause).isEmpty)
//          val rs = ares1.result
//          val ws = ares2.result
//          val pump = Pump.createPump(rs, ws)
//          pump.start
//          rs.endHandler(() => {
//            ares1.result.close { cr1: AsyncResult[Void] =>
//              ares2.result.close({ cr2: AsyncResult[Void] =>
//                fs.readFile(to, ares3 => {
//                  assertEquals(true, Option(ares3.cause).isEmpty)
//                  TestUtils.bufferEquals(content, ares3.result)
//                  testComplete()
//                })
//              }: AsyncResult[Void] => Unit)
//            }
//          })
//        })
//      })
//    })
//
  }

  private def deleteTestDir() {
    def del(dir: File) {
      dir.listFiles.foreach(
        f => if (f.isDirectory) del(f) else f.delete)
      dir.delete
    }

    if (Files.exists(path)) {
      del(new File(fileDir))
    }
  }

}
