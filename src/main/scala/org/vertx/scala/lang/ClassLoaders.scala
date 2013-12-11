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
package org.vertx.scala.lang

import scala.util.Try

/**
 * Class loading utility class.
 *
 * @author Galder Zamarreño
 */
object ClassLoaders {

  /**
   * Instantiates the given class name using the classloader provided
   * @param className String containing the class name to instantiate
   * @param classLoader where to find the given class name
   * @tparam T type of instance expected back
   * @return [[scala.util.Success]] containing a new instance of the given
   *        class or [[scala.util.Failure]] with any errors reported
   */
  def newInstance[T](className: String, classLoader: ClassLoader): Try[T] = {
    Try {
      val clazz = classLoader.loadClass(className)
      clazz.newInstance().asInstanceOf[T]
    }
  }

}
