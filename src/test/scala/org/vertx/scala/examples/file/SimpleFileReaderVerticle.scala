package org.vertx.scala.examples.file

import org.vertx.scala.core._
import org.vertx.scala.platform.Verticle
import org.vertx.java.core.AsyncResult
import org.vertx.scala.core.file.AsyncFile
import org.vertx.java.core.buffer.Buffer

class SimpleFileReaderVerticle extends Verticle {

  override def start(future: Future[Void]) {
    val fileName = "afile.txt"

    vertx.fileSystem.readFile(fileName, { ar =>
        if (ar.succeeded()) {
          val fileContentsAsString = ar.result().toString()
        }
        else {
          future.setFailure(ar.cause())
        }
      })
  }

}