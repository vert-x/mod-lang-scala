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

import org.vertx.java.core.{ MultiMap => JMultiMap }
import scala.collection.mutable
import org.vertx.java.core.http.CaseInsensitiveMultiMap

package object http {

  type HttpVersion = org.vertx.java.core.http.HttpVersion
  type WebSocketVersion = org.vertx.java.core.http.WebSocketVersion

  import scala.language.implicitConversions

  /**
   * Implicit conversion for [[org.vertx.java.core.MultiMap]] to [[scala.collection.mutable.MultiMap]].
   */
  implicit def multiMapToScalaMultiMap(n: JMultiMap): mutable.MultiMap[String, String] = {
    new JMultiMapWrapper(n)
  }

  /**
   * Implicit conversion for [[scala.collection.mutable.MultiMap]] to [[org.vertx.java.core.MultiMap]].
   */
  implicit def scalaMultiMapToMultiMap(n: mutable.MultiMap[String, String]): JMultiMap = {
    val jmultiMap = new CaseInsensitiveMultiMap
    n.foreach { entry => jmultiMap.put(entry._1, entry._2)}
    jmultiMap
  }

  private class JMultiMapWrapper(val underlying: JMultiMap)
      extends mutable.MultiMap[String, String] {
    override def addBinding(key: String, value: String): this.type = {
      underlying.add(key, value)
      this
    }
    override def removeBinding(key: String, value: String): this.type = {
      val it = underlying.iterator()
      while (it.hasNext) {
        val next = it.next()
        if (next.getKey.equalsIgnoreCase(key) && next.getValue == value)
          it.remove()
      }
      this
    }
    override def entryExists(key: String, p: (String) => Boolean): Boolean = {
      val it = underlying.iterator()
      while (it.hasNext) {
        val next = it.next()
        if (next.getKey.equalsIgnoreCase(key) && p(next.getValue))
          return true
      }
      false
    }
    override def iterator: Iterator[(String, mutable.Set[String])] = {
      val mm = new mutable.HashMap[String, mutable.Set[String]] with MultiMap
      val it = underlying.iterator()
      while (it.hasNext) {
        val next = it.next()
        mm.addBinding(next.getKey, next.getValue)
      }
      mm.iterator
    }
    override def get(key: String): Option[mutable.Set[String]] = {
      val set = mutable.HashSet[String]()
      val it = underlying.iterator()
      while (it.hasNext) {
        val next = it.next()
        if (next.getKey.equalsIgnoreCase(key))
          set.add(next.getValue)
      }
      if (seq.isEmpty) None else Some(set)
    }
    override def -=(key: String): this.type = {
      underlying.remove(key)
      this
    }
    override def +=(kv: (String, mutable.Set[String])): this.type = {
      kv._2.foreach { v =>
        underlying.add(kv._1, v)
      }
      this
    }
  }

}