package org.vertx.scala.core.buffer


import org.vertx.java.core.buffer.{Buffer => JBuffer}

/**
 * Date: 6/1/13
 * @author Edgar Chan
 */
object Buffer_x {

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
