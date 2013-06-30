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

package org.vertx.scala.platform

import org.vertx.java.core.Future
import org.vertx.scala.core.Vertx

/**
 * @author swilliams
 * 
 */
trait Verticle {

  var vertx: Vertx

  var container: Container

  def start(): Unit = {
    // NO-OP
  }

  // TODO use an idiomatic Future type here
  def start(result: Future[Void]): Unit = {
    start()
    result.setResult(null)
  }

  def stop(): Unit = {
    // NO-OP
  }

}