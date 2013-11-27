package org.vertx.scala.core

import org.vertx.java.core.datagram.{DatagramPacket => JDatagramPacket}
import org.vertx.scala.core.FunctionConverters._

package object datagram {

  def dataPacketHandlerToJava(handler: DatagramPacket => Unit) =
    fnToHandler(handler.compose { jdatagramPacket: JDatagramPacket => DatagramPacket.apply(jdatagramPacket) })

}
