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

/**
 * This trait shows that this type is a wrapper around a Vert.x Java class.
 *
 * @author <a href="http://www.campudus.com/">Joern Bernhardt</a>
 * @author Galder Zamarreño
 */
// Extends Any in order to be an universal trait and be mixed in with an AnyVal
private[scala] trait AsJava extends Any {

  /** The internal type of the Java wrapped class. */
  type J

  /** The internal instance of the Java wrapped class. */
  val asJava: J

}