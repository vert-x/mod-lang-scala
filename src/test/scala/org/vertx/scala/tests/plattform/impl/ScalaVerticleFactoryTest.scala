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
package org.vertx.scala.tests.plattform.impl

import org.junit.Test
import org.vertx.scala.testtools.TestVerticle
import org.vertx.scala.platform.impl.ScalaVerticleFactory
import org.vertx.testtools.VertxAssert._

/**
 * Scala verticle factory tests.
 *
 * @author Galder Zamarreño
 */
class ScalaVerticleFactoryTest extends TestVerticle {

  @Test
  def createVerticleWithSystemPath(): Unit = {
    val factory = createScalaVerticleFactory()
    val path = "src/test/scala/org/vertx/scala/tests/lang/VerticleClass.scala"
    val verticle = factory.createVerticle(path)
    verticle.start()
    assertHttpClientGetNow("Hello verticle class!")
  }

  @Test
  def createVerticleWithUnmatchingPathAndClassName(): Unit = {
    val factory = createScalaVerticleFactory()
    val path = "src/test/scala/org/vertx/scala/tests/lang/UnsupportedVerticleClass.scala"
    try {
      factory.createVerticle(path)
      fail("Should have failed with an exception")
    } catch {
      case e: ClassNotFoundException => testComplete() // expected
    }
  }

  private def createScalaVerticleFactory(): ScalaVerticleFactory = {
    val factory = new ScalaVerticleFactory
    factory.init(vertx.asJava, container.asJava, this.getClass.getClassLoader)
    factory
  }

  private def assertHttpClientGetNow(expected: String) {
    val client = vertx.createHttpClient().setPort(8080)
    client.getNow("/", {h =>
      assertThread()
      h.bodyHandler { data => {
          assertThread()
          assertEquals(expected, data.toString())
          testComplete()
        }
      }
    })
  }

}
