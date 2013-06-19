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
package org.vertx.scala.core.buffer

import org.vertx.java.core.buffer.{Buffer => JBuffer}

/**
 * @author swilliams
 * @author Edgar Chan
 */
object Buffer {

  implicit class SBuffer(val actual: JBuffer) extends AnyVal {

    def toJava:JBuffer = actual

    def append[T](v:T):SBuffer= v match {
      case sb:SBuffer     => actual.appendBuffer(sb.actual)
      case jb:JBuffer     => actual.appendBuffer(jb)
      case ba:Array[Byte] => actual.appendBytes(ba)
      case by:Byte        => actual.appendByte(by)
      case in:Int         => actual.appendInt(in)
      case ln:Long        => actual.appendLong(ln)
      case sh:Short       => actual.appendShort(sh)
      case fl:Float       => actual.appendFloat(fl)
      case db:Double      => actual.appendDouble(db)
      case st:String      => actual.appendString(st)
      case _ => throw new IllegalArgumentException("Invalid " + v.getClass)
    }
  }
}