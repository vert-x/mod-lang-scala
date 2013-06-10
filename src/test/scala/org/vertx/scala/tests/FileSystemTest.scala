package org.vertx.scala.tests

import org.vertx.testtools.TestVerticle
import org.vertx.testtools.VertxAssert.testComplete
import org.vertx.testtools.VertxAssert.assertEquals
import org.junit.{Before, Test}
import java.nio.file.{FileSystems, Files}
import java.io.File

/**
 * @author Edgar Chan
 */
class FileSystemTest extends TestVerticle{

  import org.vertx.scala.core._

  lazy val fileDir =  System.getProperty("java.io.tmpdir") + "/vertx"
  lazy val path = FileSystems.getDefault.getPath(fileDir)

  @Before
  override def initialize() {
    deleteTestDir()
    Files.createDirectory(path)
    FileSystemTest.super.initialize()
  }

  @Test
  def copyTest(){
    val fs = vertx.newFileSystem
    val from = fileDir + "/foo.tmp"
    val to = fileDir + "/bar.tmp"
    val content = "some-data"

    fs.writeFile(from, content, () => {
       fs.copy(from, to, ares => {
         assertEquals(true, Option(ares.cause).isEmpty)
         fs.readFile(to, ares2 => {
           assertEquals(true, Option(ares2.cause).isEmpty)
           assertEquals(content, ares2.result.toString)
           testComplete()
         })
      })
    })
  }

  @Test
  def moveTest(){
    val fs = vertx.newFileSystem
    val from = fileDir + "/foo1.tmp"
    val to = fileDir + "/bar2.tmp"
    val content = "some-data"

    fs.writeFile(from, content, () => {
      fs.move(from, to, ares =>{
        assertEquals(true, Option(ares.cause).isEmpty)
        fs.readFile(to, ares2 => {
           assertEquals(true, Option(ares2.cause).isEmpty)
           assertEquals(content, ares2.result.toString)
           fs.exists(from, ares3 => {
             assertEquals(true, Option(ares3.cause).isEmpty)
             testComplete()
           })
        })
      })
    })

  }


  private def deleteTestDir(){
    def del(dir:File){
      dir.listFiles.foreach(
        f => if ( f.isDirectory ) del(f) else f.delete
      )
      dir.delete
    }
    del( new File(fileDir) )
  }


}
