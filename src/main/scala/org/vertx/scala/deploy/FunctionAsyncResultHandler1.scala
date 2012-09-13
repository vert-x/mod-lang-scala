package org.vertx.scala.deploy

import org.vertx.java.core.AsyncResult
import org.vertx.java.core.AsyncResultHandler

class FunctionAsyncResultHandler1[T](delegate: (AsyncResult[T]) => Unit) extends AsyncResultHandler[T] {

  def handle(message: AsyncResult[T]):Unit = {
    delegate(message)
  }

}
