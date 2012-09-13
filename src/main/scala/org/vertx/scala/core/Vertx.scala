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

import org.vertx.scala.deploy.FunctionHandler0
import org.vertx.scala.deploy.FunctionHandler1


class Vertx(internal: org.vertx.java.core.Vertx) {

  val eventBus:EventBus = new EventBus(internal.eventBus())

  def cancelTimer(id: Long):Boolean = {
    internal.cancelTimer(id)
  }

  def createHttpClient():HttpClient = {
    new HttpClient(internal.createHttpClient())
  }

  def createHttpServer():HttpServer = {
    new HttpServer(internal.createHttpServer())
  }

  def createNetClient():NetClient = {
    new NetClient(internal.createNetClient())
  }

  def createNetServer():NetServer = {
    new NetServer(internal.createNetServer())
  }

  def createSockJSServer(httpServer: HttpServer):SockJSServer = {
    new SockJSServer(internal.createSockJSServer(httpServer.internal()))
  }

  def fileSystem():FileSystem = {
    new FileSystem(internal.fileSystem())
  }

  def eventLoop():Boolean = {
    internal.isEventLoop()
  }

  def worker():Boolean = {
    internal.isWorker()
  }

  def runOnLoop(handler: () => Unit):Unit = {
    internal.runOnLoop(new FunctionHandler0(handler))
  }
  def periodic(delay: Long, handler: (java.lang.Long) => Unit):Unit = {
    internal.setPeriodic(delay, new FunctionHandler1(handler))
  }

  def timer(delay: Long, handler: (java.lang.Long) => Unit):Unit = {
    internal.setTimer(delay, new FunctionHandler1(handler))
  }

  def sharedData():SharedData = {
    new SharedData(internal.sharedData())
  }

}