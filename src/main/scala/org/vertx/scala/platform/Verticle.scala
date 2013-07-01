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

  var vertx: Vertx = null

  var container: Container = null

  def start(): Unit = {
    // NO-OP
  }

  /**
   * Start verticle with a callback enabling asynchronous 
   * notification of start completion.
   * 
   * @param future
   */
  // TODO use an idiomatic Future type here
  def start(future: Future[Void]): Unit = {
    start()
    future.setResult(null)
  }

  def stop(): Unit = {
    // NO-OP
  }

}