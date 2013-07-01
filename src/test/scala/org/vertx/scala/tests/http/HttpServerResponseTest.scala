/*
 * Copyright 2011-2013 the original author or authors.
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
 
package org.vertx.scala.tests.http

import org.vertx.java.core.http.{HttpServerResponse => JHttpServerResponse}
import org.vertx.java.core.{MultiMap => JMultiMap, Handler}
import org.vertx.java.core.impl.CaseInsensitiveMultiMap
import org.vertx.java.core.buffer.Buffer
import java.lang.Iterable
import org.junit.Test
import org.vertx.scala.core.http.HttpServerResponse
import org.junit.Assert._

/**
 * @author Ranie Jade Ramiso
 */
class HttpServerResponseTest {
  // stub implementation
  class StubHttpServerResponse extends JHttpServerResponse {
    private val headerMap:JMultiMap = new CaseInsensitiveMultiMap
    private val trailerMap:JMultiMap = new CaseInsensitiveMultiMap

    def close() {}

    def closeHandler(handler: Handler[Void]): JHttpServerResponse = ???

    def end() {}

    def end(chunk: Buffer) {}

    def end(chunk: String) {}

    def end(chunk: String, enc: String) {}

    def getStatusCode: Int = ???

    def getStatusMessage: String = ???

    def headers(): JMultiMap = headerMap

    def isChunked: Boolean = ???

    def putHeader(name: String, value: String): JHttpServerResponse = {
      headerMap.add(name, value)
      StubHttpServerResponse.this
    }

    def putHeader(name: String, values: Iterable[String]): JHttpServerResponse = {
      headerMap.add(name, values)
      StubHttpServerResponse.this
    }

    def putTrailer(name: String, value: String): JHttpServerResponse = {
      trailerMap.add(name, value)
      StubHttpServerResponse.this
    }

    def putTrailer(name: String, values: Iterable[String]): JHttpServerResponse = {
      trailerMap.add(name, values)
      StubHttpServerResponse.this
    }

    def sendFile(filename: String): JHttpServerResponse = ???

    def sendFile(filename: String, notFoundFile: String): JHttpServerResponse = ???

    def setChunked(chunked: Boolean): JHttpServerResponse = ???

    def setStatusCode(statusCode: Int): JHttpServerResponse = ???

    def setStatusMessage(statusMessage: String): JHttpServerResponse = ???

    def trailers(): JMultiMap = trailerMap

    def write(chunk: Buffer): JHttpServerResponse = ???

    def write(chunk: String): JHttpServerResponse = ???

    def write(chunk: String, enc: String): JHttpServerResponse = ???

    def drainHandler(handler: Handler[Void]): JHttpServerResponse = ???

    def setWriteQueueMaxSize(maxSize: Int): JHttpServerResponse = ???

    def writeQueueFull(): Boolean = ???

    def exceptionHandler(handler: Handler[Throwable]): JHttpServerResponse = ???
  }

  @Test
  def putHttpHeaderTest() {
    val response = new HttpServerResponse(new StubHttpServerResponse)

    response.putHeader("content-type", "text/plain")
    response.putHeader("some-non-standard-header", "some-value")

    val headers = response.headers

    assertTrue(headers.entryExists("content-type",  _ == "text/plain"))
    assertTrue(headers.entryExists("some-non-standard-header", _ == "some-value"))
    assertFalse(headers.entryExists("does-not-exist", _ == "unknown"))
  }

  @Test
  def putTrailerTest() {
    val response = new HttpServerResponse(new StubHttpServerResponse)

    response.putTrailer("trailer1", "value1")
    response.putTrailer("trailer2", "value2")

    val trailers = response.trailers
    assertTrue(trailers.entryExists("trailer1", _ == "value1"))
    assertTrue(trailers.entryExists("trailer2", _ == "value2"))
    assertFalse(trailers.entryExists("trailer3", _ == "value3"))
  }
}
