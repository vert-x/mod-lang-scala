package org.vertx.scala.mods

case class BusModException(message: String = null, cause: Throwable = null, id: String = "MODULE_EXCEPTION") extends Exception(message, cause)
