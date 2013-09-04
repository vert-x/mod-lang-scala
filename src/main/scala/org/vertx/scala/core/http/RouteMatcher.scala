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

package org.vertx.scala.core.http

import org.vertx.java.core.Handler
import org.vertx.java.core.http.{ RouteMatcher => JRouteMatcher }
import org.vertx.java.core.http.{ HttpServerRequest => JHttpServerRequest }
import org.vertx.scala.core.FunctionConverters._
import org.vertx.scala.Wrap

/**
 * @author swilliams
 * @author <a href="http://www.campudus.com/">Joern Bernhardt</a>
 */
class RouteMatcher(val internal: JRouteMatcher = new JRouteMatcher()) extends Handler[HttpServerRequest] with (HttpServerRequest => Unit) with Wrap {

  def all(uri: String)(handler: JHttpServerRequest => Unit) = wrap(internal.all(uri, handler))

  def allWithRegEx(regex: String)(handler: JHttpServerRequest => Unit) = wrap(internal.allWithRegEx(regex, handler))

  def apply(request: HttpServerRequest) = wrap(handle(request))

  def connect(uri: String)(handler: JHttpServerRequest => Unit) = wrap(internal.connect(uri, handler))

  def connectWithRegEx(regex: String)(handler: JHttpServerRequest => Unit) =
    wrap(internal.connectWithRegEx(regex, handler))

  def delete(uri: String)(handler: JHttpServerRequest => Unit) = wrap(internal.delete(uri, handler))

  def deleteWithRegEx(regex: String)(handler: JHttpServerRequest => Unit) =
    wrap(internal.deleteWithRegEx(regex, handler))

  def get(uri: String)(handler: JHttpServerRequest => Unit) = internal.get(uri, handler)

  def getWithRegEx(regex: String)(handler: JHttpServerRequest => Unit) =
    wrap(internal.getWithRegEx(regex, handler))

  def handle(request: HttpServerRequest): Unit = internal.handle(request.internal)

  def head(uri: String)(handler: JHttpServerRequest => Unit) = wrap(internal.head(uri, handler))

  def headWithRegEx(regex: String)(handler: JHttpServerRequest => Unit) =
    wrap(internal.headWithRegEx(regex, handler))

  def options(uri: String)(handler: JHttpServerRequest => Unit) = wrap(internal.options(uri, handler))

  def optionsWithRegEx(regex: String)(handler: JHttpServerRequest => Unit) =
    wrap(internal.optionsWithRegEx(regex, handler))

  def patch(uri: String)(handler: JHttpServerRequest => Unit) = wrap(internal.patch(uri, handler))

  def patchWithRegEx(regex: String)(handler: JHttpServerRequest => Unit) =
    wrap(internal.patchWithRegEx(regex, handler))

  def post(uri: String)(handler: JHttpServerRequest => Unit) = wrap(internal.post(uri, handler))

  def postWithRegEx(regex: String)(handler: JHttpServerRequest => Unit) =
    wrap(internal.postWithRegEx(regex, handler))

  def put(uri: String)(handler: JHttpServerRequest => Unit) = wrap(internal.put(uri, handler))

  def putWithRegEx(regex: String)(handler: JHttpServerRequest => Unit) =
    wrap(internal.putWithRegEx(regex, handler))

  def trace(uri: String)(handler: JHttpServerRequest => Unit) = wrap(internal.trace(uri, handler))

  def traceWithRegEx(regex: String)(handler: JHttpServerRequest => Unit) =
    wrap(internal.traceWithRegEx(regex, handler))

}