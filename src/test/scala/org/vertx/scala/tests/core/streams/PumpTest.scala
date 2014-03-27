package org.vertx.scala.tests.core.streams

import java.nio.file.{Paths, Files}
import org.junit.Test
import org.vertx.scala.core.AsyncResult
import org.vertx.scala.core.buffer.{ Buffer, BufferElem }
import org.vertx.scala.core.file.AsyncFile
import org.vertx.scala.core.http.{ HttpClient, HttpClientResponse, HttpServer, HttpServerRequest }
import org.vertx.scala.core.streams.Pump
import org.vertx.scala.testtools.TestVerticle
import org.vertx.testtools.VertxAssert._

class PumpTest extends TestVerticle {

  val testPort = 8844

  val html = <html>
               <head>
                 <title>test</title>
               </head>
               <body>
                 <h1>Hello world!</h1>
               </body>
             </html>.toString()

  @Test
  def pumpingHttp(): Unit = {
    vertx.createHttpServer.requestHandler(regularRequestHandler).listen(testPort, checkServer({ c =>
      assertThread()
      val chunk = Buffer("hello pump")
      val req = c.post("/", { resp =>
        assertEquals(200, resp.statusCode)
        val buffer = Buffer()
        resp.dataHandler { buff =>
          assertThread()
          buffer.append(buff)
        }
        resp.endHandler {
          assertThread()
          assertEquals(chunk.length(), buffer.length())
          assertEquals(chunk, buffer)
          testComplete()
        }
      })
      req.setChunked(true)
      req.write(chunk)
      req.end()
    }))
  }

  @Test
  def pumpingFile(): Unit = withPumpFiles { (filename, oldFile, newFile) =>
    oldFile.endHandler({
      assertThread()
      oldFile.close { readCloseResult =>
        if (readCloseResult.failed())
          fail("Fail to close read file")
        else {
          newFile.close { writeCloseResult =>
            if (writeCloseResult.failed())
              fail("Fail to close write file")
            else {
              val readFileSize = vertx.fileSystem.propsSync(filename).size()
              val writtenBytes = Files.readAllBytes(Paths.get(filename + ".copy"))
              assertEquals(readFileSize, writtenBytes.length)
              vertx.fileSystem.deleteSync(filename + ".copy")
              testComplete()
            }
          }
        }
      }
    })

    Pump.createPump(oldFile, newFile).start()
  }

  @Test
  def pumpStopAndResume(): Unit = withPumpFiles { (filename, oldFile, newFile) =>
    val filesize = vertx.fileSystem.propsSync(filename).size
    val pump = Pump.createPump(oldFile, newFile)
    pump.setWriteQueueMaxSize((filesize / 10).intValue)

    for (i <- 0 to 9) {
      pump.start
      pump.stop
    }

    oldFile.endHandler({
      assertThread()
      val filesizeNew = vertx.fileSystem.propsSync(filename + ".copy").size
      val oldContents = vertx.fileSystem.readFileSync(filename)
      val newContents = vertx.fileSystem.readFileSync(filename + ".copy")
      assertEquals("Filesize should match", filesize, filesizeNew)
      assertEquals("Filesize should match pumped bytes", filesize, pump.bytesPumped)
      assertEquals("Contents should match", oldContents, newContents)
      testComplete
    })
    pump.start
  }

  private def withPumpFiles(fn: (String, AsyncFile, AsyncFile) => Unit): Unit = {
    val file = "src/test/resources/pumpfile.txt"
    if (vertx.fileSystem.existsSync(file + ".copy")) {
      vertx.fileSystem.deleteSync(file + ".copy")
    }
    vertx.fileSystem.createFileSync(file + ".copy")
    vertx.fileSystem.open(file, { ar1: AsyncResult[AsyncFile] =>
      assertThread()
      assertTrue("Should be able to open " + file, ar1.succeeded())
      val oldFile = ar1.result()
      vertx.fileSystem.open(file + ".copy", { ar2: AsyncResult[AsyncFile] =>
        assertThread()
        assertTrue("Should be able to open " + file + ".copy", ar2.succeeded())
        val newFile = ar2.result()
        fn(file, oldFile, newFile)
      })
    })
  }

  private def regularRequestHandler: HttpServerRequest => Unit = { req =>
    req.response.setChunked(true)
    req.endHandler({ req.response.end })
    Pump.createPump(req, req.response).start
  }

  private def checkServer(clientFn: HttpClient => Unit) = { ar: AsyncResult[HttpServer] =>
    assertThread()
    if (ar.succeeded()) {
      val httpClient = vertx.createHttpClient.setPort(testPort)
      clientFn(httpClient)
    } else {
      fail("listening did not succeed: " + ar.cause().getMessage())
    }
  }

  private def correctHandler(fn: () => Unit) = { resp: HttpClientResponse =>
    resp.bodyHandler({ buf =>
      assertThread()
      assertEquals(html, buf.toString)
      fn()
    }): Unit
  }

}