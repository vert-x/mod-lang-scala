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
import org.vertx.scala.core.FunctionConverters._
import org.vertx.scala.Self

/**
 * Not sure whether this kind of RouteMatcher should stay in Scala...
 *
 * @author swilliams
 * @author <a href="http://www.campudus.com/">Joern Bernhardt</a>
 * @author Galder Zamarreño
 */
class RouteMatcher (val asJava: JRouteMatcher = new JRouteMatcher()) extends Handler[HttpServerRequest]
  with (HttpServerRequest => Unit)
  with Self[RouteMatcher] {

  protected[this] def self: RouteMatcher = this

  def all(uri: String, handler: HttpServerRequest => Unit): RouteMatcher =
    wrap(asJava.all(uri, wrapHandler(handler)))

  def allWithRegEx(regex: String, handler: HttpServerRequest => Unit): RouteMatcher =
    wrap(asJava.allWithRegEx(regex, wrapHandler(handler)))

  def apply(request: HttpServerRequest): Unit = handle(request)

  def connect(uri: String, handler: HttpServerRequest => Unit): RouteMatcher =
    wrap(asJava.connect(uri, wrapHandler(handler)))

  def connectWithRegEx(regex: String, handler: HttpServerRequest => Unit): RouteMatcher =
    wrap(asJava.connectWithRegEx(regex, wrapHandler(handler)))

  def delete(uri: String, handler: HttpServerRequest => Unit): RouteMatcher =
    wrap(asJava.delete(uri, wrapHandler(handler)))

  def deleteWithRegEx(regex: String, handler: HttpServerRequest => Unit): RouteMatcher =
    wrap(asJava.deleteWithRegEx(regex, wrapHandler(handler)))

  def get(uri: String, handler: HttpServerRequest => Unit): RouteMatcher =
    wrap(asJava.get(uri, wrapHandler(handler)))

  def getWithRegEx(regex: String, handler: HttpServerRequest => Unit): RouteMatcher =
    wrap(asJava.getWithRegEx(regex, wrapHandler(handler)))

  def handle(request: HttpServerRequest): Unit = asJava.handle(request.asJava)

  def head(uri: String, handler: HttpServerRequest => Unit): RouteMatcher =
    wrap(asJava.head(uri, wrapHandler(handler)))

  def headWithRegEx(regex: String, handler: HttpServerRequest => Unit): RouteMatcher =
    wrap(asJava.headWithRegEx(regex, wrapHandler(handler)))

  def options(uri: String, handler: HttpServerRequest => Unit): RouteMatcher =
    wrap(asJava.options(uri, wrapHandler(handler)))

  def optionsWithRegEx(regex: String, handler: HttpServerRequest => Unit): RouteMatcher =
    wrap(asJava.optionsWithRegEx(regex, wrapHandler(handler)))

  def patch(uri: String, handler: HttpServerRequest => Unit): RouteMatcher =
    wrap(asJava.patch(uri, wrapHandler(handler)))

  def patchWithRegEx(regex: String, handler: HttpServerRequest => Unit): RouteMatcher =
    wrap(asJava.patchWithRegEx(regex, wrapHandler(handler)))

  def post(uri: String, handler: HttpServerRequest => Unit): RouteMatcher =
    wrap(asJava.post(uri, wrapHandler(handler)))

  def postWithRegEx(regex: String, handler: HttpServerRequest => Unit): RouteMatcher =
    wrap(asJava.postWithRegEx(regex, wrapHandler(handler)))

  def put(uri: String, handler: HttpServerRequest => Unit): RouteMatcher =
    wrap(asJava.put(uri, wrapHandler(handler)))

  def putWithRegEx(regex: String, handler: HttpServerRequest => Unit): RouteMatcher =
    wrap(asJava.putWithRegEx(regex, wrapHandler(handler)))

  def trace(uri: String, handler: HttpServerRequest => Unit): RouteMatcher =
    wrap(asJava.trace(uri, wrapHandler(handler)))

  def traceWithRegEx(regex: String, handler: HttpServerRequest => Unit): RouteMatcher =
    wrap(asJava.traceWithRegEx(regex, wrapHandler(handler)))

  private def wrapHandler(handler: HttpServerRequest => Unit) =
    fnToHandler(handler.compose(HttpServerRequest.apply))

}