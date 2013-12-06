package org.vertx.scala.core.http

import org.vertx.java.core.http.{ WebSocketBase => JWebSocketBase }
import org.vertx.scala.core.streams.{WriteStream, ReadStream}
import org.vertx.scala.core.FunctionConverters._
import org.vertx.scala.core.buffer.Buffer

/**
 * Represents an HTML 5 Websocket<p>
 * Instances of this class are created and provided to the handler of an
 * {@link HttpClient} when a successful websocket connect attempt occurs.<p>
 * On the server side, the subclass {@link ServerWebSocket} is used instead.<p>
 * It implements both {@link ReadStream} and {@link WriteStream} so it can be used with
 * {@link org.vertx.java.core.streams.Pump} to pump data with flow control.<p>
 * Instances of this class are not thread-safe<p>
 *
 * @author <a href="http://tfox.org">Tim Fox</a>
 * @author <a href="http://www.campudus.com/">Joern Bernhardt</a>
 * @author Galder Zamarreño
 */
trait WebSocketBase[+S <: WebSocketBase[S]] extends Any
  with ReadStream[S]
  with WriteStream[S] {

  override type J <: JWebSocketBase[J]

  /**
   * When a {@code Websocket} is created it automatically registers an event handler with the eventbus, the ID of that
   * handler is given by {@code binaryHandlerID}.<p>
   * Given this ID, a different event loop can send a binary frame to that event handler using the event bus and
   * that buffer will be received by this instance in its own event loop and written to the underlying connection. This
   * allows you to write data to other websockets which are owned by different event loops.
   */
  def binaryHandlerID(): String = asJava.binaryHandlerID()

  /**
   * Close the websocket
   */
  def close(): Unit = asJava.close()

  /**
   * Set a closed handler on the connection
   */
  def closeHandler(handler: => Unit): S = wrap(asJava.closeHandler(handler))

  /**
   * When a {@code Websocket} is created it automatically registers an event handler with the eventbus, the ID of that
   * handler is given by {@code textHandlerID}.<p>
   * Given this ID, a different event loop can send a text frame to that event handler using the event bus and
   * that buffer will be received by this instance in its own event loop and written to the underlying connection. This
   * allows you to write data to other websockets which are owned by different event loops.
   */
  def textHandlerID(): String = asJava.textHandlerID()

  /**
   * Write {@code data} to the websocket as a binary frame
   */
  def writeBinaryFrame(data: Buffer): S = wrap(asJava.writeBinaryFrame(data.asJava))

  /**
   * Write {@code str} to the websocket as a text frame
   */
  def writeTextFrame(str: String): S = wrap(asJava.writeTextFrame(str))

}