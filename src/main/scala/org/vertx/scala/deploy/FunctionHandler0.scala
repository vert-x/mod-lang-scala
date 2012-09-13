package org.vertx.scala.deploy

import org.vertx.java.core.Handler

class FunctionHandler0(delegate: () => Unit) extends Handler[Void] {

  def handle(message: Void) {
    delegate()
  }

  override def hashCode():Int = {
    delegate.hashCode() * 23
  }

  override def equals(obj: Any):Boolean = {
    delegate.equals(obj)
  }

  override def toString():String = {
    delegate.toString()
  }

}
