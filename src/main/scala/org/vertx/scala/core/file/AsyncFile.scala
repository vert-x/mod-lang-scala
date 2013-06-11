package org.vertx.scala.core.file


import org.vertx.java.core.file.{AsyncFile => JAsyncFile}
import org.vertx.java.core.streams.{WriteStream, ReadStream}
import org.vertx.java.core.{file, AsyncResult, Handler}
import org.vertx.java.core.buffer.Buffer
import org.vertx.scala.core.FunctionConverters._

/**
 * @author Edgar Chan
 */
object AsyncFile {
  def apply(internal:JAsyncFile) = new AsyncFile(internal)
  implicit def toScala(internal:JAsyncFile) = new AsyncFile(internal)
}

class AsyncFile(internal:JAsyncFile) extends ReadStream[AsyncFile] with WriteStream[AsyncFile]{

  def dataHandler(h: (Buffer) => Unit):AsyncFile={
    dataHandler(h)
    this
  }

  def dataHandler(p1: Handler[Buffer]): AsyncFile = {
    internal.dataHandler(p1)
    this
  }

  def pause: AsyncFile = {
    internal.pause
    this
  }

  def resume: AsyncFile = {
    internal.resume()
    this
  }

  def endHandler(h: () => Unit ):AsyncFile = endHandler(h)

  def endHandler(h: Handler[Void]): AsyncFile = {
    internal.endHandler(h)
    this
  }

  def write(b: Buffer): AsyncFile = {
    internal.write(b)
    this
  }

  def setWriteQueueMaxSize(m: Int): AsyncFile = {
    internal.setWriteQueueMaxSize(m)
    this
  }

  def writeQueueFull: Boolean = internal.writeQueueFull()

  def drainHandler(h: () => Unit):AsyncFile = drainHandler(h)

  def drainHandler(h: Handler[Void]): AsyncFile = {
    internal.drainHandler(h)
    this
  }

  def exceptionHandler(h: () => Throwable ):AsyncFile = exceptionHandler(h)

  def exceptionHandler(handler: Handler[Throwable]): AsyncFile = {
    internal.exceptionHandler(handler)
    this
  }

  def close(){
    internal.close()
  }

  def close(h:AsyncResult[Unit] => Unit){
    close(h)
  }

  def close(handler: Handler[AsyncResult[Void]]) {
    internal.close(handler)
  }


  def write(b:Buffer, p:Int, h:AsyncResult[Unit] => Unit ):AsyncFile={
    write(b, p, h)
  }

  def write(buffer: Buffer, position: Int, handler: Handler[AsyncResult[Void]]):AsyncFile = {
    internal.write(buffer, position, handler)
    this
  }


  def read(buffer: Buffer, offset: Int, position: Int, length: Int, handler: AsyncResult[Buffer] => Unit):AsyncFile = {
    read(buffer, offset, position, length, handler)
    this
  }

  def read(buffer: Buffer, offset: Int, position: Int, length: Int, handler: Handler[AsyncResult[Buffer]]):AsyncFile = {
    internal.read(buffer, offset, position, length, handler)
    this
  }

  def flush():AsyncFile = {
    internal.flush()
    this
  }

  def flush(handler:AsyncResult[Unit] => Unit):AsyncFile={
   flush(handler)
  }

  def flush(handler: Handler[AsyncResult[Void]]):AsyncFile ={
    internal.flush(handler)
    this
  }
}
