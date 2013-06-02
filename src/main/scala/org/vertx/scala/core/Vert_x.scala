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

import org.vertx.java.core.{Vertx => JVertx}
import org.vertx.java.core.{VertxFactory => JVertxFactory}
import org.vertx.scala.core.http.{HttpClient, HttpServer, HttpServerRequest}
import org.vertx.scala.core.net.{NetServer, NetClient}
import org.vertx.scala.core.sockjs.SockJSServer
import org.vertx.scala.core.file.FileSystem
import org.vertx.scala.core.FunctionConverters._
import org.vertx.scala.core.eventbus.EventBus

/**
 * Date: 6/1/13
 * @author Edgar Chan
 */
object Vert_x {

  def newVertx() =
    new SVertx(JVertxFactory.newVertx())

  def newVertx(port: Int, hostname: String) =
    new SVertx(JVertxFactory.newVertx(port, hostname))

  def newVertx(hostname: String) =
    new SVertx(JVertxFactory.newVertx(hostname))



  implicit class SVertx(val internal: JVertx) extends AnyVal {

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

    def fileSystem:FileSystem = FileSystem(internal.fileSystem)

    def isEventLoop:Boolean = internal.isEventLoop

    def isWorker:Boolean = internal.isWorker

    def runOnLoop(handler: () => Unit):Unit = internal.runOnLoop(handler)

    def periodic(delay: Long)(handler: (java.lang.Long) => Unit):Unit = internal.setPeriodic(delay, handler)

    def timer(delay: Long)(handler: (java.lang.Long) => Unit):Unit = internal.setTimer(delay, handler)

    def sharedData:SharedData = SharedData(internal.sharedData)


  }


}
