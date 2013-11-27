package org.vertx.scala.core.datagram

import org.vertx.java.core.datagram.{InternetProtocolFamily => JInternetProtocolFamily}

/**
 * Internet Protocol (IP) families used by [[org.vertx.scala.core.datagram.DatagramSocket]].
 */
sealed trait InternetProtocolFamily
case object IPv4 extends InternetProtocolFamily
case object IPv6 extends InternetProtocolFamily

object InternetProtocolFamily {
  implicit def toScalaIpFamily(family: JInternetProtocolFamily): InternetProtocolFamily = {
    family match {
      case JInternetProtocolFamily.IPv4 => IPv4
      case JInternetProtocolFamily.IPv6 => IPv6
    }
  }

  implicit def toJavaIpFamily(family: Option[InternetProtocolFamily]): Option[JInternetProtocolFamily] = {
    family match {
      case Some(IPv4) => Some(JInternetProtocolFamily.IPv4)
      case Some(IPv6) => Some(JInternetProtocolFamily.IPv6)
      case None => None
    }
  }
}
