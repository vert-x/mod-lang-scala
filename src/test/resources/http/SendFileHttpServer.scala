/*
 * Copyright 2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package http

import org.vertx.scala.deploy.Verticle
import org.vertx.scala.Vertx
import org.vertx.scala.http.HttpServer
import org.vertx.scala.http.HttpServerRequest
import org.vertx.java.core.Handler
import org.vertx.scala.handlers.FunctionHandler1
import org.vertx.scala.handlers.FunctionHandler0
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.net.URLClassLoader


class SendFileHttpServer extends Verticle {

  var http: HttpServer = null

  @throws(classOf[Exception])
  override def start(): Unit = {
    http = vertx.createHttpServer.requestHandler({ req: HttpServerRequest =>
      req.response.sendFile("helloscala.txt")
    }).listen(8080)
  }

  @throws(classOf[Exception])
  override def stop(): Unit = {
    def latch = new CountDownLatch(1)
    http.close(() => latch.countDown())
    latch.await(5000L, TimeUnit.MILLISECONDS)
  }

}