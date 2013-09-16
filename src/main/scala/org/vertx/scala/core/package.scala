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

import org.vertx.java.core.{ Vertx => JVertx }
import org.vertx.java.core.{ VertxFactory => JVertxFactory }
import org.vertx.java.core.AsyncResult
import org.vertx.java.core.{ Future => JFuture }
import org.vertx.scala.core.eventbus.EventBus
import org.vertx.scala.core.http.{ HttpClient, HttpServerRequest, HttpServer }
import org.vertx.scala.core.net.{ NetServer, NetClient }
import org.vertx.scala.core.sockjs.SockJSServer
import org.vertx.scala.core.file.FileSystem
import org.vertx.scala.core.FunctionConverters._

package object core {

  def newVertx() = new Vertx(JVertxFactory.newVertx())

  def newVertx(port: Int, hostname: String) = new Vertx(JVertxFactory.newVertx(port, hostname))

  def newVertx(hostname: String) = new Vertx(JVertxFactory.newVertx(hostname))

  implicit class Vertx(val internal: JVertx) extends AnyVal {

    def eventBus: EventBus = EventBus(internal.eventBus)

    def cancelTimer(id: Long): Boolean = internal.cancelTimer(id)

    def createHttpServer(): HttpServer = HttpServer(internal.createHttpServer)

    def createHttpClient(): HttpClient = HttpClient(internal.createHttpClient)

    def createNetClient(): NetClient = NetClient(internal.createNetClient)

    def createNetServer(): NetServer = NetServer(internal.createNetServer)

    def fileSystem: FileSystem = FileSystem(internal.fileSystem)

    def isEventLoop: Boolean = internal.isEventLoop

    def isWorker: Boolean = internal.isWorker

    def runOnLoop(handler: () => Unit): Unit = internal.runOnLoop(handler)

    def periodic(delay: Long)(handler: (java.lang.Long) => Unit): Unit = internal.setPeriodic(delay, handler)

    def timer(delay: Long)(handler: (java.lang.Long) => Unit): Unit = internal.setTimer(delay, handler)

    def sharedData: SharedData = SharedData(internal.sharedData)

  }

  implicit class Future[T](internal: JFuture[T]) extends AsyncResult[T] {

    def complete(): Boolean = internal.complete()

    def setHandler(handler: AsyncResult[T] => Unit): Future[T] = {
      internal.setHandler(handler)
      this
    }

    def setFailure(cause: Throwable): Future[T] = {
      internal.setFailure(cause)
      this
    }

    def setResult(result: T): Future[T] = {
      internal.setResult(result)
      this
    }

    override def result(): T = internal.result()

    override def cause(): Throwable = internal.cause()

    override def succeeded(): Boolean = internal.succeeded()

    override def failed(): Boolean = internal.failed()

  }

}
