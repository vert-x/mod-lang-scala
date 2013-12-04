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
 * Traits that require access to the Java object should extend this trait.
 * Concrete implementations will provide final information on the exact Java
 * type and they'd provide access to the actual java object.
 *
 * @author <a href="http://www.campudus.com/">Joern Bernhardt</a>
 * @author Galder Zamarreño
 */
private[scala] trait AsJava {

  /** The internal type of the Java wrapped class. */
  type J

  /** The internal instance of the Java wrapped class. */
  val asJava: J

}