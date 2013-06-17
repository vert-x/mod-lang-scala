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

package org.vertx.scala.core

import org.vertx.java.core.{MultiMap => JMultiMap}
import scala.collection.mutable
import scala.collection.JavaConversions._

package object http {

  /**
   * Implicit conversion for [[org.vertx.java.core.MultiMap]] to [[scala.collection.mutable.MultiMap]].
   */
  implicit def multiMapToScalaMultiMap(n: JMultiMap): mutable.MultiMap[String, String] = {
    val multiMap = new mutable.HashMap[String, mutable.Set[String]] with mutable.MultiMap[String, String]
    n.iterator.foldLeft(multiMap) { (prev, next) =>
      prev.addBinding(next.getKey, next.getValue)
    }
  }
}