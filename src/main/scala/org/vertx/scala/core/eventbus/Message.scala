package org.vertx.scala.core.eventbus

import org.vertx.java.core.eventbus.{Message => JMessage}
import org.vertx.scala.core.FunctionConverters._
import org.vertx.java.core.buffer.Buffer
import org.vertx.java.core.json.{JsonObject, JsonArray}


object Message {

  def apply[T](jmessage: JMessage[T]) =
    new Message(jmessage)

}

class Message[T](jmessage: JMessage[T]) {

  def body:T = jmessage.body()

  def replyAddress:String = jmessage.replyAddress()

  def reply(payload: T, handler: Message[T] => Unit = msg => {}):Unit = payload match{
      case str:String =>
              jmessage.reply(str, handler)
      case boo:Boolean =>
              jmessage.reply(boo, handler)
      case bff:Buffer =>
              jmessage.reply(bff, handler)
      case bya:Array[Byte] =>
              jmessage.reply(bya, handler)
      case chr:Char =>
              jmessage.reply(Char.box(chr), handler)
      case dbl:Double =>
              jmessage.reply(dbl, handler)
      case flt:Float =>
              jmessage.reply(Float.box(flt), handler)
      case int:Int =>
              jmessage.reply(Int.box(int), handler)
      case jsa:JsonArray =>
              jmessage.reply(jsa, handler)
      case jso:JsonObject =>
              jmessage.reply(jso, handler)
      case lng:Long =>
              jmessage.reply(Long.box(lng), handler)
      case srt:Short =>
              jmessage.reply(Short.box(srt), handler)
      case obj:AnyRef=>
              jmessage.reply(obj, handler)

      case _ => throw new IllegalArgumentException("Invalid reply message " + payload.getClass)

  }


}
