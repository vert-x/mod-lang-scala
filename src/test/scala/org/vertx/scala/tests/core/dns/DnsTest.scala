package org.vertx.scala.tests.core.dns

import org.vertx.scala.testtools.TestVerticle
import org.vertx.testtools.VertxAssert._
import org.junit.Test
import org.vertx.testtools.TestDnsServer
import org.vertx.scala.core.dns.DnsClient
import java.net.InetSocketAddress
import java.net.Inet4Address
import org.vertx.scala.core.AsyncResult
import java.net.Inet6Address
import org.vertx.scala.core.dns.MxRecord
import org.vertx.scala.core.dns.SrvRecord
import java.net.InetAddress
import org.vertx.scala.core.dns.DnsException
import org.vertx.scala.core.dns.DnsResponseCode
import org.vertx.java.core.dns.{ DnsResponseCode => JDnsResponseCode }

class DnsTest extends TestVerticle {

  // bytes representation of ::1
  private val IP6_BYTES: Array[Byte] = Array(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1)

  @Test
  def testResolveA(): Unit = {
    val ip = "10.0.0.1"
    val server: TestDnsServer = TestDnsServer.testResolveA(ip)
    val dns: DnsClient = prepareDns(server)

    dns.resolveA("vertx.io", { ar: AsyncResult[List[Inet4Address]] =>
      if (ar.succeeded()) {
        val result: List[Inet4Address] = ar.result

        assertTrue(!result.isEmpty)
        assertEquals(result.size, 1)
        assertTrue(ip.equals(result.apply(0).getHostAddress()))
        server.stop()
        testComplete
      } else {
        fail("ResolveA failed: " + ar.cause().asInstanceOf[DnsException].code)
      }
    })
  }

  @Test
  def testResolveAAAA(): Unit = {
    val server: TestDnsServer = TestDnsServer.testResolveAAAA("::1")
    val dns: DnsClient = prepareDns(server)

    dns.resolveAAAA("vertx.io", { ar: AsyncResult[List[Inet6Address]] =>
      if (ar.succeeded()) {
        val result: List[Inet6Address] = ar.result

        assertTrue(!result.isEmpty)
        assertEquals(result.size, 1)
        (0 to IP6_BYTES.length - 1) map (i => assertEquals(IP6_BYTES(i),result(0).getAddress()(i)))
        server.stop()
        testComplete
      } else {
        fail("ResolveAAAA failed: " + ar.cause())
      }
    })

  }

  @Test
  def testResolveMX(): Unit = {
    val mxRecordx: String = "mail.vertx.io"
    val prio: Int = 10
    val server: TestDnsServer = TestDnsServer.testResolveMX(prio, mxRecordx)
    val dns: DnsClient = prepareDns(server)

    dns.resolveMX("vertx.io", { ar: AsyncResult[List[MxRecord]] =>
      if (ar.succeeded()) {
        val result: List[MxRecord] = ar.result()
        val record: MxRecord = result(0)
        assertTrue(!result.isEmpty)
        assertEquals(result.size, 1)
        assertEquals(record.priority, prio)
        assertEquals(record.name, mxRecordx)
        server.stop()
        testComplete
      } else {
        fail("ResolveMX failed: " + ar.cause())
      }
    })
  }

  @Test
  def testResolveTXT(): Unit = {
    val txt: String = "vertx is awesome"
    val server: TestDnsServer = TestDnsServer.testResolveTXT(txt)  
    val dns: DnsClient = prepareDns(server)

    dns.resolveTXT("vertx.io", { ar: AsyncResult[List[String]] =>
      if (ar.succeeded()) {
        val result: List[String] = ar.result()
        assertTrue(!result.isEmpty)
        assertEquals(result.size, 1)
        assertEquals(txt, result(0))
        server.stop()
        testComplete
      } else {
        fail("ResolveMX failed: " + ar.cause())
      }
    })
  }

  @Test
  def testResolveNS(): Unit = {
    val ns: String = "ns.vertx.io"
    val server: TestDnsServer = TestDnsServer.testResolveNS(ns)  
    val dns: DnsClient = prepareDns(server)

    dns.resolveNS("vertx.io", { ar: AsyncResult[List[String]] =>
      if (ar.succeeded()) {
        val result: List[String] = ar.result()
        assertTrue(!result.isEmpty)
        assertEquals(result.size, 1)
        assertEquals(ns, result(0))
        server.stop()
        testComplete
      } else {
        fail("Failed " + ar.cause())
      }
    })
  }

  @Test
  def testResolveCNAME(): Unit = {
    val cname: String = "cname.vertx.io"
    val server: TestDnsServer = TestDnsServer.testResolveCNAME(cname)
    val dns: DnsClient = prepareDns(server)

    dns.resolveCNAME("vertx.io", { ar: AsyncResult[List[String]] =>
      if (ar.succeeded()) {
        val result: List[String] = ar.result()
        val record: String = result(0)
        assertTrue(!result.isEmpty)
        assertEquals(result.size, 1)
        assertTrue(!record.isEmpty)
        assertEquals(cname, record)
        server.stop()
        testComplete
      } else {
        fail("Failed " + ar.cause())
      }
    })
  }

  @Test
  def testResolvePTR(): Unit = {
    val ptr: String = "ptr.vertx.io"
    val server: TestDnsServer = TestDnsServer.testResolvePTR(ptr)
    val dns: DnsClient = prepareDns(server)

    dns.resolvePTR("10.0.0.1.in-addr.arpa", { ar: AsyncResult[String] =>
      if (ar.succeeded()) {
        val result: String = ar.result()
        assertTrue(!result.isEmpty)
        assertEquals(ptr, result)
        server.stop()
        testComplete
      } else {
        fail("Failed " + ar.cause())
      }
    })
  }

  @Test
  def testResolveSRV(): Unit = {
    val priority: Int = 10
    val weight: Int = 1
    val port: Int = 80
    val target: String = "vertx.io"
    val server: TestDnsServer = TestDnsServer.testResolveSRV(priority, weight, port, target)
    val dns: DnsClient = prepareDns(server)

    dns.resolveSRV("vertx.io", { ar: AsyncResult[List[SrvRecord]] =>
      if (ar.succeeded()) {
        val result: List[SrvRecord] = ar.result()
        val record: SrvRecord = result(0)
        assertTrue(!result.isEmpty)
        assertEquals(result.size, 1)
        assertEquals(priority, record.priority)
        assertEquals(weight, record.weight)
        assertEquals(port, record.port)
        assertEquals(target, record.target)
        server.stop()
        testComplete
      } else {
        fail("Failed " + ar.cause())
      }
    })
  }

  @Test
  def testLookup4(): Unit = {
    val ip: String = "10.0.0.1"
    val server: TestDnsServer = TestDnsServer.testLookup4(ip)
    val dns: DnsClient = prepareDns(server)

    dns.lookup4("vertx.io", { ar: AsyncResult[Inet4Address] =>
      if (ar.succeeded()) {
        val result: InetAddress = ar.result()
        assertEquals(ip, result.getHostAddress())
        server.stop()
        testComplete
      } else {
        fail("Lookup4 failed: " + ar.cause())
      }

    })
  }

  @Test
  def testLookup6(): Unit = {
    val server: TestDnsServer = TestDnsServer.testLookup6()
    val dns: DnsClient = prepareDns(server)

    dns.lookup6("vertx.io", { ar: AsyncResult[Inet6Address] =>
      if (ar.succeeded()) {
        val result: Inet6Address = ar.result()
        (0 to IP6_BYTES.length - 1) map (i => assertEquals(IP6_BYTES(i),result.getAddress()(i)))
        server.stop()
        testComplete
      } else {
        fail("Lookup4 failed: " + ar.cause())
      }
    })
  }

  @Test
  def testLookup(): Unit = {
    val ip: String = "10.0.0.1"
      val server: TestDnsServer = TestDnsServer.testLookup(ip)
    val dns: DnsClient = prepareDns(server)

    dns.lookup("vertx.io", { ar: AsyncResult[InetAddress] =>
      if (ar.succeeded()) {
        val result: InetAddress = ar.result()
        assertEquals(ip, result.getHostAddress())
        server.stop()
        testComplete
      } else {
        fail("Failed " + ar.cause())
      }
    })
  }

  @Test
  def testLookupNonExisting(): Unit = {
    val server: TestDnsServer = TestDnsServer.testLookupNonExisting()
    val dns: DnsClient = prepareDns(server)

    dns.lookup("gfegjegjf.sg1", { ar: AsyncResult[InetAddress] =>
        val cause: DnsException = ar.cause().asInstanceOf[DnsException]
        assertEquals(cause.code, DnsResponseCode.fromJava(JDnsResponseCode.NXDOMAIN))
        server.stop()
        testComplete
    })
  }

  @Test
  def testReverseLookupIpv4(): Unit = {
    val address: Array[Byte] = InetAddress.getByName("10.0.0.1").getAddress()
    val ptr: String = "ptr.vertx.io"
    val server: TestDnsServer = TestDnsServer.testReverseLookup(ptr)
    val dns: DnsClient = prepareDns(server)

    dns.reverseLookup("10.0.0.1", { ar: AsyncResult[InetAddress] =>
      if (ar.succeeded()) {
        val result: InetAddress = ar.result()
        assertTrue(result.isInstanceOf[Inet4Address])
        assertEquals(ptr, result.getHostName())
        (0 to address.length - 1) map (i => assertEquals(address(i),result.getAddress()(i)))
        server.stop()
        testComplete
      } else {
        fail("Failed " + ar.cause())
      }
    })
  }

  @Test
  def testReverseLookupIpv6(): Unit = {
    val address: Array[Byte] = InetAddress.getByName("::1").getAddress()
    val ptr: String = "prt.vertx.io"
    val server: TestDnsServer = TestDnsServer.testReverseLookup(ptr)
    val dns: DnsClient = prepareDns(server)

    dns.reverseLookup("::1", { ar: AsyncResult[InetAddress] =>
      if (ar.succeeded()) {
        val result: InetAddress = ar.result()
        assertTrue(result.isInstanceOf[Inet6Address])
        assertEquals(ptr, result.getHostName())
        (0 to address.length - 1) map (i => assertEquals(address(i),result.getAddress()(i)))
        server.stop()
        testComplete
      } else {
        fail("Failed " + ar.cause())
      }
    })
  }

  private def prepareDns(server: TestDnsServer): DnsClient = {
    val dnsServer = server
    dnsServer.start()
    val addr: InetSocketAddress = dnsServer.getTransports()(0).getAcceptor().getLocalAddress().asInstanceOf[InetSocketAddress]
    vertx.createDnsClient(addr)
  }

}