package org.vertx.scala.tests.core.file

import org.vertx.scala.testtools.TestVerticle
import org.junit.{ Ignore, Test }
import org.vertx.testtools.VertxAssert._
import org.vertx.scala.core.AsyncResult
import scala.concurrent.Future
import scala.util.Success
import scala.util.Failure
import org.vertx.scala.core.file.AsyncFile
import scala.concurrent.Promise
import org.vertx.scala.core.buffer.Buffer
import org.vertx.scala.core.file.FileProps
import org.hamcrest.Matchers._

class FileTest extends TestVerticle {

  override def asyncBefore(): Future[Unit] = {
    (for {
      exists <- fileExists("x.dat") if (exists)
      deleted <- fileDelete("x.dat", false)
    } yield {
    }) recover {
      case ex: Throwable =>
    }
  }

  @Test def createFile: Unit = checkNoError(for {
    cf <- fileCreate("x.dat")
    ex <- fileExists("x.dat")
    df <- fileDelete("x.dat", false)
  } yield assertTrue(ex))

  @Test def copyFile: Unit = checkNoError(for {
    cf <- fileCreate("x.dat")
    fc <- fileCopy("x.dat", "y.dat")
    ex <- fileExists("y.dat")
    df <- fileDelete("x.dat", false)
    df <- fileDelete("y.dat", false)
  } yield assertTrue(ex))

  @Test def deleteFile: Unit = checkNoError(for {
    cf <- fileCreate("x.dat")
    df <- fileDelete("x.dat", false)
    ex <- fileExists("x.dat")
  } yield assertFalse(ex))

  @Test def moveFile: Unit = checkNoError(for {
    cf <- fileCreate("x.dat")
    mf <- fileMove("x.dat", "./src/x.dat")
    ex <- fileExists("./src/x.dat")
    df <- fileDelete("./src/x.dat", false)
  } yield assertTrue(ex))

  @Test def existsFile: Unit = checkNoError(for {
    _ <- fileCreate("x.dat")
    ex <- fileExists("x.dat")
    _ <- fileDelete("x.dat", false)
  } yield assertTrue(ex))

  @Test def writeFile: Unit = checkNoError(for {
    wf <- fileWrite("x.dat", "Hello")
    ex <- fileExists("x.dat")
    df <- fileDelete("x.dat", false)
  } yield assertTrue(ex))

  @Test def readFile: Unit = checkNoError(for {
    wf <- fileWrite("x.dat", "Hello")
    rf <- fileRead("x.dat")
    df <- fileDelete("x.dat", false)
  } yield assertEquals("Hello", rf.toString()))

  @Test def linkFile: Unit = checkNoError(for {
    cf <- fileCreate("x.dat")
    lf <- fileLink("y.dat", "x.dat")
    ex <- fileExists("y.dat")
    df <- fileDelete("x.dat", false)
    df <- fileDelete("y.dat", false)
  } yield assertTrue(ex))

  @Test def unlinkFile: Unit = checkNoError(for {
    cf <- fileCreate("x.dat")
    lf <- fileLink("y.dat", "x.dat")
    uf <- fileUnlink("y.dat")
    ex <- fileExists("y.dat")
    df <- fileDelete("x.dat", false)
  } yield assertFalse(ex))

  @Test def symlinkFile: Unit = checkNoError(for {
    cf <- fileCreate("x.dat")
    lf <- fileSymlink("y.dat", "x.dat")
    ex <- fileExists("y.dat")
    df <- fileDelete("x.dat", false)
    df <- fileDelete("y.dat", false)
  } yield assertTrue(ex))

  @Ignore("Bug in Vert.x Core: https://bugs.eclipse.org/bugs/show_bug.cgi?id=421932")
  @Test def readSymlinkFile: Unit = checkNoError(for {
    cf <- fileCreate("x.dat")
    lf <- fileSymlink("y.dat", "x.dat")
    ex <- fileReadSymLink("y.dat")
    df <- fileDelete("x.dat", false)
    df <- fileDelete("y.dat", false)
  } yield assertEquals("x.dat", ex.toString))

  @Test def readSymlinkFileEndsWith: Unit = checkNoError(for {
    cf <- fileCreate("x.dat")
    lf <- fileSymlink("y.dat", "x.dat")
    ex <- fileReadSymLink("y.dat")
    df <- fileDelete("x.dat", false)
    df <- fileDelete("y.dat", false)
  } yield assertThat(ex.toString, endsWith("x.dat")))

  @Test def propsFile: Unit = {
    val startTime = 1000 * (System.currentTimeMillis() / 1000 - 1)
    checkNoError(for {
      cf <- fileCreate("x.dat")
      pf <- fileProps("x.dat")
      df <- fileDelete("x.dat", false)
    } yield { () =>
      assertFalse(pf.isDirectory)
      assertTrue(pf.isRegularFile)
      assertFalse(pf.isSymbolicLink)
      assertFalse(pf.isOther)
      // greaterThan matchers but do not work, Comparable implicit conversion fails :(
      // maybe use ScalaTest asserts instead?
      assertTrue(pf.lastAccessTime().getTime >= startTime)
      assertTrue(pf.creationTime().getTime >= startTime)
      assertTrue(pf.lastModifiedTime().getTime >= startTime)
    })
  }

  @Test def chmodFile: Unit = checkNoError(for {
    of <- fileOpen("ch.dat")
    aw <- fileAsynWrite(of, "Hello-Chmod", 0)
    fc <- fileChmod("ch.dat", "---------")
    aw <- fileAsynWrite(of, "Hello-Chmod-xxxx", 0)
    rf <- fileAsynRead(of, 0, 0, "Hello-Chmod".length)
    df <- fileDelete("ch.dat", false)
  } yield assertEquals("Hello-Chmod", rf.toString()))

  @Test def truncateFile: Unit = checkNoError(for {
    wf <- fileWrite("x.dat", "Hallo-was-los")
    tf <- fileTruncate("x.dat", 5)
    rf <- fileRead("x.dat")
    df <- fileDelete("x.dat", false)
  } yield assertEquals(5, rf.toString().getBytes.length))

  @Test def makeDirectory: Unit = checkNoError(for {
    wf <- fileMkdir("d")
    pf <- fileProps("d")
    df <- fileDelete("d", true)
  } yield assertTrue(pf.isDirectory))

  @Test def readDirectory: Unit = checkNoError(for {
    wf <- fileMkdir("d")
    cf <- fileCreate("./d/x.dat")
    cf <- fileCreate("./d/y.dat")
    xf <- fileReadDir("d")
    df <- fileDelete("d", true)
  } yield assertThat(xf.length, is(2)))

  @Test def openFile: Unit = checkNoError(for {
    of <- fileOpen("asyn.dat")
    ex <- fileExists("asyn.dat")
    df <- fileDelete("asyn.dat", false)
  } yield assertTrue(ex))

  @Test def writeAndReadAsyncFile: Unit = checkNoError(for {
    of <- fileOpen("asynx.dat")
    aw <- fileAsynWrite(of, "Hello-World", 0)
    rf <- fileAsynRead(of, 0, 0, "Hello-World".length)
    df <- fileDelete("asynx.dat", false)
  } yield assertEquals("Hello-World", rf.toString()))

  private def resultInPromise[T](p: Promise[T]): AsyncResult[T] => Unit = { ar: AsyncResult[T] =>
    if (ar.succeeded()) {
      p.success(ar.result())
    } else {
      p.failure(ar.cause())
    }
  }

  private def promisify[T](x: Promise[T] => _): Future[T] = {
    val p = Promise[T]
    x(p)
    p.future
  }

  private def fileCreate(f: String): Future[Void] = promisify { p: Promise[Void] => vertx.fileSystem.createFile(f, resultInPromise(p)) }
  private def fileDelete(f: String, b: Boolean): Future[Void] = promisify { p: Promise[Void] => vertx.fileSystem.delete(f, b, resultInPromise(p)) }
  private def fileCopy(f: String, t: String): Future[Void] = promisify { p: Promise[Void] => vertx.fileSystem.copy(f, t, resultInPromise(p)) }
  private def fileMove(f: String, t: String): Future[Void] = promisify { p: Promise[Void] => vertx.fileSystem.move(f, t, resultInPromise(p)) }
  private def fileExists(f: String): Future[Boolean] = promisify { p: Promise[Boolean] => vertx.fileSystem.exists(f, resultInPromise(p)) }
  private def fileWrite(f: String, d: String): Future[Void] = promisify { p: Promise[Void] => vertx.fileSystem.writeFile(f, Buffer(d), resultInPromise(p)) }
  private def fileRead(f: String): Future[Buffer] = promisify { p: Promise[Buffer] => vertx.fileSystem.readFile(f, resultInPromise(p)) }
  private def fileLink(l: String, e: String): Future[Void] = promisify { p: Promise[Void] => vertx.fileSystem.link(l, e, resultInPromise(p)) }
  private def fileSymlink(l: String, e: String): Future[Void] = promisify { p: Promise[Void] => vertx.fileSystem.symlink(l, e, resultInPromise(p)) }
  private def fileUnlink(l: String): Future[Void] = promisify { p: Promise[Void] => vertx.fileSystem.unlink(l, resultInPromise(p)) }
  private def fileReadSymLink(l: String): Future[String] = promisify { p: Promise[String] => vertx.fileSystem.readSymlink(l, resultInPromise(p)) }
  private def fileProps(l: String): Future[FileProps] = promisify { p: Promise[FileProps] => vertx.fileSystem.props(l, resultInPromise(p)) }
  private def fileChmod(f: String, per: String): Future[Void] = promisify { p: Promise[Void] => vertx.fileSystem.chmod(f, per, resultInPromise(p)) }
  private def fileTruncate(f: String, l: Long): Future[Void] = promisify { p: Promise[Void] => vertx.fileSystem.truncate(f, l, resultInPromise(p)) }
  private def fileMkdir(f: String): Future[Void] = promisify { p: Promise[Void] => vertx.fileSystem.mkdir(f, resultInPromise(p)) }
  private def fileReadDir(f: String): Future[Array[String]] = promisify { p: Promise[Array[String]] => vertx.fileSystem.readDir(f, resultInPromise(p)) }
  private def fileOpen(f: String): Future[AsyncFile] = promisify { p: Promise[AsyncFile] => vertx.fileSystem.open(f, resultInPromise(p)) }
  private def fileAsynWrite(ar: AsyncFile, d: String, i: Int): Future[Void] = promisify { p: Promise[Void] => ar.write(Buffer(d), i, resultInPromise(p)) }
  private def fileAsynRead(ar: AsyncFile, o: Int, i: Int, l: Int): Future[Buffer] = promisify { p: Promise[Buffer] => ar.read(Buffer(1000), o, i, l, resultInPromise(p)) }

  private def checkNoError(fut: Future[Unit]) = fut onComplete {
    case Success(_) => testComplete()
    case Failure(x) => fail("Failed: " + x.getCause().getMessage())
  }
}