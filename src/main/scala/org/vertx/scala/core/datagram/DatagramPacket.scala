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
package org.vertx.scala.core.datagram

import org.vertx.java.core.datagram.{ DatagramPacket => JDatagramPacket }
import java.net.InetSocketAddress
import org.vertx.scala.core.buffer.Buffer

/**
 * A received Datagram packet (UDP) which contains the data and information about the sender of the data itself.
 *
 * @author <a href="mailto:nmaurer@redhat.com">Norman Maurer</a>
 * @author Galder Zamarreño
 */
// constructor is private because users should use apply in companion
// extends AnyVal to avoid object allocation and improve performance
final class DatagramPacket private[scala] (val asJava: JDatagramPacket) extends AnyVal {

  /**
   * Returns the [[java.net.InetSocketAddress]] of the sender that send this
   * [[org.vertx.scala.core.datagram.DatagramPacket]].
   */
  def sender(): InetSocketAddress = asJava.sender()

  /**
   * Returns the data of the [[org.vertx.scala.core.datagram.DatagramPacket]]
   */
  def data(): Buffer = Buffer(asJava.data())

}

/** Factory for [[org.vertx.scala.core.datagram.DatagramPacket]] instances by wrapping a Java instance. */
object DatagramPacket {
  def apply(actual: JDatagramPacket) = new DatagramPacket(actual)
}
