package org.vertx.scala.core

import org.vertx.java.core.http.{RouteMatcher => JRouteMatcher}
import org.vertx.java.core.http.HttpServerRequest
import org.vertx.scala.handlers.FunctionHandler1

class RouteMatcher(internal: JRouteMatcher) {

  def getWithRegex(regex: String, handler: (HttpServerRequest) => Unit):Unit = {
    internal.getWithRegEx(regex, FunctionHandler1(handler))
  }

}