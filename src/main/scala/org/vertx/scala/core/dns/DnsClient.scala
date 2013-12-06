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

import java.net.{ Inet4Address, Inet6Address, InetAddress }

import scala.collection.JavaConversions.asScalaBuffer

import org.vertx.java.core.dns.{ DnsClient => JDnsClient, DnsException => JDnsException, MxRecord => JMxRecord, SrvRecord => JSrvRecord }
import org.vertx.scala.Self
import org.vertx.scala.core.AsyncResult
import org.vertx.scala.core.FunctionConverters.{ asyncResultConverter, convertFunctionToParameterisedAsyncHandler, handlerToFn }

/**
 * Provides a way to asynchronous lookup informations from DNS-Servers.
 *
 * @author <a href="mailto:nmaurer@redhat.com">Norman Maurer</a>
 * @author <a href="http://www.campudus.com/">Joern Bernhardt</a>
 */
// constructor is private because users should use apply in companion
// extends AnyVal to avoid object allocation and improve performance
final class DnsClient private[dns] (val internal: JDnsClient) extends AnyVal
  with Self[DnsClient] {

  override protected[this] def self: DnsClient = this

  /**
   * Try to lookup the A (ipv4) or AAAA (ipv6) record for the given name. The first found will be used.
   *
   * @param name      The name to resolve
   * @param handler   the {@link Handler} to notify with the {@link AsyncResult}. The {@link AsyncResult} will get
   *                  notified with the resolved {@link InetAddress} if a record was found. If non was found it will
   *                  get notifed with {@code null}.
   *                  If an error accours it will get failed.
   * @return          itself for method-chaining.
   */
  def lookup(name: String, handler: AsyncResult[InetAddress] => Unit): DnsClient = wrap(internal.lookup(name, mapDnsException(handler)))

  /**
   * Try to lookup the A (ipv4) record for the given name. The first found will be used.
   *
   * @param name      The name to resolve
   * @param handler   the {@link Handler} to notify with the {@link AsyncResult}. The {@link AsyncResult} will get
   *                  notified with the resolved {@link Inet4Address} if a record was found. If non was found it will
   *                  get notifed with {@code null}.
   *                  If an error accours it will get failed.
   * @return          itself for method-chaining.
   */
  def lookup4(name: String, handler: AsyncResult[Inet4Address] => Unit): DnsClient = wrap(internal.lookup4(name, mapDnsException(handler)))

  /**
   * Try to lookup the AAAA (ipv6) record for the given name. The first found will be used.
   *
   * @param name      The name to resolve
   * @param handler   the {@link Handler} to notify with the {@link AsyncResult}. The {@link AsyncResult} will get
   *                  notified with the resolved {@link Inet6Address} if a record was found. If non was found it will
   *                  get notifed with {@code null}.
   *                  If an error accours it will get failed.
   * @return          itself for method-chaining.
   */
  def lookup6(name: String, handler: AsyncResult[Inet6Address] => Unit): DnsClient = wrap(internal.lookup6(name, mapDnsException(handler)))

  /**
   * Try to resolve all A (ipv4) records for the given name.
   *
   *
   *
   * @param name      The name to resolve
   * @param handler   the {@link org.vertx.java.core.Handler} to notify with the {@link org.vertx.java.core.AsyncResult}. The {@link org.vertx.java.core.AsyncResult} will get
   *                  notified with a {@link java.util.List} that contains all the resolved {@link java.net.Inet4Address}es. If non was found
   *                  and empty {@link java.util.List} will be used.
   *                  If an error accours it will get failed.
   * @return          itself for method-chaining.
   */
  def resolveA(name: String, handler: AsyncResult[List[Inet4Address]] => Unit): DnsClient = wrap(internal.resolveA(name, mapDnsException(asyncResultConverter(listMapper[Inet4Address])(handler))))

  /**
   * Try to resolve all AAAA (ipv6) records for the given name.
   *
   *
   * @param name      The name to resolve
   * @param handler   the {@link org.vertx.java.core.Handler} to notify with the {@link org.vertx.java.core.AsyncResult}. The {@link org.vertx.java.core.AsyncResult} will get
   *                  notified with a {@link java.util.List} that contains all the resolved {@link java.net.Inet6Address}es. If non was found
   *                  and empty {@link java.util.List} will be used.
   *                  If an error accours it will get failed.
   * @return          itself for method-chaining.
   */
  def resolveAAAA(name: String, handler: AsyncResult[List[Inet6Address]] => Unit): DnsClient = wrap(internal.resolveAAAA(name, mapDnsException(asyncResultConverter(listMapper[Inet6Address])(handler))))

  /**
   * Try to resolve the CNAME record for the given name.
   *
   * @param name      The name to resolve the CNAME for
   * @param handler   the {@link Handler} to notify with the {@link AsyncResult}. The {@link AsyncResult} will get
   *                  notified with the resolved {@link String} if a record was found. If non was found it will
   *                  get notified with {@code null}.
   *                  If an error accours it will get failed.
   * @return          itself for method-chaining.
   */
  def resolveCNAME(name: String, handler: AsyncResult[List[String]] => Unit): DnsClient = wrap(internal.resolveCNAME(name, mapDnsException(asyncResultConverter(listMapper[String])(handler))))

  /**
   * Try to resolve the MX records for the given name.
   *
   *
   * @param name      The name for which the MX records should be resolved
   * @param handler   the {@link org.vertx.java.core.Handler} to notify with the {@link org.vertx.java.core.AsyncResult}. The {@link org.vertx.java.core.AsyncResult} will get
   *                  notified with a List that contains all resolved {@link org.vertx.java.core.dns.MxRecord}s, sorted by their
   *                  {@link org.vertx.java.core.dns.MxRecord#priority()}. If non was found it will get notified with an empty {@link java.util.List}
   *                  If an error accours it will get failed.
   * @return          itself for method-chaining.
   */
  def resolveMX(name: String, handler: AsyncResult[List[MxRecord]] => Unit): DnsClient = wrap(internal.resolveMX(name, mapDnsException(asyncResultConverter(mappedListMapper[JMxRecord, MxRecord](MxRecord.apply))(handler))))

  /**
   * Try to resolve the TXT records for the given name.
   *
   * @param name      The name for which the TXT records should be resolved
   * @param handler   the {@link Handler} to notify with the {@link AsyncResult}. The {@link AsyncResult} will get
   *                  notified with a List that contains all resolved {@link String}s. If non was found it will
   *                  get notified with an empty {@link List}
   *                  If an error accours it will get failed.
   * @return          itself for method-chaining.
   */
  def resolveTXT(name: String, handler: AsyncResult[List[String]] => Unit): DnsClient = wrap(internal.resolveTXT(name, mapDnsException(asyncResultConverter(listMapper[String])(handler))))

  /**
   * Try to resolve the PTR record for the given name.
   *
   * @param name      The name to resolve the PTR for
   * @param handler   the {@link Handler} to notify with the {@link AsyncResult}. The {@link AsyncResult} will get
   *                  notified with the resolved {@link String} if a record was found. If non was found it will
   *                  get notified with {@code null}.
   *                  If an error accours it will get failed.
   * @return          itself for method-chaining.
   */
  def resolvePTR(name: String, handler: AsyncResult[String] => Unit): DnsClient = wrap(internal.resolvePTR(name, mapDnsException(handler)))

  /**
   * Try to resolve the NS records for the given name.
   *
   * @param name      The name for which the NS records should be resolved
   * @param handler   the {@link Handler} to notify with the {@link AsyncResult}. The {@link AsyncResult} will get
   *                  notified with a List that contains all resolved {@link String}s. If non was found it will
   *                  get notified with an empty {@link List}
   *                  If an error accours it will get failed.
   * @return          itself for method-chaining.
   */
  def resolveNS(name: String, handler: AsyncResult[List[String]] => Unit): DnsClient = wrap(internal.resolveNS(name, mapDnsException(asyncResultConverter(listMapper[String])(handler))))

  /**
   * Try to resolve the SRV records for the given name.
   *
   * @param name      The name for which the SRV records should be resolved
   * @param handler   the {@link Handler} to notify with the {@link AsyncResult}. The {@link AsyncResult} will get
   *                  notified with a List that contains all resolved {@link SrvRecord}s. If non was found it will
   *                  get notified with an empty {@link List}
   *                  If an error accours it will get failed.
   * @return          itself for method-chaining.
   */
  def resolveSRV(name: String, handler: AsyncResult[List[SrvRecord]] => Unit): DnsClient = wrap(internal.resolveSRV(name, mapDnsException(asyncResultConverter(mappedListMapper[JSrvRecord, SrvRecord](SrvRecord.apply))(handler))))

  /**
   * Try to do a reverse lookup of an ipaddress. This is basically the same as doing trying to resolve a PTR record
   * but allows you to just pass in the ipaddress and not a valid ptr query string.
   *
   * @param ipaddress The ipaddress to resolve the PTR for
   * @param handler   the {@link Handler} to notify with the {@link AsyncResult}. The {@link AsyncResult} will get
   *                  notified with the resolved {@link String} if a record was found. If non was found it will
   *                  get notified with {@code null}.
   *                  If an error accours it will get failed.
   * @return          itself for method-chaining.
   */
  def reverseLookup(ipaddress: String, handler: AsyncResult[InetAddress] => Unit): DnsClient = wrap(internal.reverseLookup(ipaddress, mapDnsException(handler)))

  private def listMapper[X](jList: java.util.List[X]): List[X] = jList.toList
  private def mappedListMapper[JT, ST](mappedFn: JT => ST)(jList: java.util.List[JT]): List[ST] = jList.toList.map(mappedFn)
  private def mapDnsException[X](handler: AsyncResult[X] => Unit): AsyncResult[X] => Unit = {
    handler.compose { ar: AsyncResult[X] =>
      ar.cause() match {
        case x: JDnsException => new DnsExceptionAsyncResult(x, ar)
        case _ => ar
      }
    }
  }

}

object DnsClient {
  def apply(internal: JDnsClient) = new DnsClient(internal)
}