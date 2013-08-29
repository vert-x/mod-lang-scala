package org.vertx.scala

trait Wrap {

  protected[this] def wrap[X](doStuff: => X): this.type = {
    doStuff
    this
  }

}