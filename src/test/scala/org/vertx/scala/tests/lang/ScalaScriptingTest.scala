package org.vertx.scala.tests.lang

import org.vertx.testtools.ScriptClassRunner
import org.junit.runner.RunWith
import org.junit.Test
import org.vertx.testtools.TestVerticleInfo

/**
 * This is dummy JUnit test class which is used to run any Scala test scripts as JUnit tests.
 *
 * The scripts by default go in src/test/resources/integration_tests.
 *
 * If you don't have any Scala tests in your project you can delete this
 *
 * You do not need to edit this file unless you want it to look for tests elsewhere
 */
@TestVerticleInfo(filenameFilter = ".+\\.scala", funcRegex = "def[\\s]+(test[^\\s(]+)")
@RunWith(classOf[ScriptClassRunner])
class ScalaScriptingTest {
  @Test
  def __vertxDummy(): Unit = {}
}