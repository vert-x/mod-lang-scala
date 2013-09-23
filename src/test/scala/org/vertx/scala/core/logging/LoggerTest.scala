package org.vertx.scala.core.logging

import org.vertx.scala.testtools.TestVerticle
import org.junit.Test
import org.vertx.testtools.VertxAssert._
import org.vertx.scala.core.logging._

class LoggerTest extends TestVerticle {

  @Test
  def testLogger() {

    val logger = container.logger()
    
    //assertEquals(logger != null)
    
    logger.info("foo")

    testComplete()
  }
}