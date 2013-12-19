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
