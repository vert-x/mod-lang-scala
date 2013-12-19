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
 * [[org.vertx.scala.Self]] allows fluent return types pointing to the type of
 * this object.
 *
 * @author <a href="http://www.campudus.com/">Joern Bernhardt</a>
 * @author Galder Zamarreño
 */
private[scala] trait Self {

  /**
   * Helper method wrapping invocations and returning the Scala type, once
   * again to help provide fluent return types
   */
  protected[this] def wrap[X](doStuff: => X): this.type = {
    doStuff
    this
  }

}
