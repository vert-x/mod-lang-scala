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
package org.vertx.scala.core.shareddata

import org.vertx.java.core.shareddata.{ SharedData => JSharedData }
import org.vertx.java.core.shareddata.ConcurrentSharedMap
import java.util.Set

/**
 * Sometimes it is desirable to share immutable data between different event loops, for example to implement a
 * cache of data.<p>
 * This class allows instances of shared data structures to be looked up and used from different event loops.<p>
 * The data structures themselves will only allow certain data types to be stored into them. This shields you from
 * worrying about any thread safety issues might occur if mutable objects were shared between event loops.<p>
 * The following types can be stored in a shareddata data structure:<p>
 * <pre>
 *   {@link String}
 *   {@link Integer}
 *   {@link Long}
 *   {@link Double}
 *   {@link Float}
 *   {@link Short}
 *   {@link Byte}
 *   {@link Character}
 *   {@code byte[]} - this will be automatically copied, and the copy will be stored in the structure.
 *   {@link org.vertx.java.core.buffer.Buffer} - this will be automatically copied, and the copy will be stored in the
 *   structure.
 * </pre>
 * <p>
 *
 * Instances of this class are thread-safe.<p>
 *
 * @author <a href="http://tfox.org">Tim Fox</a>
 * @author <a href="http://www.campudus.com/">Joern Bernhardt</a>
 * @author Galder Zamarreño
 */
// constructor is private because users should use apply in companion
// extends AnyVal to avoid object allocation and improve performance
final class SharedData private[scala] (val asJava: JSharedData) extends AnyVal {

  /**
   * Return a {@code Map} with the specific {@code name}. All invocations of this method with the same value of {@code name}
   * are guaranteed to return the same {@code Map} instance. <p>
   */
  def getMap[K, V](name: String): ConcurrentSharedMap[K, V] = asJava.getMap(name)

  /**
   * Return a {@code Set} with the specific {@code name}. All invocations of this method with the same value of {@code name}
   * are guaranteed to return the same {@code Set} instance. <p>
   */
  def getSet[E](name: String): Set[E] = asJava.getSet(name)

  /**
   * Remove the {@code Map} with the specific {@code name}.
   */
  def removeMap(name: String): Boolean = asJava.removeMap(name)

  /**
   * Remove the {@code Set} with the specific {@code name}.
   */
  def removeSet(name: String): Boolean = asJava.removeSet(name)

}

object SharedData {
  def apply(actual: JSharedData) = new SharedData(actual)
}
