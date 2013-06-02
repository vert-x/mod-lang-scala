package org.vertx.scala.core

import org.vertx.java.core.{Vertx => JVertx}
import org.vertx.scala.core.http.{HttpClient, HttpServer, HttpServerRequest}

/**
 * Date: 6/1/13
 * Time: 1:18 AM
 * @author Edgar Chan
 */
object Vert_x {

  implicit class SVertx(val actual: JVertx) extends AnyVal {

    def newHttpServer(h: HttpServerRequest => Unit):HttpServer =
         HttpServer(actual.createHttpServer).requestHandler(h)

    def newHttpClient:HttpClient = HttpClient(actual.createHttpClient)

  }


}
