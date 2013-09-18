package org.vertx.scala.testtools

import org.junit.runner.notification.RunNotifier
import org.vertx.testtools.JavaClassRunner
import org.junit.runner.Description
import org.junit.runners.model.FrameworkMethod

class ScalaClassRunner(klass: Class[_]) extends JavaClassRunner(klass) {
  println("init scala class runner")

  override protected def computeTestMethods(): java.util.List[FrameworkMethod] = {
    println("computing test methods")
    val testClass = getTestClass().getJavaClass()
    if (!(classOf[TestVerticle].isAssignableFrom(testClass))) {
      throw new IllegalArgumentException("Scala Test classes must extend org.vertx.scala.testtools.TestVerticle")
    }
    this.main = "scala:" + testClass.getName()
    println("this.main: " + this.main)
    getTestMethods()
  }

}