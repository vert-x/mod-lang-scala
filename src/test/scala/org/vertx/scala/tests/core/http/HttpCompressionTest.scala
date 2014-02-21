/*
 * Copyright 2014 the original author or authors.
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
package org.vertx.scala.tests.core.http

import org.junit.Test
import org.vertx.scala.tests.util.TestUtils._
import org.vertx.testtools.VertxAssert._

class HttpCompressionTest extends HttpTest {

  compression = true

  @Test override def sendFile(): Unit = {
    val (file, content) = generateRandomContentFile("test-send-file.html", 10000)
    checkServer(vertx.createHttpServer(), _.response().sendFile(file.getAbsolutePath)) { c =>
      c.getNow("some-uri", { res =>
        assertEquals(200, res.statusCode())
        assertTrue(res.headers().entryExists("content-type", _ == "text/html"))
        res.bodyHandler { buff =>
          assertEquals(content, buff.toString())
          file.delete()
          testComplete()
        }
      })
    }
  }

  @Test override def sendFileWithHandler(): Unit = {
    val (file, content) = generateRandomContentFile("test-send-file.html", 10000)
    var sendComplete = false
    checkServer(vertx.createHttpServer(), _.response().sendFile(file.getAbsolutePath, { res =>
      sendComplete = true
    } )) { c =>
      c.getNow("some-uri", { res =>
        assertEquals(200, res.statusCode())
        assertTrue(res.headers().entryExists("content-type", _ == "text/html"))
        res.bodyHandler { buff =>
          assertEquals(content, buff.toString())
          file.delete()
          assertTrue(sendComplete)
          testComplete()
        }
      })
    }
  }

  @Test override def sendFileOverrideHeaders(): Unit = {
    val (file, content) = generateRandomContentFile("test-send-file.html", 10000)
    checkServer(vertx.createHttpServer(),
      _.response().putHeader("Content-Type", "wibble").sendFile(file.getAbsolutePath)
    ) { c =>
      c.getNow("some-uri", { res =>
        assertEquals(200, res.statusCode())
        assertTrue(res.headers().entryExists("content-type", _ == "wibble"))
        res.bodyHandler { buff =>
          assertEquals(content, buff.toString())
          file.delete()
          testComplete()
        }
      })
    }
  }

}
