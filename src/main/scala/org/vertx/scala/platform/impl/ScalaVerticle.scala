/*
 * Copyright 2011-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.vertx.scala.platform.impl
import org.vertx.java.core.{ Vertx => JVertx }
import org.vertx.java.platform.{ Container => JContainer }
import org.vertx.java.platform.{ Verticle => JVerticle }
import org.vertx.java.core.Future
import org.vertx.scala.core.Vertx
import org.vertx.scala.platform.Verticle
import org.vertx.scala.platform.Container
import scala.util.Success
import scala.util.Failure
import org.vertx.scala.core.VertxExecutionContext
import scala.concurrent.Promise

/**
 * @author swilliams
 * @author <a href="http://www.campudus.com/">Joern Bernhardt</a>
 */
object ScalaVerticle {
  def newVerticle(delegate: Verticle, vertx: Vertx, container: Container): ScalaVerticle = {
    val verticle = new ScalaVerticle(delegate)
    verticle.setVertx(vertx.asJava)
    verticle.setContainer(container.asJava)
    verticle
  }
}

final private[platform] class ScalaVerticle(delegate: Verticle) extends JVerticle with VertxExecutionContext {

  override def setContainer(jcontainer: JContainer) = {
    delegate.setContainer(new Container(jcontainer))
    super.setContainer(jcontainer)
  }

  override def setVertx(jvertx: JVertx) = {
    delegate.setVertx(new Vertx(jvertx))
    super.setVertx(jvertx)
  }

  override def start(): Unit = {
    delegate.start
  }

  override def start(future: Future[Void]): Unit = {
    val p = Promise[Unit]
    delegate.start()
    delegate.start(p)
    p.future onComplete {
      case Success(_) =>
        future.setResult(null)
      case Failure(x) =>
        future.setFailure(x)
    }
  }

  override def stop(): Unit = delegate.stop

}