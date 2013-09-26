package org.vertx.scala.tests.core.file

import org.vertx.scala.testtools.TestVerticle
import org.junit.Test
import org.vertx.testtools.VertxAssert._
import org.vertx.scala.core.AsyncResult
import java.io.File
import java.nio.file.Paths
import java.nio.file.Files
import org.vertx.scala.testframework.TestUtils
import scala.concurrent.Future
import scala.util.Success
import scala.util.Failure
import org.vertx.scala.core.file.AsyncFile
import scala.concurrent.Promise
import org.vertx.scala.core.buffer.Buffer
import org.vertx.java.core.buffer.{ Buffer => JBuffer }
import org.vertx.scala.core.file.FileProps

class FileTest extends TestVerticle {

  override def asyncBefore(): Future[Unit] = {
    (for {
      exists <- fileExists("x.dat") if (exists)
      deleted <- fileDelete("x.dat")
    } yield {
    }) recover {
      case ex: Throwable =>
    }
  }

  @Test def createFile: Unit = checkNoError(for {
    cf <- fileCreate("x.dat")
    ex <- fileExists("x.dat")
    df <- fileDelete("x.dat")
  } yield {
    ex
  })

  @Test def copyFile: Unit = checkNoError(for {
    cf <- fileCreate("x.dat")
    fc <- fileCopy("x.dat", "y.dat")
    ex <- fileExists("y.dat")
    df <- fileDelete("x.dat")
    df <- fileDelete("y.dat")
  } yield {
    ex
  })

  @Test def deleteFile: Unit = checkNoError(for {
    cf <- fileCreate("x.dat")
    df <- fileDelete("x.dat")
    ex <- fileExists("x.dat")
  } yield {
    !ex
  })

  @Test def moveFile: Unit = checkNoError(for {
    cf <- fileCreate("x.dat")
    mf <- fileMove("x.dat", "./src/x.dat")
    ex <- fileExists("./src/x.dat")
    df <- fileDelete("./src/x.dat")
  } yield {
    ex
  })

  @Test def existsFile: Unit = checkNoError(for {
    _ <- fileCreate("x.dat")
    ex <- fileExists("x.dat")
    _ <- fileDelete("x.dat")
  } yield {
    ex
  })

  @Test def writeFile: Unit = checkNoError(for {
    wf <- fileWrite("x.dat", "Hello")
    ex <- fileExists("x.dat")
    df <- fileDelete("x.dat")
  } yield {
    ex
  })

  @Test def readFile: Unit = checkNoError(for {
    wf <- fileWrite("x.dat", "Hello")
    rf <- fileRead("x.dat")
    df <- fileDelete("x.dat")
  } yield {
    "Hello" == rf.toString
  })

  @Test def linkFile: Unit = checkNoError(for {
    cf <- fileCreate("x.dat")
    lf <- fileLink("y.dat", "x.dat")
    ex <- fileExists("y.dat")
    df <- fileDelete("x.dat")
    df <- fileDelete("y.dat")
  } yield {
    ex
  })

  @Test def unlinkFile: Unit = checkNoError(for {
    cf <- fileCreate("x.dat")
    lf <- fileLink("y.dat", "x.dat")
    uf <- fileUnlink("y.dat")
    ex <- fileExists("y.dat")
    df <- fileDelete("x.dat")
  } yield {
    !ex
  })

  @Test def symlinkFile: Unit = checkNoError(for {
    cf <- fileCreate("x.dat")
    lf <- fileSymlink("y.dat", "x.dat")
    ex <- fileExists("y.dat")
    df <- fileDelete("x.dat")
    df <- fileDelete("y.dat")
  } yield {
    ex
  })

  @Test def readSymlinkFile: Unit = checkNoError(for {
    cf <- fileCreate("x.dat")
    lf <- fileSymlink("y.dat", "x.dat")
    ex <- fileReadSymLink("y.dat")
    df <- fileDelete("x.dat")
    df <- fileDelete("y.dat")
  } yield {
    ex.toString() == "x.dat"
  })

  @Test def propsFile: Unit = {
    val startTime = 1000 * (System.currentTimeMillis() / 1000 - 1);
    checkNoError(for {
      cf <- fileCreate("x.dat")
      pf <- fileProps("x.dat")
      df <- fileDelete("x.dat")
    } yield {
      !pf.isDirectory
      pf.isRegularFile
      !pf.isSymbolicLink
      !pf.isOther
      pf.lastAccessTime.getTime >= startTime
      pf.creationTime.getTime >= startTime
      pf.lastModifiedTime.getTime >= startTime
    })
  }
  
  @Test def chmodFile: Unit = checkNoError((for {
    cf <- fileCreate("x.dat")
    fc <- fileChmod("x.dat", "rwxr-xr-x")
    df <- fileDelete("x.dat")
  } yield {
    fc.toString() == "rwxr-xr-x"
  }) recover {
      case ex: Throwable => false
  })
  
  @Test def truncateFile: Unit = checkNoError(for {
    wf <- fileWrite("x.dat", "Hallo-was-los")
    tf <- fileTruncate("x.dat", 5)
    rf <- fileRead("x.dat")
    df <- fileDelete("x.dat")
  } yield {
    rf.toString.getBytes.length == 5L
  })
  
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
  private def fileDelete(f: String): Future[Void] = promisify { p: Promise[Void] => vertx.fileSystem.delete(f, resultInPromise(p)) }
  private def fileCopy(f: String, t: String): Future[Void] = promisify { p: Promise[Void] => vertx.fileSystem.copy(f, t, resultInPromise(p)) }
  private def fileMove(f: String, t: String): Future[Void] = promisify { p: Promise[Void] => vertx.fileSystem.move(f, t, resultInPromise(p)) }
  private def fileExists(f: String): Future[Boolean] = promisify { p: Promise[Boolean] => vertx.fileSystem.exists(f, resultInPromise(p)) }
  private def fileWrite(f: String, d: String): Future[Void] = promisify { p: Promise[Void] => vertx.fileSystem.writeFile(f, new JBuffer(d), resultInPromise(p)) }
  private def fileRead(f: String): Future[Buffer] = promisify { p: Promise[Buffer] => vertx.fileSystem.readFile(f, resultInPromise(p)) }
  private def fileLink(l: String, e: String): Future[Void] = promisify { p: Promise[Void] => vertx.fileSystem.link(l, e, resultInPromise(p)) }
  private def fileSymlink(l: String, e: String): Future[Void] = promisify { p: Promise[Void] => vertx.fileSystem.symlink(l, e, resultInPromise(p)) }
  private def fileUnlink(l: String): Future[Void] = promisify { p: Promise[Void] => vertx.fileSystem.unlink(l, resultInPromise(p)) }
  private def fileReadSymLink(l: String): Future[String] = promisify { p: Promise[String] => vertx.fileSystem.readSymlink(l, resultInPromise(p)) }
  private def fileProps(l: String): Future[FileProps] = promisify { p: Promise[FileProps] => vertx.fileSystem.props(l, resultInPromise(p)) }
  private def fileChmod(f: String, per: String): Future[Void] = promisify { p: Promise[Void] => vertx.fileSystem.chmod(f, per, resultInPromise(p)) }
  private def fileTruncate(f: String, l: Long): Future[Void] = promisify { p: Promise[Void] => vertx.fileSystem.truncate(f, l, resultInPromise(p)) }

  private def checkNoError(fut: Future[Boolean]) = fut onComplete {
    case Success(x) =>
      assertTrue(x)
      testComplete()
    case Failure(x) => fail("Failed: " + x.getCause().getMessage())
  }
}