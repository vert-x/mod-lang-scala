package org.vertx.scala.core.http

import org.vertx.java.core.http.{RouteMatcher => JRouteMatcher}
import org.vertx.java.core.http.HttpServerRequest
import org.vertx.scala.handlers.FunctionHandler1
import org.vertx.java.core.Handler
import org.vertx.java.core.http.{RouteMatcher => JRouteMatcher}

class RouteMatcher(delegate: (HttpServerRequest) => Unit) extends Handler[HttpServerRequest] {

  private val internal:JRouteMatcher = new JRouteMatcher()

  def all(uri: String, handler: (HttpServerRequest) => Unit):Unit = {
    internal.all(uri, FunctionHandler1(handler))
  }

  def allWithRegEx(regex: String, handler: (HttpServerRequest) => Unit):Unit = {
    internal.allWithRegEx(regex, FunctionHandler1(handler))
  }

  def connect(uri: String, handler: (HttpServerRequest) => Unit):Unit = {
    internal.connect(uri, FunctionHandler1(handler))
  }

  def connectWithRegEx(regex: String, handler: (HttpServerRequest) => Unit):Unit = {
    internal.connectWithRegEx(regex, FunctionHandler1(handler))
  }

  def delete(uri: String, handler: (HttpServerRequest) => Unit):Unit = {
    internal.delete(uri, FunctionHandler1(handler))
  }

  def deleteWithRegEx(regex: String, handler: (HttpServerRequest) => Unit):Unit = {
    internal.deleteWithRegEx(regex, FunctionHandler1(handler))
  }

  def get(uri: String, handler: (HttpServerRequest) => Unit):Unit = {
    internal.get(uri, FunctionHandler1(handler))
  }

  def getWithRegEx(regex: String, handler: (HttpServerRequest) => Unit):Unit = {
    internal.getWithRegEx(regex, FunctionHandler1(handler))
  }

  def handle(message: HttpServerRequest):Unit = {
    internal.handle(message) // FIXME reverse handler?
  }

  def head(uri: String, handler: (HttpServerRequest) => Unit):Unit = {
    internal.head(uri, FunctionHandler1(handler))
  }

  def headWithRegEx(regex: String, handler: (HttpServerRequest) => Unit):Unit = {
    internal.headWithRegEx(regex, FunctionHandler1(handler))
  }

  def options(uri: String, handler: (HttpServerRequest) => Unit):Unit = {
    internal.options(uri, FunctionHandler1(handler))
  }

  def optionsWithRegEx(regex: String, handler: (HttpServerRequest) => Unit):Unit = {
    internal.optionsWithRegEx(regex, FunctionHandler1(handler))
  }

  def patch(uri: String, handler: (HttpServerRequest) => Unit):Unit = {
    internal.patch(uri, FunctionHandler1(handler))
  }

  def patchWithRegEx(regex: String, handler: (HttpServerRequest) => Unit):Unit = {
    internal.patchWithRegEx(regex, FunctionHandler1(handler))
  }

  def post(uri: String, handler: (HttpServerRequest) => Unit):Unit = {
    internal.post(uri, FunctionHandler1(handler))
  }

  def postWithRegEx(regex: String, handler: (HttpServerRequest) => Unit):Unit = {
    internal.postWithRegEx(regex, FunctionHandler1(handler))
  }

  def put(uri: String, handler: (HttpServerRequest) => Unit):Unit = {
    internal.put(uri, FunctionHandler1(handler))
  }

  def putWithRegEx(regex: String, handler: (HttpServerRequest) => Unit):Unit = {
    internal.putWithRegEx(regex, FunctionHandler1(handler))
  }

  def trace(uri: String, handler: (HttpServerRequest) => Unit):Unit = {
    internal.trace(uri, FunctionHandler1(handler))
  }

  def traceWithRegEx(regex: String, handler: (HttpServerRequest) => Unit):Unit = {
    internal.traceWithRegEx(regex, FunctionHandler1(handler))
  }

}