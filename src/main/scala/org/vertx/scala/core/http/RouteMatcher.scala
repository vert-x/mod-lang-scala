package org.vertx.scala.core.http

import org.vertx.java.core.Handler
import org.vertx.java.core.http.{RouteMatcher => JRouteMatcher}
import org.vertx.java.core.http.{HttpServerRequest => JHttpServerRequest}
import org.vertx.scala.handlers.FunctionHandler1

class RouteMatcher extends Handler[HttpServerRequest] {

  private val internal = new JRouteMatcher()

  def all(uri: String, handler: (HttpServerRequest) => Unit):Unit = {
    internal.all(uri, HttpServerRequestHandler1(handler))
  }

  def allWithRegEx(regex: String, handler: (HttpServerRequest) => Unit):Unit = {
    internal.allWithRegEx(regex, HttpServerRequestHandler1(handler))
  }

  def connect(uri: String, handler: (HttpServerRequest) => Unit):Unit = {
    internal.connect(uri, HttpServerRequestHandler1(handler))
  }

  def connectWithRegEx(regex: String, handler: (HttpServerRequest) => Unit):Unit = {
    internal.connectWithRegEx(regex, HttpServerRequestHandler1(handler))
  }

  def delete(uri: String, handler: (HttpServerRequest) => Unit):Unit = {
    internal.delete(uri, HttpServerRequestHandler1(handler))
  }

  def deleteWithRegEx(regex: String, handler: (HttpServerRequest) => Unit):Unit = {
    internal.deleteWithRegEx(regex, HttpServerRequestHandler1(handler))
  }

  def get(uri: String, handler: (HttpServerRequest) => Unit):Unit = {
    internal.get(uri, HttpServerRequestHandler1(handler))
  }

  def getWithRegEx(regex: String, handler: (HttpServerRequest) => Unit):Unit = {
    internal.getWithRegEx(regex, HttpServerRequestHandler1(handler))
  }

  def handle(request: HttpServerRequest):Unit = {
    internal.handle(request.internal) // FIXME reverse handler?
  }

  def head(uri: String, handler: (HttpServerRequest) => Unit):Unit = {
    internal.head(uri, HttpServerRequestHandler1(handler))
  }

  def headWithRegEx(regex: String, handler: (HttpServerRequest) => Unit):Unit = {
    internal.headWithRegEx(regex, HttpServerRequestHandler1(handler))
  }

  def options(uri: String, handler: (HttpServerRequest) => Unit):Unit = {
    internal.options(uri, HttpServerRequestHandler1(handler))
  }

  def optionsWithRegEx(regex: String, handler: (HttpServerRequest) => Unit):Unit = {
    internal.optionsWithRegEx(regex, HttpServerRequestHandler1(handler))
  }

  def patch(uri: String, handler: (HttpServerRequest) => Unit):Unit = {
    internal.patch(uri, HttpServerRequestHandler1(handler))
  }

  def patchWithRegEx(regex: String, handler: (HttpServerRequest) => Unit):Unit = {
    internal.patchWithRegEx(regex, HttpServerRequestHandler1(handler))
  }

  def post(uri: String, handler: (HttpServerRequest) => Unit):Unit = {
    internal.post(uri, HttpServerRequestHandler1(handler))
  }

  def postWithRegEx(regex: String, handler: (HttpServerRequest) => Unit):Unit = {
    internal.postWithRegEx(regex, HttpServerRequestHandler1(handler))
  }

  def put(uri: String, handler: (HttpServerRequest) => Unit):Unit = {
    internal.put(uri, HttpServerRequestHandler1(handler))
  }

  def putWithRegEx(regex: String, handler: (HttpServerRequest) => Unit):Unit = {
    internal.putWithRegEx(regex, HttpServerRequestHandler1(handler))
  }

  def trace(uri: String, handler: (HttpServerRequest) => Unit):Unit = {
    internal.trace(uri, HttpServerRequestHandler1(handler))
  }

  def traceWithRegEx(regex: String, handler: (HttpServerRequest) => Unit):Unit = {
    internal.traceWithRegEx(regex, HttpServerRequestHandler1(handler))
  }

}