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
//import org.vertx.scala.core.Vertx
//import org.vertx.java.core.{Vertx => JVertx}
//import org.vertx.java.platform.{Verticle => JVerticle}
//import org.vertx.java.platform.{Container => JContainer}
import org.vertx.java.core.{Vertx => JVertx}
import org.vertx.java.platform.{Container => JContainer}
import org.vertx.java.platform.{Verticle => JVerticle}
import org.vertx.scala.platform.Verticle
import org.vertx.scala.platform.Container

/**
 * @author swilliams
 * 
 */
object ScalaVerticle {
  def apply(delegate: => Verticle) = 
    new ScalaVerticle(delegate)
}

final private[platform] class ScalaVerticle(delegate: => Verticle) extends JVerticle {

  override def setContainer(jcontainer: JContainer) {
    delegate.container = Container(jcontainer)
    super.setContainer(jcontainer)
  }

  override def setVertx(jvertx: JVertx) {
    delegate.vertx = org.vertx.scala.core.Vertx(jvertx)
    super.setVertx(jvertx)
  }

  @throws(classOf[Exception])
  override def start(): Unit = delegate.start

  @throws(classOf[Exception])
  override def stop(): Unit = delegate.stop

}