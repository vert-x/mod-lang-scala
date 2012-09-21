package org.vertx.scala.core

trait SocketConfigurer {

  def listen(port: Int):SocketConfigurer.this.type

  def listen(port: Int, address: String):SocketConfigurer.this.type

  def acceptBacklog():Int

  def acceptBacklog(backlog: Int):SocketConfigurer.this.type

  def keyStorePassword():String

  def keyStorePassword(keyStorePassword: String):SocketConfigurer.this.type

  def keyStorePath():String

  def keyStorePath(keyStorePath: String):SocketConfigurer.this.type

  def receiveBufferSize():Int

  def receiveBufferSize(receiveBufferSize: Int):SocketConfigurer.this.type

  def sendBufferSize():Int

  def sendBufferSize(sendBufferSize: Int):SocketConfigurer.this.type

  def trafficClass():Int

  def trafficClass(trafficClass: Int):SocketConfigurer.this.type

  def trustStorePassword():String

  def trustStorePassword(password: String):SocketConfigurer.this.type

  def trustStorePath():String

  def trustStorePath(path: String):SocketConfigurer.this.type

}