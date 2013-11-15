package org.vertx.scala.mods

class UnknownActionException(msg: String = null, cause: Throwable = null) extends BusModException(msg, cause, "UNKNOWN_ACTION")
