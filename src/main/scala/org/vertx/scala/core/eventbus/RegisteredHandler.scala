package org.vertx.scala.core.eventbus

import org.vertx.java.core.{ Handler => JHandler }
import org.vertx.java.core.eventbus.{ Message => JMessage }
import org.vertx.scala.core._
import org.vertx.scala.core.FunctionConverters._

/**
 * A RegisteredHandler can be unregistered at a later point in time.
 * @param address The address the handler is listening on.
 * @param handler The (java) handler that is registered at the address.
 * @param eventbus The event bus where this handler is registered
 *
 * @author <a href="http://www.campudus.com/">Joern Bernhardt</a>
 * @author Galder Zamarreño
 */
case class RegisteredHandler[X <% MessageData] private[scala]
  (address: String, handler: JHandler[JMessage[X]], eventbus: EventBus) {

  /**
   * Unregisters the registered handler from the event bus.
   */
  def unregister(): Unit = eventbus.asJava.unregisterHandler(address, handler)

  /**
   * Unregisters the registered handler from the event bus.
   * @param resultHandler Fires when the unregistration event propagated through the whole cluster.
   */
  def unregister(resultHandler: AsyncResult[Void] => Unit): Unit =
    eventbus.asJava.unregisterHandler(address, handler, resultHandler)

}

