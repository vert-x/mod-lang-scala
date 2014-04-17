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
package org.vertx.scala.tests.plattform.impl

import org.vertx.scala.platform.Verticle
import org.vertx.scala.testtools.TestVerticle
import org.vertx.scala.tests.lang.VerticleClass
import org.vertx.scala.core.json.Json
import org.hamcrest.Matchers.startsWith
import org.junit.{After, Before, Test}
import org.vertx.java.platform.{PlatformManager, PlatformLocator}
import org.vertx.scala.core.FunctionConverters._
import java.net.{URLClassLoader, URL}
import scala.collection.mutable
import org.vertx.scala.core.AsyncResult
import scala.concurrent.{Await, Promise}
import scala.concurrent.duration._
import org.junit.Assert._

/**
 * @author Galder Zamarreño
 */
class SystemPropertyDefaultLangOverrideTest {

  var mgr: PlatformManager = _

  @Before def beforeTest() {
    System.setProperty("vertx.langs..", "scala") // override default via system property
    mgr = PlatformLocator.factory.createPlatformManager
  }

  @After def afterTest() {
    System.clearProperty("vertx.langs..")
    mgr.stop()
  }

  @Test def testDeployVerticle(): Unit = {
    val p = Promise[String]()
    mgr.deployVerticle(classOf[VerticleClass].getName, null, findURLs().orNull, 1, null, { res: AsyncResult[String] =>
      if (res.succeeded()) p.success(res.result())
      else p.failure(res.cause())
      ()
    })
    assertThat(Await.result(p.future, 10 second), startsWith("deployment-"))
  }

  private def findURLs(): Option[Array[URL]] = {
    val urls = mutable.ListBuffer[URL]()
    val pcl = Thread.currentThread.getContextClassLoader
    pcl match {
      case u: URLClassLoader =>
        for (url <- u.getURLs) {
          val stringUrl = url.toString
          if (!stringUrl.endsWith(".jar") && !stringUrl.endsWith(".zip"))
            urls += url
        }
        Some(urls.toArray)
      case _ => None
    }
  }

}
