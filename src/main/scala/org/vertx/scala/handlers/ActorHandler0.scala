package org.vertx.scala.handlers

import org.vertx.java.core.Handler
import scala.actors.Actor
import scala.actors.Reactor

class ActorHandler0(delegate: () => Actor) extends Handler[Void] with Reactor[Void] {

  def handle(message: Void):Unit = {
    // Hmm
  }

  override def act = {
    // Hmm
  }

}