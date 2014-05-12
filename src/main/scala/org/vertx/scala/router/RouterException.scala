package org.vertx.scala.router

/**
 * @author <a href="http://www.campudus.com/">Joern Bernhardt</a>
 */
case class RouterException(message: String = "",
                           cause: Throwable = null,
                           id: String = "UNKNOWN_SERVER_ERROR",
                           statusCode: Int = 500)
  extends Exception(message, cause)
