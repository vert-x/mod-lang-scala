package org.vertx.scala.core

import org.vertx.java.core.eventbus.{ EventBus => JEventBus }
import org.vertx.java.core.Handler

/**
 * @author <a href="http://www.campudus.com/">Joern Bernhardt</a>
 */
package object eventbus {
  trait MessageType[T] {
    def publish(eventBus: EventBus, address: String, message: T): EventBus
    def send(eventBus: EventBus, address: String, message: T): EventBus
    def send[RT: EventBusType](eventBus: EventBus, address: String, message: T, replyHandler: RT => Unit): EventBus

    def wrap(eventBus: EventBus, x: => Unit): EventBus = {
      x
      eventBus
    } 
  }

  implicit object StringElem extends MessageType[String] {
    def publish(eventBus: EventBus, address: String, message: String): EventBus = wrap(eventBus, eventBus.toJava.publish(address, message))
    def send(eventBus: EventBus, address: String, message: String): EventBus = wrap(eventBus, eventBus.toJava.send(address, message))
    def send[RT: EventBusType](eventBus: EventBus, address: String, message: String, replyHandler: Handler[_]): EventBus = wrap(eventBus, eventBus.toJava.send(address, message, replyHandler))
  } 
}