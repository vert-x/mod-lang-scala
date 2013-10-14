package org.vertx.scala.tests.core.dns

import org.vertx.scala.testtools.TestVerticle
import org.vertx.testtools.VertxAssert._
import org.junit.Test

class DnsTest extends TestVerticle {
  @Test
  def testResolveA(): Unit = fail("not implemented test")
  //    final String ip = "10.0.0.1";
  //    DnsClient dns = prepareDns(TestDnsServer.testResolveA(ip));
  //
  //    dns.resolveA("vertx.io", new Handler<AsyncResult<List<Inet4Address>>>() {
  //      @Override
  //      public void handle(AsyncResult<List<Inet4Address>> event) {
  //        tu.checkThread();
  //        List<Inet4Address> result = event.result();
  //
  //        tu.azzert(!result.isEmpty());
  //        tu.azzert(result.size() == 1);
  //        tu.azzert(ip.equals(result.get(0).getHostAddress()));
  //        tu.testComplete();
  //      }
  //    });

  @Test
  def testResolveAAAA(): Unit = fail("not implemented test")
  //    DnsClient dns = prepareDns(TestDnsServer.testResolveAAAA("::1"));
  //
  //    dns.resolveAAAA("vertx.io", new Handler<AsyncResult<List<Inet6Address>>>() {
  //      @Override
  //      public void handle(AsyncResult<List<Inet6Address>> event) {
  //        tu.checkThread();
  //        List<Inet6Address> result = event.result();
  //        tu.azzert(result != null);
  //        tu.azzert(!result.isEmpty());
  //        tu.azzert(result.size() == 1);
  //
  //        tu.azzert(Arrays.equals(IP6_BYTES, result.get(0).getAddress()));
  //        tu.testComplete();
  //      }
  //    });

  @Test
  def testResolveMX(): Unit = fail("not implemented test")
  //    final String mxRecord = "mail.vertx.io";
  //    final int prio = 10;
  //    DnsClient dns = prepareDns(TestDnsServer.testResolveMX(prio, mxRecord));
  //
  //    dns.resolveMX("vertx.io", new Handler<AsyncResult<List<MxRecord>>>() {
  //      @Override
  //      public void handle(AsyncResult<List<MxRecord>> event) {
  //        tu.checkThread();
  //        List<MxRecord> result = event.result();
  //        tu.azzert(!result.isEmpty());
  //        tu.azzert(1 == result.size());
  //        MxRecord record = result.get(0);
  //        tu.azzert(record.priority() == prio);
  //        tu.azzert(mxRecord.equals(record.name()));
  //        tu.testComplete();
  //      }
  //    });

  @Test
  def testResolveTXT(): Unit = fail("not implemented test")
  //    final String txt = "vertx is awesome";
  //    DnsClient dns = prepareDns(TestDnsServer.testResolveTXT(txt));
  //
  //    dns.resolveTXT("vertx.io", new Handler<AsyncResult<List<String>>>() {
  //      @Override
  //      public void handle(AsyncResult<List<String>> event) {
  //        tu.checkThread();
  //        List<String> result = event.result();
  //        tu.azzert(!result.isEmpty());
  //        tu.azzert(result.size() == 1);
  //        tu.azzert(txt.equals(result.get(0)));
  //        tu.testComplete();
  //      }
  //    });

  @Test
  def testResolveNS(): Unit = fail("not implemented test")
  //    final String ns = "ns.vertx.io";
  //    DnsClient dns = prepareDns(TestDnsServer.testResolveNS(ns));
  //
  //    dns.resolveNS("vertx.io", new Handler<AsyncResult<List<String>>>() {
  //      @Override
  //      public void handle(AsyncResult<List<String>> event) {
  //        tu.checkThread();
  //        List<String> result = event.result();
  //        tu.azzert(!result.isEmpty());
  //        tu.azzert(result.size() == 1);
  //        tu.azzert(ns.equals(result.get(0)));
  //        tu.testComplete();
  //      }
  //    });

  @Test
  def testResolveCNAME(): Unit = fail("not implemented test")
  //    final String cname = "cname.vertx.io";
  //    DnsClient dns = prepareDns(TestDnsServer.testResolveCNAME(cname));
  //
  //    dns.resolveCNAME("vertx.io", new Handler<AsyncResult<List<String>>>() {
  //      @Override
  //      public void handle(AsyncResult<List<String>> event) {
  //        tu.checkThread();
  //        List<String> result = event.result();
  //        tu.azzert(!result.isEmpty());
  //        tu.azzert(result.size() == 1);
  //
  //        String record = result.get(0);
  //
  //        tu.azzert(!record.isEmpty());
  //        tu.azzert(cname.equals(record));
  //        tu.testComplete();
  //      }
  //    });

  @Test
  def testResolvePTR(): Unit = fail("not implemented test")
  //    final String ptr = "ptr.vertx.io";
  //    DnsClient dns = prepareDns(TestDnsServer.testResolvePTR(ptr));
  //
  //    dns.resolvePTR("10.0.0.1.in-addr.arpa", new Handler<AsyncResult<String>>() {
  //      @Override
  //      public void handle(AsyncResult<String> event) {
  //        tu.checkThread();
  //        String result = event.result();
  //        tu.azzert(result != null);
  //
  //        tu.azzert(ptr.equals(result));
  //        tu.testComplete();
  //      }
  //    });

  @Test
  def testResolveSRV(): Unit = fail("not implemented test")
  //    final int priority = 10;
  //    final int weight = 1;
  //    final int port = 80;
  //    final String target = "vertx.io";
  //
  //    DnsClient dns = prepareDns(TestDnsServer.testResolveSRV(priority, weight, port, target));
  //
  //    dns.resolveSRV("vertx.io", new Handler<AsyncResult<List<SrvRecord>>>() {
  //      @Override
  //      public void handle(AsyncResult<List<SrvRecord>> event) {
  //        tu.checkThread();
  //        List<SrvRecord> result = event.result();
  //        tu.azzert(!result.isEmpty());
  //        tu.azzert(result.size() == 1);
  //
  //        SrvRecord record = result.get(0);
  //
  //        tu.azzert(priority == record.priority());
  //        tu.azzert(weight == record.weight());
  //        tu.azzert(port == record.port());
  //        tu.azzert(target.equals(record.target()));
  //
  //        tu.testComplete();
  //      }
  //    });

  @Test
  def testLookup4(): Unit = fail("not implemented test")
  //    final String ip = "10.0.0.1";
  //    DnsClient dns = prepareDns(TestDnsServer.testLookup4(ip));
  //
  //    dns.lookup4("vertx.io", new Handler<AsyncResult<Inet4Address>>() {
  //      @Override
  //      public void handle(AsyncResult<Inet4Address> event) {
  //        tu.checkThread();
  //        InetAddress result = event.result();
  //        tu.azzert(result != null);
  //        tu.azzert(ip.equals(result.getHostAddress()));
  //        tu.testComplete();
  //      }
  //    });

  @Test
  def testLookup6(): Unit = fail("not implemented test")
  //    DnsClient dns = prepareDns(TestDnsServer.testLookup6());
  //
  //    dns.lookup6("vertx.io", new Handler<AsyncResult<Inet6Address>>() {
  //      @Override
  //      public void handle(AsyncResult<Inet6Address> event) {
  //        tu.checkThread();
  //        Inet6Address result = event.result();
  //        tu.azzert(result != null);
  //        tu.azzert(Arrays.equals(IP6_BYTES, result.getAddress()));
  //        tu.testComplete();
  //      }
  //    });

  @Test
  def testLookup(): Unit = fail("not implemented test")
  //    final String ip = "10.0.0.1";
  //    DnsClient dns = prepareDns(TestDnsServer.testLookup(ip));
  //
  //    dns.lookup("vertx.io", new Handler<AsyncResult<InetAddress>>() {
  //      @Override
  //      public void handle(AsyncResult<InetAddress> event) {
  //        tu.checkThread();
  //        InetAddress result = event.result();
  //        tu.azzert(result != null);
  //        tu.azzert(ip.equals(result.getHostAddress()));
  //        tu.testComplete();
  //      }
  //    });

  @Test
  def testLookupNonExisting(): Unit = fail("not implemented test")
  //    DnsClient dns = prepareDns(TestDnsServer.testLookupNonExisting());
  //    dns.lookup("gfegjegjf.sg1", new Handler<AsyncResult<InetAddress>>() {
  //      @Override
  //      public void handle(AsyncResult<InetAddress> event) {
  //        tu.checkThread();
  //        DnsException cause = (DnsException) event.cause();
  //        tu.azzert(cause.code() == DnsResponseCode.NXDOMAIN);
  //        tu.testComplete();
  //      }
  //    });

  @Test
  def testReverseLookupIpv4(): Unit = fail("not implemented test")
  //    final byte[] address = InetAddress.getByName("10.0.0.1").getAddress();
  //    final String ptr = "ptr.vertx.io";
  //    DnsClient dns = prepareDns(TestDnsServer.testReverseLookup(ptr));
  //
  //    dns.reverseLookup("10.0.0.1", new Handler<AsyncResult<InetAddress>>() {
  //      @Override
  //      public void handle(AsyncResult<InetAddress> event) {
  //        tu.checkThread();
  //        InetAddress result = event.result();
  //        tu.azzert(result != null);
  //        tu.azzert(result instanceof Inet4Address);
  //        tu.azzert(ptr.equals(result.getHostName()));
  //        tu.azzert(Arrays.equals(address, result.getAddress()));
  //        tu.testComplete();
  //      }
  //    });

  @Test
  def testReverseLookupIpv6(): Unit = fail("not implemented test")
  //    final byte[] address = InetAddress.getByName("::1").getAddress();
  //    final String ptr = "ptr.vertx.io";
  //
  //    DnsClient dns = prepareDns(TestDnsServer.testReverseLookup(ptr));
  //
  //    dns.reverseLookup("::1", new Handler<AsyncResult<InetAddress>>() {
  //      @Override
  //      public void handle(AsyncResult<InetAddress> event) {
  //        tu.checkThread();
  //        InetAddress result = event.result();
  //        tu.azzert(result != null);
  //        tu.azzert(result instanceof Inet6Address);
  //        tu.azzert(ptr.equals(result.getHostName()));
  //        tu.azzert(Arrays.equals(address, result.getAddress()));
  //        tu.testComplete();
  //      }
  //    });

  //  private DnsClient prepareDns(TestDnsServer server) throws Exception {
  //    dnsServer = server;
  //    dnsServer.start();
  //    InetSocketAddress addr = (InetSocketAddress) dnsServer.getTransports()[0].getAcceptor().getLocalAddress();
  //    return vertx.createDnsClient(addr);
  //  }

}