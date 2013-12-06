/*
 * Copyright 2013 the original author or authors.
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

package org.vertx.scala

import org.vertx.java.core.{ Vertx => JVertx }
import org.vertx.java.core.{ VertxFactory => JVertxFactory }

package object core {

  type AsyncResult[T] = org.vertx.java.core.AsyncResult[T]
  type Handler[T] = org.vertx.java.core.Handler[T]
  type MultiMap = org.vertx.java.core.MultiMap

  def newVertx() = new Vertx(JVertxFactory.newVertx())

  def newVertx(port: Int, hostname: String) = new Vertx(JVertxFactory.newVertx(port, hostname))

  def newVertx(hostname: String) = new Vertx(JVertxFactory.newVertx(hostname))

  implicit def javaVertxToScalaVertx(jvertx: JVertx): Vertx = new Vertx(jvertx)

}
