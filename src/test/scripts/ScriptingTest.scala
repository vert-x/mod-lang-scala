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

import org.vertx.scala.tests.util.TestUtils._
import org.vertx.testtools.VertxAssert._
import org.vertx.testtools.VertxAssert

VertxAssert.initialize(vertx.internal)

vertx.createHttpServer.requestHandler { req: HttpServerRequest =>
  req.response.end("Hello verticle test script!")
}.listen(8080, { ar: AsyncResult[HttpServer] => {
  startTests(this, container)
}})

def testHttpServer(): Unit = {
  vertx.createHttpClient.setPort(8080).getNow("/", { resp => 
    resp.bodyHandler({ buf =>
      assertEquals("Hello verticle test script!", buf.toString)
      testComplete()
    })
  })
}
