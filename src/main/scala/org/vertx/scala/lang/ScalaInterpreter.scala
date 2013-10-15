package org.vertx.scala.lang

import java.io.{ File, PrintWriter }
import scala.annotation.tailrec
import scala.io.Source
import scala.reflect.internal.util.BatchSourceFile
import scala.reflect.io.Path.jfile2path
import scala.reflect.io.PlainFile
import scala.tools.nsc.Settings
import scala.tools.nsc.interpreter.{ IMain, NamedParam }
import scala.tools.nsc.interpreter.Results.{ Error, Incomplete, Result, Success }
import org.vertx.scala.core.Vertx
import scala.tools.nsc.NewLinePrintWriter
import scala.tools.nsc.ConsoleWriter

/**
 * Scala interpreter
 *
 * @author Galder Zamarreño
 */
class ScalaInterpreter(settings: Settings, vertx: Vertx, out: PrintWriter = new NewLinePrintWriter(new ConsoleWriter, true)) {

  private val interpreter = new IMain(settings, out)
  interpreter.setContextClassLoader()

  def addBootClasspathJar(path: String): Unit =
    settings.bootclasspath.append(path)

  def close(): Unit = interpreter.close()

  def runScript(script: File): Result = {
    println("Running script " + script.getAbsolutePath())
    val content = Source.fromFile(script)
    val ops = List(
      ("<internal adding imports>", () => addImports(
        "org.vertx.scala._",
        "org.vertx.scala.core._",
        "org.vertx.scala.core.http._")),
      ("<internal adding vertx>", () => bind("vertx", "org.vertx.scala.core.Vertx", vertx)))
    interpret(ops ::: content.getLines.map(l => (l, () => interpret(l))).toList, -1, Incomplete)
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
      interpreter.quietRun(content))

  private def verboseOrQuiet(verbose: => Result, quiet: => Result): Result = {
    if (ScalaInterpreter.isVerbose) verbose else quiet
  }

  def compileClass(classFile: File, className: String): Option[Class[_]] = {
    val source = new BatchSourceFile(PlainFile.fromPath(classFile))
    val result = interpreter.compileSources(source)
    if (result) Some(interpreter.classLoader.loadClass(className)) else None
  }

  @tailrec
  private def interpret(ops: List[(String, () => Result)], line: Int, accumulate: Result): Result = {
    ops match {
      case List() => accumulate
      case x :: xs =>
        val result = x._2()
        result match {
          case Error =>
            println("error when interpreting line " + line + ": " + x._1)
            result
          case Incomplete =>
            println("interpret incomplete at line " + line + ": " + x._1)
            result
          case Success => interpret(xs, line + 1, result)
        }
    }
  }

}

object ScalaInterpreter {

  def isVerbose: Boolean =
    System.getProperty("vertx.scala.interpreter.verbose", "false").toBoolean

}