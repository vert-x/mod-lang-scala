package org.vertx.scala

import org.vertx.java.core.file.{AsyncFile => JAsyncFile}
import org.vertx.scala.core.file.AsyncFile
import org.vertx.java.core.eventbus.{EventBus => JEventBus}
import org.vertx.scala.core.eventbus.EventBus
import org.vertx.java.core.http.{ServerWebSocket => JServerWebSocket, WebSocket => JWebSocket}
import org.vertx.scala.core.http.{WebSocket, ServerWebSocket}


trait VertxScalaConverters{

  implicit def toScalaAsycFile(jcomponent:JAsyncFile):AsyncFile = AsyncFile(jcomponent)

  implicit def toScalaEventBus(jbus: JEventBus): EventBus = EventBus(jbus)

  implicit def convertJavaToScalaWebSocket(jsocket: JServerWebSocket):ServerWebSocket = {
    ServerWebSocket(jsocket)
  }

  implicit def convertJavaToScalaWebSocket(jsocket: JWebSocket):WebSocket = {
    WebSocket(jsocket)
  }

}

object VertxConverters extends VertxScalaConverters
