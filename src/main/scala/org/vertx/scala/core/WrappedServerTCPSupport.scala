package org.vertx.scala.core

import org.vertx.java.core.ServerTCPSupport

/**
 * @author <a href="http://www.campudus.com/">Joern Bernhardt</a>
 */
trait WrappedServerTCPSupport extends WrappedTCPSupport {
  override type InternalType <: ServerTCPSupport[_]

  /**
   * Set the accept backlog
   * @return a reference to this so multiple method calls can be chained together
   */
  def setAcceptBacklog(backlog: Int): this.type = wrap(internal.setAcceptBacklog(backlog))

  /**
   *
   * @return The accept backlog
   */
  def getAcceptBacklog(): Int = internal.getAcceptBacklog()
}