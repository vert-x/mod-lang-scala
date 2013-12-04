package org.vertx.scala.core

import org.vertx.scala.core.FunctionConverters._

package object datagram {

  def dataPacketHandlerToJava(handler: DatagramPacket => Unit) =
    fnToHandler(handler.compose { DatagramPacket.apply })

}
