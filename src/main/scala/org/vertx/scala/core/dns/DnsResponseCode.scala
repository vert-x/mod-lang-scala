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
package org.vertx.scala.core.dns

import org.vertx.java.core.dns.{ DnsResponseCode => JDnsResponseCode }

abstract sealed class DnsResponseCode(code: Int, message: String) {
  override def toString: String = getClass.getName + ": type " + code + ", " + message
  def toJava: JDnsResponseCode = JDnsResponseCode.valueOf(code)
}

object DnsResponseCode {
  def fromJava(x: JDnsResponseCode) = x.code() match {
    case 0 => NOERROR
    case 1 => FORMERROR
    case 2 => SERVFAIL
    case 3 => NXDOMAIN
    case 4 => NOTIMPL
    case 5 => REFUSED
    case 6 => YXDOMAIN
    case 7 => YXRRSET
    case 8 => NXRRSET
    case 9 => NOTAUTH
    case 10 => NOTZONE
    case 11 => BADVERS
    case 12 => BADSIG
    case 13 => BADKEY
    case 14 => BADTIME
  }
}

/**
 * ID 0, no error
 */
case object NOERROR extends DnsResponseCode(0, "no error")

/**
 * ID 1, format error
 */
case object FORMERROR extends DnsResponseCode(1, "format error")

/**
 * ID 2, server failure
 */
case object SERVFAIL extends DnsResponseCode(2, "server failure")

/**
 * ID 3, name error
 */
case object NXDOMAIN extends DnsResponseCode(3, "name error")

/**
 * ID 4, not implemented
 */
case object NOTIMPL extends DnsResponseCode(4, "not implemented")

/**
 * ID 5, operation refused
 */
case object REFUSED extends DnsResponseCode(5, "operation refused")

/**
 * ID 6, domain name should not exist
 */
case object YXDOMAIN extends DnsResponseCode(6, "domain name should not exist")

/**
 * ID 7, resource record set should not exist
 */
case object YXRRSET extends DnsResponseCode(7, "resource record set should not exist")

/**
 * ID 8, rrset does not exist
 */
case object NXRRSET extends DnsResponseCode(8, "rrset does not exist")

/**
 * ID 9, not authoritative for zone
 */
case object NOTAUTH extends DnsResponseCode(9, "not authoritative for zone")

/**
 * ID 10, name not in zone
 */
case object NOTZONE extends DnsResponseCode(10, "name not in zone")

/**
 * ID 11, bad extension mechanism for version
 */
case object BADVERS extends DnsResponseCode(11, "bad extension mechanism for version")

/**
 * ID 12, bad signature
 */
case object BADSIG extends DnsResponseCode(12, "bad signature")

/**
 * ID 13, bad key
 */
case object BADKEY extends DnsResponseCode(13, "bad key")

/**
 * ID 14, bad timestamp
 */
case object BADTIME extends DnsResponseCode(14, "bad timestamp")
