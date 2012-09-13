package org.vertx.scala.deploy

import org.vertx.java.core.AsyncResult
import org.vertx.java.core.AsyncResultHandler

class FunctionAsyncResultHandler0(delegate: () => Unit) extends AsyncResultHandler[Void] {

  def handle(message: AsyncResult[Void]):Unit = {
    delegate()
  }

}
