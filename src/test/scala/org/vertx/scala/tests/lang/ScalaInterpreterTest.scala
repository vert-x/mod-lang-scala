package org.vertx.scala.tests.lang

import java.io.{File, PrintWriter, StringWriter, Writer}

import org.junit.Assert.fail
import org.junit.Test
import org.vertx.scala.lang.{ClassLoaders, ScalaInterpreter}
import org.vertx.scala.platform.Verticle
import org.vertx.scala.platform.impl.ScalaVerticle
import org.vertx.scala.testtools.TestVerticle
import org.vertx.testtools.VertxAssert._

import scala.tools.nsc.Settings
import scala.util.{Failure, Success}

class ScalaInterpreterTest extends TestVerticle {

  @Test
  def runScriptTest(): Unit = {
    val path = new File("src/test/scripts/VerticleScript.scala").toURI.toURL
    val out = new StringWriter()
    val interpreter = createInterpreter(out)
    interpreter.runScript(path) match {
      case Success(s) =>
        assertHttpClientGetNow("Hello verticle script!")
      case Failure(ex) =>
        println(out.toString)
        fail(ex.toString)
    }
  }

  @Test
  def runClassTest(): Unit = {
    val filePath = "src/test/scala/org/vertx/scala/tests/lang/VerticleClass.scala"
    val out = new StringWriter()
    val interpreter = createInterpreter(out)
    val classLoader = interpreter.compileClass(new File(filePath))
    val className = "org.vertx.scala.tests.lang.VerticleClass"
    val delegate = ClassLoaders.newInstance[Verticle](className, classLoader.get)
    val verticle = ScalaVerticle.newVerticle(delegate.get, vertx, container)
    verticle.start()
    assertHttpClientGetNow("Hello verticle class!")
  }

  private def createInterpreter(out: Writer): ScalaInterpreter = {
    val settings = new Settings()
    settings.usejavacp.value = true
    settings.verbose.value = ScalaInterpreter.isVerbose
    new ScalaInterpreter(settings, vertx, container, new PrintWriter(out))
  }

  private def assertHttpClientGetNow(expected: String) {
    val client = vertx.createHttpClient().setPort(8080)
    client.getNow("/", { h =>
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
