/*
 * Copyright 2011-2012 the original author or authors.
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

package org.vertx.scala.core

import org.vertx.java.core.buffer.Buffer
import org.vertx.scala.deploy.FunctionAsyncResultHandler0
import org.vertx.scala.deploy.FunctionAsyncResultHandler1
import org.vertx.java.core.file.FileProps
import org.vertx.java.core.file.AsyncFile
import org.vertx.java.core.AsyncResult
import org.vertx.java.core.file.FileSystemProps

class FileSystem(internal: org.vertx.java.core.file.FileSystem) {

  def chmod(path: String, perms: String, handler: () => Unit):Unit = {
    internal.chmod(path, perms, new FunctionAsyncResultHandler0(handler));
  }

  def chmod(path: String, perms: String, dirPerms: String, handler: () => Unit):Unit = {
    internal.chmod(path, perms, dirPerms, new FunctionAsyncResultHandler0(handler));
  }

  def chmodSync(path: String, perms: String):Unit = {
    internal.chmodSync(path, perms)
  }

  def chmodSync(path: String, perms: String, dirPerms: String):Unit = {
    internal.chmodSync(path, perms, dirPerms)
  }

  def copy(from: String, to: String, handler: () => Unit):Unit = {
    internal.copy(from, to, new FunctionAsyncResultHandler0(handler))
  }

  def copy(from: String, to: String, recursive: Boolean, handler: () => Unit):Unit = {
    internal.copy(from, to, recursive, new FunctionAsyncResultHandler0(handler))
  }

  def copySync(from: String, to: String):Unit = {
    internal.copySync(from, to)
  }

  def copySync(from: String, to: String, recursive: Boolean):Unit = {
    internal.copySync(from, to, recursive)
  }

  def copySync(path: String, perms: String, handler: () => Unit):Unit = {
    internal.createFile(path, perms, new FunctionAsyncResultHandler0(handler))
  }

  def createFile(path: String, handler: () => Unit):Unit = {
    internal.createFile(path, new FunctionAsyncResultHandler0(handler))
  }

  def createFileSync(path: String):Unit = {
    internal.createFileSync(path)
  }

  def createFileSync(path: String, perms: String):Unit = {
    internal.createFileSync(path, perms)
  }

  def delete(path: String, recursive: Boolean, handler: () => Unit):Unit = {
    internal.delete(path, recursive, new FunctionAsyncResultHandler0(handler))
  }

  def delete(path: String, handler: () => Unit):Unit = {
    internal.delete(path, new FunctionAsyncResultHandler0(handler))
  }

  def deleteSync(path: String):Unit = {
    internal.deleteSync(path)
  }

  def deleteSync(path: String, recursive: Boolean):Unit = {
    internal.deleteSync(path, recursive)
  }

  def exists(path: String, handler: (AsyncResult[java.lang.Boolean]) => Unit):Unit = {
    internal.exists(path, new FunctionAsyncResultHandler1(handler))
  }

  def existsSync(path: String):Unit = {
    internal.existsSync(path)
  }

  def fsProps(path: String, handler: (AsyncResult[FileSystemProps]) => Unit):Unit = {
    internal.fsProps(path, new FunctionAsyncResultHandler1(handler))
  }

  def fsPropsSync(path: String):Unit = {
    internal.fsPropsSync(path)
  }

  def link(link: String, existing: String, handler: () => Unit):Unit = {
    internal.link(link, existing, new FunctionAsyncResultHandler0(handler))
  }

  def linkSync(link: String, existing: String):Unit = {
    internal.linkSync(link, existing)
  }

  def lprops(path: String, handler: (AsyncResult[FileProps]) => Unit):Unit = {
    internal.lprops(path, new FunctionAsyncResultHandler1(handler))
  }

  def lpropsSync(path: String):Unit = {
    internal.lpropsSync(path)
  }

  def mkdir(path: String, createParents: Boolean, handler: () => Unit):Unit = {
    internal.mkdir(path, createParents, new FunctionAsyncResultHandler0(handler))
  }

  def mkdir(path: String, perms: String, createParents: Boolean, handler: () => Unit):Unit = {
    internal.mkdir(path, perms, createParents, new FunctionAsyncResultHandler0(handler))
  }

  def mkdir(path: String, perms: String, handler: () => Unit):Unit = {
    internal.mkdir(path, perms, new FunctionAsyncResultHandler0(handler))
  }

  def mkdir(path: String, handler: () => Unit):Unit = {
    internal.mkdir(path, new FunctionAsyncResultHandler0(handler))
  }

  def mkdirSync(path: String):Unit = {
    internal.mkdirSync(path)
  }

  def mkdirSync(path: String, createParents: Boolean):Unit = {
    internal.mkdirSync(path, createParents)
  }

  def mkdirSync(path: String, perms: String):Unit = {
    internal.mkdirSync(path, perms)
  }

  def mkdirSync(path: String, perms: String, createParents: Boolean):Unit = {
    internal.mkdirSync(path, perms, createParents)
  }

  def move(from: String, to: String, handler: () => Unit):Unit = {
    internal.move(from, to, new FunctionAsyncResultHandler0(handler))
  }

  def moveSync(from: String, to: String):Unit = {
    internal.moveSync(from, to)
  }

  def open(path: String, perms: String, read: Boolean, write: Boolean, createNew: Boolean, flush: Boolean, handler: (AsyncResult[AsyncFile]) => Unit):Unit = {
    internal.open(path, perms, read, write, createNew, flush, new FunctionAsyncResultHandler1(handler))
  }

  def open(path: String, perms: String, read: Boolean, write: Boolean, createNew: Boolean, handler: (AsyncResult[AsyncFile]) => Unit):Unit = {
    internal.open(path, perms, read, write, createNew, new FunctionAsyncResultHandler1(handler))
  }

  def open(path: String, perms: String, createNew: Boolean, handler: (AsyncResult[AsyncFile]) => Unit):Unit = {
    internal.open(path, perms, createNew, new FunctionAsyncResultHandler1(handler))
  }

  def open(path: String, perms: String, handler: (AsyncResult[AsyncFile]) => Unit):Unit = {
    internal.open(path, perms, new FunctionAsyncResultHandler1(handler))
  }

  def open(path: String, handler: (AsyncResult[AsyncFile]) => Unit):Unit = {
    internal.open(path, new FunctionAsyncResultHandler1(handler))
  }

  def openSync(path: String):AsyncFile = {
    internal.openSync(path)
  }

  def openSync(path: String, perms: String):AsyncFile = {
    internal.openSync(path, perms)
  }

  def openSync(path: String, perms: String, createNew: Boolean):AsyncFile = {
    internal.openSync(path, perms, createNew)
  }

  def openSync(path: String, perms: String, read: Boolean, write: Boolean, createNew: Boolean):AsyncFile = {
    internal.openSync(path, perms, read, write, createNew)
  }

  def openSync(path: String, perms: String, read: Boolean, write: Boolean, createNew: Boolean, flush: Boolean):AsyncFile = {
    internal.openSync(path, perms, read, write, createNew, flush)
  }

  def props(path: String, handler: (AsyncResult[FileProps]) => Unit):Unit = {
    internal.props(path, new FunctionAsyncResultHandler1(handler))
  }

  def propsSync(path: String):FileProps = {
    internal.propsSync(path)
  }

  def readDir(path: String, filter: String, handler: (AsyncResult[Array[String]]) => Unit):Unit = {
    internal.readDir(path, filter, new FunctionAsyncResultHandler1(handler))
  }

  def readDir(path: String, handler: (AsyncResult[Array[String]]) => Unit):Unit = {
    internal.readDir(path, new FunctionAsyncResultHandler1(handler))
  }

  def readDirSync(path: String):Array[String] = {
    internal.readDirSync(path)
  }

  def readDirSync(path: String, filter: String):Array[String] = {
    internal.readDirSync(path, filter)
  }

  def readFile(path: String, handler: (AsyncResult[Buffer]) => Unit):Unit = {
    internal.readFile(path, new FunctionAsyncResultHandler1(handler))
  }

  def readFileSync(path: String):Buffer = {
    internal.readFileSync(path)
  }

  def readSymlink(link: String, handler: (AsyncResult[String]) => Unit):Unit = {
    internal.readSymlink(link, new FunctionAsyncResultHandler1(handler))
  }

  def readSymlink(link: String):String = {
    internal.readSymlinkSync(link)
  }

  def symlink(link: String, existing: String, handler: () => Unit):Unit = {
    internal.symlink(link, existing, new FunctionAsyncResultHandler0(handler))
  }

  def symlinkSync(link: String, existing: String):Unit = {
    internal.symlinkSync(link, existing)
  }

  def truncate(path: String, len: Int, handler: () => Unit):Unit = {
    internal.truncate(path, len, new FunctionAsyncResultHandler0(handler))
  }

  def truncateSync(path: String, len: Int):Unit = {
    internal.truncateSync(path, len)
  }

  def unlink(link: String, handler: () => Unit):Unit = {
    internal.unlink(link, new FunctionAsyncResultHandler0(handler))
  }

  def unlinkSync(link: String):Unit = {
    internal.unlinkSync(link)
  }

  def writeFile(path: String, data: Buffer, handler: () => Unit):Unit = {
    internal.writeFile(path, data, new FunctionAsyncResultHandler0(handler))
  }

  def writeFileSync(path: String, data: Buffer):Unit = {
    internal.writeFileSync(path, data)
  }

}