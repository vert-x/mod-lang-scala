package org.vertx.scala.core.dns

import org.vertx.java.core.dns.{ DnsException => JDnsException }

case class DnsException(code: DnsResponseCode) extends Exception
