package org.vertx.scala.core.net

import collection.JavaConversions._
import org.vertx.java.core.net.{NetSocket => JNetSocket}

object NetSocket {
  def apply(actual: JNetSocket) =
    new NetSocket(actual)
}

class NetSocket (internal: JNetSocket) {

}