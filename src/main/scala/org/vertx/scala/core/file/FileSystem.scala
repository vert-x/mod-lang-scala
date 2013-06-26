/*
 * Copyright 2011-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License")
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

package org.vertx.scala.core.file

import org.vertx.java.core.{file, Handler, AsyncResult}
import org.vertx.java.core.buffer.Buffer
import org.vertx.java.core.file.{FileProps, FileSystemProps, AsyncFile => JAsyncFile}
import org.vertx.scala.core.FunctionConverters._
import org.vertx.java.core.file.{FileSystem => JFileSystem}

/**
 * @author swilliams
 * 
 */
object FileSystem {
  def apply(actual: JFileSystem) =
    new FileSystem(actual)
}

class FileSystem(internal: JFileSystem) {

  def chmod(path: String, perms: String, dirPerms: Option[String], handler: () => Unit):FileSystem ={
    internal.chmod(path, perms, dirPerms.getOrElse(null) , handler)
    this
  }

  def chmodSync(path: String, perms: String, dirPerms: Option[String]):FileSystem = {
    internal.chmodSync(path, perms, dirPerms.getOrElse(null))
    this
  }


  def copySync(from: String, to: String, recursive: Boolean = false):FileSystem = {
    internal.copySync(from, to, recursive)
    this
  }

  def copySync(path: String, perms: String, handler: () => Unit):FileSystem = {
    internal.createFile(path, perms, handler)
    this
  }

  def createFile(path: String, handler: () => Unit):FileSystem ={
    internal.createFile(path, handler)
    this
  }


  def createFileSync(path: String, perms: String = null):FileSystem = {
    internal.createFileSync(path, perms)
    this
  }

  def delete(path: String, recursive: Boolean, handler: () => Unit):FileSystem = {
    internal.delete(path, recursive, handler)
    this
  }

  def delete(path: String, handler: () => Unit):FileSystem = {
    delete(path, false, handler)
    this
  }

  def deleteSync(path: String):FileSystem = {
    internal.deleteSync(path)
    this
  }

  def deleteSync(path: String, recursive: Boolean):FileSystem = {
    internal.deleteSync(path, recursive)
    this
  }

  def exists(path: String, handler: (AsyncResult[java.lang.Boolean]) => Unit):FileSystem = {
    internal.exists(path, handler)
    this
  }

  def existsSync(path: String):Boolean =
    internal.existsSync(path)


  def fsProps(path: String, handler: (AsyncResult[FileSystemProps]) => Unit):FileSystem = {
    internal.fsProps(path, handler)
    this
  }


  def fsPropsSync(path: String):FileSystemProps =
    internal.fsPropsSync(path)

  def link(link: String, existing: String, handler: () => Unit):FileSystem = {
    internal.link(link, existing, handler)
    this
  }

  def linkSync(link: String, existing: String):FileSystem = {
    internal.linkSync(link, existing)
    this
  }

  def lprops(path: String, handler: (AsyncResult[FileProps]) => Unit):FileSystem = {
    internal.lprops(path, handler)
    this
  }

  def lpropsSync(path: String):FileProps =
    internal.lpropsSync(path)

  def mkdir(path: String, createParents: Boolean, handler: () => Unit):FileSystem = {
    internal.mkdir(path, createParents, handler)
    this
  }

  def mkdir(path: String, perms: String, createParents: Boolean, handler: () => Unit):FileSystem = {
    internal.mkdir(path, perms, createParents, handler)
    this
  }

  def mkdir(path: String, perms: String, handler: () => Unit):FileSystem = {
    internal.mkdir(path, perms, handler)
    this
  }

  def mkdir(path: String, handler: () => Unit):FileSystem = {
    internal.mkdir(path, handler)
    this
  }

  def mkdirSync(path: String):FileSystem = {
    internal.mkdirSync(path)
    this
  }

  def mkdirSync(path: String, createParents: Boolean):FileSystem = {
    internal.mkdirSync(path, createParents)
    this
  }

  def mkdirSync(path: String, perms: String):FileSystem = {
    internal.mkdirSync(path, perms)
    this
  }

  def mkdirSync(path: String, perms: String, createParents: Boolean):FileSystem = {
    internal.mkdirSync(path, perms, createParents)
    this
  }


  def moveSync(from: String, to: String):FileSystem = {
    internal.moveSync(from, to)
    this
  }


  def openSync(path: String):AsyncFile = {
    internal.openSync(path)
  }

  def openSync(path: String, perms: String):AsyncFile = {
    internal.openSync(path, perms)
  }

  def openSync(path: String, perms: String, createNew: Boolean):AsyncFile =  {
    internal.openSync(path, perms, createNew)
  }

  def openSync(path: String, perms: String, read: Boolean, write: Boolean, createNew: Boolean):AsyncFile =
    internal.openSync(path, perms, read, write, createNew)

  def openSync(path: String, perms: String, read: Boolean, write: Boolean, createNew: Boolean, flush: Boolean):AsyncFile =
    internal.openSync(path, perms, read, write, createNew, flush)


  def propsSync(path: String):FileProps = internal.propsSync(path)


  def readDirSync(path: String):Array[String] = internal.readDirSync(path)

  def readDirSync(path: String, filter: String):Array[String] = internal.readDirSync(path, filter)


  def readFile(path: String, handler: (AsyncResult[Buffer]) => Unit):FileSystem = {
    internal.readFile(path, handler)
    this
  }


  def readFileSync(path: String):Buffer =
    internal.readFileSync(path)

  def readSymlink(link: String, handler: (AsyncResult[String]) => Unit):FileSystem = {
    internal.readSymlink(link, handler)
    this
  }

  def readSymlink(link: String):String = internal.readSymlinkSync(link)


  def symlink(link: String, existing: String, handler: () => Unit):FileSystem = {
    internal.symlink(link, existing, handler)
    this
  }

  def symlinkSync(link: String, existing: String):FileSystem = {
    internal.symlinkSync(link, existing)
    this
  }

  def truncate(path: String, len: Int, handler: () => Unit):FileSystem = {
    internal.truncate(path, len, handler)
    this
  }

  def truncateSync(path: String, len: Int):FileSystem = {
    internal.truncateSync(path, len)
    this
  }

  def unlink(link: String, handler: () => Unit):FileSystem = {
    internal.unlink(link, handler)
    this
  }

  def unlinkSync(link: String):FileSystem =  {
    internal.unlinkSync(link)
    this
  }

  def writeFileSync(path: String, data: Buffer):FileSystem = {
    internal.writeFileSync(path, data)
    this
  }


  private def toAsync[T](h: AsyncResult[AsyncFile] => T)( f: JAsyncFile => AsyncFile ) = asyncHandler(h, f)
  private val wrapAsync = toAsync( _:AsyncResult[AsyncFile] => Unit)(j => AsyncFile.apply(j))


  def open(path: String, handler: (AsyncResult[AsyncFile]) => Unit):FileSystem ={
    internal.open(path, wrapAsync(handler))
    this
  }

  def open(path: String, perms: String, read: Boolean, write: Boolean, createNew: Boolean, flush: Boolean, handler: (AsyncResult[AsyncFile]) => Unit):FileSystem = {
    internal.open(path, perms, read, write, createNew, flush, wrapAsync(handler))
    this
  }

  def open(path: String, perms: String, read: Boolean, write: Boolean, createNew: Boolean, handler: (AsyncResult[AsyncFile]) => Unit):FileSystem = {
    internal.open(path, perms, read, write, createNew, wrapAsync(handler))
    this
  }

  def open(path: String, perms: String, createNew: Boolean, handler: (AsyncResult[AsyncFile]) => Unit):FileSystem = {
    internal.open(path, perms, createNew, wrapAsync(handler))
    this
  }

  def open(path: String, perms: String, handler: (AsyncResult[AsyncFile]) => Unit):FileSystem = {
    internal.open(path, perms, wrapAsync(handler))
    this
  }


  def props(path: String, handler: (AsyncResult[FileProps]) => Unit):FileSystem ={
    internal.props(path, handler)
    this
  }


  def readDir(path: String, filter: String, handler: (AsyncResult[Array[String]]) => Unit):FileSystem ={
    internal.readDir(path, filter, handler)
    this
  }

  def readDir(path: String, handler: (AsyncResult[Array[String]]) => Unit):FileSystem ={
    internal.readDir(path, handler)
    this
  }

  def move(from: String, to: String, handler: AsyncResult[Unit] => Unit):FileSystem ={
    internal.move(from, to, voidAsyncHandler(handler))
    this
  }

  def copy(from: String, to: String, handler: AsyncResult[Unit] => Unit):FileSystem ={
    internal.copy(from, to, voidAsyncHandler(handler))
    this
  }

  def writeFile(path: String, data: Buffer, handler: () => FileSystem):FileSystem ={
    internal.writeFile(path, data, new Handler[AsyncResult[Void]]() {
      override def handle(result: AsyncResult[Void]) = {
        handler()
      }
    })
    this
  }

  def writeFile(path:String, data:String, handler: () => FileSystem):FileSystem={
    writeFile(path, new org.vertx.java.core.buffer.Buffer(data), handler)
    this
  }

}