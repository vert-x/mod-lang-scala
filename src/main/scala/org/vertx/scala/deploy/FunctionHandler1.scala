package org.vertx.scala.deploy

import org.vertx.java.core.Handler

class FunctionHandler1[T](delegate: (T) => Unit) extends Handler[T] {

  def handle(message: T) {
    delegate(message)
  }

  override def hashCode():Int = {
    delegate.hashCode() * 37
  }

  override def equals(obj: Any):Boolean = {
    delegate.equals(obj)
  }

  override def toString():String = {
    delegate.toString()
  }

}