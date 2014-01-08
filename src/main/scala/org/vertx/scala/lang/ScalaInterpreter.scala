package org.vertx.scala.lang

import java.io.{ File, PrintWriter }
import scala.annotation.tailrec
import scala.io.Source
import scala.reflect.internal.util.BatchSourceFile
import scala.reflect.io.Path.jfile2path
import scala.reflect.io.PlainFile
import scala.tools.nsc.Settings
import scala.tools.nsc.interpreter.{ IMain, NamedParam }
import scala.tools.nsc.interpreter.Results.Result
import scala.tools.nsc.interpreter.Results.{ Error => InterpreterError }
import scala.tools.nsc.interpreter.Results.{ Incomplete => InterpreterIncomplete}
import scala.tools.nsc.interpreter.Results.{ Success => InterpreterSuccess }
import org.vertx.scala.core.Vertx
import scala.tools.nsc.NewLinePrintWriter
import scala.tools.nsc.ConsoleWriter
import java.net.URL
import org.vertx.scala.platform.Container
import scala.util.{Success, Failure, Try}

/**
 * Scala interpreter
 *
 * @author Galder Zamarreño
 */
class ScalaInterpreter(
    settings: Settings,
    vertx: Vertx,
    container: Container,
    out: PrintWriter = new NewLinePrintWriter(new ConsoleWriter, true)) {

  private val interpreter = new IMain(settings, out)
  interpreter.setContextClassLoader()

  def addBootClasspathJar(path: String): Unit =
    settings.bootclasspath.append(path)

  def close(): Unit = interpreter.close()

  def runScript(script: URL): Try[Unit] = {
    val content = Source.fromURL(script).mkString
    val ops = List(
      () => addImports(
        "org.vertx.scala._",
        "org.vertx.scala.core._",
        "org.vertx.scala.core.buffer._",
        "org.vertx.scala.core.dns._",
        "org.vertx.scala.core.eventbus._",
        "org.vertx.scala.core.file._",
        "org.vertx.scala.core.http._",
        "org.vertx.scala.core.json._",
        "org.vertx.scala.core.net._",
        "org.vertx.scala.core.streams._"
      ),
      () => bind("vertx", "org.vertx.scala.core.Vertx", vertx),
      () => bind("container", "org.vertx.scala.platform.Container", container),
      () => interpret(content)
    )
    val result = interpret(ops, InterpreterIncomplete)
    result match {
      case InterpreterError => Failure(new ScalaCompilerError(s"Interpreter error running $script"))
      case InterpreterIncomplete => Failure(new ScalaCompilerError(s"Incomplete script $script"))
      case InterpreterSuccess => Success()
    }
  }

  private def addImports(ids: String*): Result =
    verboseOrQuiet(
      interpreter.addImports(ids: _*),
      interpreter.quietImport(ids: _*))

  private def bind(name: String, boundType: String, value: Any): Result =
    verboseOrQuiet(
      interpreter.bind(name, boundType, value),
      interpreter.quietBind(NamedParam(name, boundType, value)))

  private def interpret(content: String): Result =
    verboseOrQuiet(
      interpreter.interpret(content),
      interpreter.beSilentDuring(interpreter.interpret(content)))

  private def verboseOrQuiet(verbose: => Result, quiet: => Result): Result = {
    if (ScalaInterpreter.isVerbose) verbose else quiet
  }

  def compileClass(classFile: File): Try[ClassLoader] = {
    val source = new BatchSourceFile(PlainFile.fromPath(classFile))
    val result = interpreter.compileSources(source)
    if (result)
      Success(interpreter.classLoader)
    else
      Failure(new IllegalArgumentException(s"Unable to compile $classFile"))
  }

  @tailrec
  private def interpret(ops: List[() => Result], accumulate: Result): Result = {
    ops match {
      case List() => accumulate
      case x :: xs =>
        val result = x()
        result match {
          case InterpreterError | InterpreterIncomplete => result
          case InterpreterSuccess => interpret(xs, result)
        }
    }
  }

  private class ScalaCompilerError(message: String) extends Exception(message)
}

object ScalaInterpreter {

  def isVerbose: Boolean =
    System.getProperty("vertx.scala.interpreter.verbose", "false").toBoolean

}