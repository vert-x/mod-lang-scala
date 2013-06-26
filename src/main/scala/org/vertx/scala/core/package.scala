/*
 * Copyright 2013 the original author or authors.
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

package org.vertx.scala

import org.vertx.scala.core.eventbus.EventBus
import org.vertx.scala.core.http.{HttpClient, HttpServerRequest, HttpServer}
import org.vertx.scala.core.net.{NetServer, NetClient}
import org.vertx.scala.core.sockjs.SockJSServer
import org.vertx.scala.core.file.FileSystem
import org.vertx.java.core.{Vertx => JVertx}
import org.vertx.java.core.{VertxFactory => JVertxFactory}
import org.vertx.scala.core.FunctionConverters._

package object core {

  def newVertx() =
    new Vertx(JVertxFactory.newVertx())

  def newVertx(port: Int, hostname: String) =
    new Vertx(JVertxFactory.newVertx(port, hostname))

  def newVertx(hostname: String) =
    new Vertx(JVertxFactory.newVertx(hostname))



  implicit class Vertx(val internal: JVertx) extends AnyVal {

  // TODO no vals allowed in value classes. do we need to make it val?
  // val eventBus:EventBus = EventBus(internal.eventBus)

    def eventBus:EventBus = EventBus(internal.eventBus)

    def cancelTimer(id: Long):Boolean = internal.cancelTimer(id)

    def newHttpServer:HttpServer = HttpServer(internal.createHttpServer)

    def newHttpServer(h: HttpServerRequest => Unit):HttpServer =
         HttpServer(internal.createHttpServer).requestHandler(h)

    def newHttpClient:HttpClient = HttpClient(internal.createHttpClient)

    def newNetClient:NetClient = NetClient(internal.createNetClient)

    def newNetServer:NetServer = NetServer(internal.createNetServer)

    def newSockJSServer(httpServer: HttpServer):SockJSServer = SockJSServer(internal.createSockJSServer(httpServer.actual))

    def newFileSystem:FileSystem = FileSystem(internal.fileSystem)

    def isEventLoop:Boolean = internal.isEventLoop

    def isWorker:Boolean = internal.isWorker

    def runOnLoop(handler: () => Unit):Unit = internal.runOnLoop(handler)

    def periodic(delay: Long)(handler: (java.lang.Long) => Unit):Unit = internal.setPeriodic(delay, handler)

    def timer(delay: Long)(handler: (java.lang.Long) => Unit):Unit = internal.setTimer(delay, handler)

    def sharedData:SharedData = SharedData(internal.sharedData)


  }


}
