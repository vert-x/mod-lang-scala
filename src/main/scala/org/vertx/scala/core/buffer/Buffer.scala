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

import org.vertx.java.core.buffer.{ Buffer => JBuffer }
import org.vertx.scala.VertxWrapper

/**
 * @author swilliams
 * @author Edgar Chan
 * @author <a href="http://www.campudus.com/">Joern Bernhardt</a>
 */
package object buffer {

  implicit class Buffer(val internal: JBuffer) extends AnyVal {

    def toJava: JBuffer = internal

    def append[T: BufferType](v: T): Buffer = {
      implicitly[BufferType[T]].appendToBuffer(internal, v)
    }

    override def toString(): String = internal.toString()
  }
}