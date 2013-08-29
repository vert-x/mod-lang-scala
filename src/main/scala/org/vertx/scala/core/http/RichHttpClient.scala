package org.vertx.scala.core.http

import org.vertx.java.core.http.{ HttpClient => JHttpClient }
import org.vertx.java.core.http.{ WebSocket => JWebSocket }
import org.vertx.scala.core.FunctionConverters._

class RichHttpClient(internal: JHttpClient) extends JHttpClient {
  def connectWebsocket(uri: String)(wsConnect: JWebSocket => Unit): JHttpClient =
    internal.connectWebsocket(uri, convertFunctionToParameterisedHandler(wsConnect))
}
object RichHttpClient {
  implicit def httpClientToRich(httpClient: JHttpClient) = new RichHttpClient(httpClient)
}