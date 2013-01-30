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

package org.vertx.scala.http

import org.vertx.java.core.Handler
import org.vertx.java.core.http.{RouteMatcher => JRouteMatcher}
import org.vertx.java.core.http.{HttpServerRequest => JHttpServerRequest}
import org.vertx.scala.FunctionConverters._

/**
 * @author swilliams
 * 
 */
class RouteMatcher extends Handler[HttpServerRequest] {

  private val internal = new JRouteMatcher()

  def all(uri: String, handler: (HttpServerRequest) => Unit):Unit = {
    internal.all(uri, HttpServerRequestHandler(handler))
  }

  def allWithRegEx(regex: String, handler: (HttpServerRequest) => Unit):Unit = {
    internal.allWithRegEx(regex, HttpServerRequestHandler(handler))
  }

  def connect(uri: String, handler: (HttpServerRequest) => Unit):Unit = {
    internal.connect(uri, HttpServerRequestHandler(handler))
  }

  def connectWithRegEx(regex: String, handler: (HttpServerRequest) => Unit):Unit = {
    internal.connectWithRegEx(regex, HttpServerRequestHandler(handler))
  }

  def delete(uri: String, handler: (HttpServerRequest) => Unit):Unit = {
    internal.delete(uri, HttpServerRequestHandler(handler))
  }

  def deleteWithRegEx(regex: String, handler: (HttpServerRequest) => Unit):Unit = {
    internal.deleteWithRegEx(regex, HttpServerRequestHandler(handler))
  }

  def get(uri: String, handler: (HttpServerRequest) => Unit):Unit = {
    internal.get(uri, HttpServerRequestHandler(handler))
  }

  def getWithRegEx(regex: String, handler: (HttpServerRequest) => Unit):Unit = {
    internal.getWithRegEx(regex, HttpServerRequestHandler(handler))
  }

  def handle(request: HttpServerRequest):Unit = {
    internal.handle(request.internal) // FIXME reverse handler?
  }

  def head(uri: String, handler: (HttpServerRequest) => Unit):Unit = {
    internal.head(uri, HttpServerRequestHandler(handler))
  }

  def headWithRegEx(regex: String, handler: (HttpServerRequest) => Unit):Unit = {
    internal.headWithRegEx(regex, HttpServerRequestHandler(handler))
  }

  def options(uri: String, handler: (HttpServerRequest) => Unit):Unit = {
    internal.options(uri, HttpServerRequestHandler(handler))
  }

  def optionsWithRegEx(regex: String, handler: (HttpServerRequest) => Unit):Unit = {
    internal.optionsWithRegEx(regex, HttpServerRequestHandler(handler))
  }

  def patch(uri: String, handler: (HttpServerRequest) => Unit):Unit = {
    internal.patch(uri, HttpServerRequestHandler(handler))
  }

  def patchWithRegEx(regex: String, handler: (HttpServerRequest) => Unit):Unit = {
    internal.patchWithRegEx(regex, HttpServerRequestHandler(handler))
  }

  def post(uri: String, handler: (HttpServerRequest) => Unit):Unit = {
    internal.post(uri, HttpServerRequestHandler(handler))
  }

  def postWithRegEx(regex: String, handler: (HttpServerRequest) => Unit):Unit = {
    internal.postWithRegEx(regex, HttpServerRequestHandler(handler))
  }

  def put(uri: String, handler: (HttpServerRequest) => Unit):Unit = {
    internal.put(uri, HttpServerRequestHandler(handler))
  }

  def putWithRegEx(regex: String, handler: (HttpServerRequest) => Unit):Unit = {
    internal.putWithRegEx(regex, HttpServerRequestHandler(handler))
  }

  def trace(uri: String, handler: (HttpServerRequest) => Unit):Unit = {
    internal.trace(uri, HttpServerRequestHandler(handler))
  }

  def traceWithRegEx(regex: String, handler: (HttpServerRequest) => Unit):Unit = {
    internal.traceWithRegEx(regex, HttpServerRequestHandler(handler))
  }

}