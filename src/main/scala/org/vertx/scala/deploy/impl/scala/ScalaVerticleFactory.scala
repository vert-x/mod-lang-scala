package org.vertx.scala.deploy.impl.scala

import org.vertx.java.core.impl.VertxInternal
import org.vertx.java.deploy.impl.VerticleManager
import org.vertx.java.deploy.VerticleFactory
import org.vertx.java.deploy.impl.VertxLocator
import org.vertx.scala.core.Vertx
import org.vertx.scala.deploy.Container
import org.vertx.scala.deploy.Verticle

class ScalaVerticleFactory extends VerticleFactory {

  protected val LANGUAGE: String = "scala"

  protected val PREFIX: String = "scala:"

  protected val SUFFIX: String = ".scala"

  private var manager: VerticleManager = null

  def init(amanager: VerticleManager): Unit = {
    manager = amanager
  }

  def getLanguage(): String = { LANGUAGE }

  def isFactoryFor(main: String): Boolean = {
    main.startsWith(PREFIX) || main.endsWith(SUFFIX)
  }

  @throws(classOf[Exception])
  def createVerticle(main: String, parent: ClassLoader): org.vertx.java.deploy.Verticle = {

    if (main.endsWith(SUFFIX)) {
      throw new RuntimeException("scala scripts are not yet supported")
    }

    var className: String = null
    if (main.startsWith(PREFIX)) {
      className = main.replaceFirst(PREFIX, "")
    }
    else {
      className = main
    }

    val rawClass = Class.forName(className, true, parent)
    val verticle = rawClass.newInstance().asInstanceOf[Verticle]

    new ScalaVerticle(verticle)
  }

  def reportException(t: Throwable): Unit = {
    manager.getLogger().error("oops!", t);
  }

}