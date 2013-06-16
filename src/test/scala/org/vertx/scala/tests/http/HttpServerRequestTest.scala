package org.vertx.scala.tests.http

import org.vertx.java.core.http.{HttpServerRequest => JHttpServerRequest, HttpServerResponse, HttpServerFileUpload, HttpVersion}
import org.vertx.java.core.{MultiMap => JMultiMap, CaseInsensitiveMultiMap, Handler}
import org.vertx.java.core.buffer.Buffer
import java.net.{URI, InetSocketAddress}
import javax.security.cert.X509Certificate
import org.vertx.java.core.net.NetSocket
import java.util
import org.junit.Test
import org.junit.Assert._
import org.vertx.scala.core.http.HttpServerRequest

/**
 * @author Ranie Jade Ramiso
 */
class HttpServerRequestTest {
  class StubHttpServerRequest extends JHttpServerRequest {
    val headerMap = new CaseInsensitiveMultiMap
    val paramMap = new CaseInsensitiveMultiMap

    def absoluteURI(): URI = ???

    def bodyHandler(bodyHandler: Handler[Buffer]): JHttpServerRequest = ???

    def formAttributes(): util.Map[String, String] = ???

    def headers(): JMultiMap = headerMap

    def method(): String = ???

    def netSocket(): NetSocket = ???

    def params(): JMultiMap = paramMap

    def path(): String = ???

    def peerCertificateChain(): Array[X509Certificate] = ???

    def query(): String = ???

    def remoteAddress(): InetSocketAddress = ???

    def response(): HttpServerResponse = ???

    def uploadHandler(uploadHandler: Handler[HttpServerFileUpload]): JHttpServerRequest = ???

    def uri(): String = ???

    def version(): HttpVersion = ???

    def dataHandler(handler: Handler[Buffer]): JHttpServerRequest = ???

    def endHandler(endHandler: Handler[Void]): JHttpServerRequest = ???

    def pause(): JHttpServerRequest = ???

    def resume(): JHttpServerRequest = ???

    def exceptionHandler(handler: Handler[Throwable]): JHttpServerRequest = ???
  }

  @Test
  def httpHeaderShouldExistTest() {
    val internalRequest = new StubHttpServerRequest

    // add some headers to the internal multimap
    internalRequest.headers.add("header1", "value1")
    internalRequest.headers.add("header2", "value2")

    val request = new HttpServerRequest(internalRequest)

    val headers = request.headers

    assertTrue(headers.entryExists("header1", _ == "value1"))
    assertTrue(headers.entryExists("header2", _ == "value2"))
    assertFalse(headers.entryExists("header2", _ == "value1"))
  }

  @Test
  def httpParamShouldExistTest() {
    val internalRequest = new StubHttpServerRequest

    // add some headers to the internal multimap
    internalRequest.params.add("param1", "value1")
    internalRequest.params.add("param2", "value2")

    val request = new HttpServerRequest(internalRequest)

    val headers = request.params

    assertTrue(headers.entryExists("param1", _ == "value1"))
    assertTrue(headers.entryExists("param2", _ == "value2"))
    assertFalse(headers.entryExists("param2", _ == "value1"))
  }
}
