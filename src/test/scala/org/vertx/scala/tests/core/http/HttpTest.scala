package org.vertx.scala.tests.core.http

import org.vertx.scala.core.Vertx
import org.vertx.scala.testtools.TestVerticle

final class HttpTest extends TestVerticle with HttpTestBase {

  override def compression: Boolean = false
  override def getVertx: Vertx = vertx

}
