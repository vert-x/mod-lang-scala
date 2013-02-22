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

package org.vertx.scala.core

import scala.language.implicitConversions
import org.vertx.java.core.{Vertx => JVertx}
import org.vertx.scala.core.FunctionConverters._
import org.vertx.scala.core.net.NetClient
import org.vertx.scala.core.net.NetServer
import org.vertx.scala.core.http.HttpClient
import org.vertx.scala.core.http.HttpServer
import org.vertx.scala.core.sockjs.SockJSServer


/**
 * @author swilliams
 * 
 */
object Vertx {
  def apply(actual: JVertx) =
    new Vertx(actual)

  def newVertx() =
    new Vertx(JVertx.newVertx())

  def newVertx(port: Int, hostname: String) =
    new Vertx(JVertx.newVertx(port, hostname))

  def newVertx(hostname: String) =
    new Vertx(JVertx.newVertx(hostname))
}

class Vertx(val internal: JVertx) {

  implicit def convertJavaToScalaVertx(jvertx: JVertx):Vertx = Vertx(jvertx)

  implicit def convertScalaToJavaVertx(vertx: Vertx):JVertx = vertx.internal

  val eventBus:EventBus = EventBus(internal.eventBus)

  def cancelTimer(id: Long):Boolean = internal.cancelTimer(id)

  def createHttpClient():HttpClient = HttpClient(internal.createHttpClient)

  def createHttpServer():HttpServer = HttpServer(internal.createHttpServer)

  def createNetClient():NetClient = NetClient(internal.createNetClient)

  def createNetServer():NetServer = NetServer(internal.createNetServer)

  def createSockJSServer(httpServer: HttpServer):SockJSServer = SockJSServer(internal.createSockJSServer(httpServer.actual))

  def fileSystem():FileSystem = FileSystem(internal.fileSystem)

  def isEventLoop():Boolean = internal.isEventLoop

  def isWorker():Boolean = internal.isWorker

  def runOnLoop(handler: () => Unit):Unit = internal.runOnLoop(handler)

  def periodic(delay: Long)(handler: (java.lang.Long) => Unit):Unit = internal.setPeriodic(delay, handler)

  def timer(delay: Long)(handler: (java.lang.Long) => Unit):Unit = internal.setTimer(delay, handler)

  def sharedData():SharedData = SharedData(internal.sharedData)

}