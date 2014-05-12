package org.vertx.scala

import org.vertx.scala.core.FunctionConverters._
import org.vertx.scala.core._

import scala.concurrent.{Future, Promise}
import scala.util.{Failure, Success, Try}

/**
 * General helpers for generic scala problems that occur inside the Vert.x code.
 *
 * @author <a href="http://www.campudus.com/">Joern Bernhardt</a>
 */
object FutureOps {

  type ResultHandler[T] = AsyncResult[T] => Unit

  def futurify[T](x: Promise[T] => _): Future[T] = {
    val p = Promise[T]()
    x(p)
    p.future
  }

  def asyncResultToFuture[T](fn: ResultHandler[T] => _): Future[T] = futurify { p: Promise[T] =>
    val t = {
      case Success(result) => p.success(result)
      case Failure(ex) => p.failure(ex)
    }: Try[T] => Unit
    fn(t)
  }

}
