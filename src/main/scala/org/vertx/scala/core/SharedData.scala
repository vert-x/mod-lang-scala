/*
 * Copyright 2011-2012 the original author or authors.
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

package org.vertx.scala.core

import scala.collection.JavaConversions._
import scala.collection.mutable.ConcurrentMap
import scala.collection.mutable.Set
import org.vertx.java.core.shareddata.{SharedData => JSharedData}

object SharedData {
  def apply(actual: JSharedData) =
    new SharedData(actual)
}

class SharedData(internal: JSharedData) {

  def map[K,V](name: String):ConcurrentMap[K,V] = {
    val map: java.util.concurrent.ConcurrentMap[K,V] = internal.getMap(name)
    map
  }

  def set[T](name: String):Set[T] = {
    val set: java.util.Set[T] = internal.getSet(name)
    set
  }

  def removeMap[T](name: Any):Boolean = {
    internal.removeMap(name)
  }

  def removeSet[T](name: String):Boolean = {
    internal.removeSet(name)
  }

}