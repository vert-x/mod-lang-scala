package http

import org.vertx.scala.deploy.Verticle
import org.vertx.scala.Vertx
import org.vertx.scala.http.HttpServer
import org.vertx.scala.http.HttpServerRequest
import org.vertx.java.core.Handler
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit


class HelloWorldHttpServer extends Verticle {

  var http: HttpServer = null

  @throws(classOf[Exception])
  override def start(): Unit = {
    http = vertx.createHttpServer.requestHandler({ req: HttpServerRequest =>
      req.response.end("Hello World (direct write)")
    }).listen(8080)
  }

  @throws(classOf[Exception])
  override def stop(): Unit = {
    def latch = new CountDownLatch(1)
    http.close(() => latch.countDown())
    latch.await(5000L, TimeUnit.MILLISECONDS)
  }

}