package org.vertx.scala.http

import org.vertx.java.core.http.{HttpClientResponse => JHttpClientResponse}

import org.vertx.java.core.Handler

object HttpClientResponseHandler1 {
  def apply(response: (HttpClientResponse) => Unit) = 
    new HttpClientResponseHandler1(response)
}


class HttpClientResponseHandler1(response: (HttpClientResponse) => Unit) extends Handler[JHttpClientResponse]  {

  implicit def convertJavaToScala(jres: JHttpClientResponse):HttpClientResponse = {
    HttpClientResponse(jres)
  }

  def handle(jres: JHttpClientResponse) {
    response(jres)
  }

}