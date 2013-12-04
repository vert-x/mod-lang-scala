package org.vertx.scala.tests.lang

import java.io.{File, Writer, StringWriter, PrintWriter}
import org.junit.Test
import scala.tools.nsc.interpreter.Results.{Success, Result}
import org.vertx.scala.testtools.TestVerticle
import org.junit.Assert.fail
import org.vertx.testtools.VertxAssert._
import org.vertx.scala.lang.ScalaInterpreter
import org.vertx.scala.platform.impl.ScalaVerticle
import org.vertx.scala.platform.Verticle
import scala.tools.nsc.Settings

class ScalaInterpreterTest extends TestVerticle {

  @Test
  def runScriptTest(): Unit = {
    val path = new File("src/test/scripts/VerticleScript.scala").toURI.toURL
    val out = new StringWriter()
    val interpreter = createInterpreter(out)
    assertInterpret(out, interpreter.runScript(path))
    assertHttpClientGetNow("Hello verticle script!")
  }

  @Test
  def runClassTest(): Unit = {
    val filePath = "src/test/scala/org/vertx/scala/tests/lang/VerticleClass.scala"
    val out = new StringWriter()
    val interpreter = createInterpreter(out)
    val verticleClass = interpreter.compileClass(new File(filePath),
        "org.vertx.scala.tests.lang.VerticleClass")
    val verticle = ScalaVerticle.newVerticle(
        verticleClass.get.newInstance().asInstanceOf[Verticle], vertx, container)
    verticle.start()
    assertHttpClientGetNow("Hello verticle class!")
  }

  private def createInterpreter(out: Writer): ScalaInterpreter = {
    val settings = new Settings()
    settings.usejavacp.value = true
    settings.verbose.value = ScalaInterpreter.isVerbose
    new ScalaInterpreter(settings, vertx, container, new PrintWriter(out))
  }

  private def assertInterpret(out: Writer, result: Result) {
    if (result != Success)
      fail(out.toString)
  }

  private def assertHttpClientGetNow(expected: String) {
    val client = vertx.createHttpClient().setPort(8080)
    client.getNow("/", {
      h => h.bodyHandler {
        data => {
          assertEquals(expected, data.toString())
          testComplete()
        }
      }
    })
  }

}
