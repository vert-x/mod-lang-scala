package org.vertx.scala.core

trait ClientConfigurer {

  def close():Unit

  def bossThreads():Int

  def connectTimeout():Long

  def keyStorePassword():String

  def keyStorePassword(keyStorePassword: String):ClientConfigurer.this.type

  def keyStorePath():String

  def keyStorePath(keyStorePath: String):ClientConfigurer.this.type

  def receiveBufferSize():Int

  def receiveBufferSize(receiveBufferSize: Int):ClientConfigurer.this.type

  def sendBufferSize():Int

  def sendBufferSize(sendBufferSize: Int):ClientConfigurer.this.type

  def trafficClass():Int

  def trafficClass(trafficClass: Int):ClientConfigurer.this.type

  def trustStorePassword():String

  def trustStorePassword(password: String):ClientConfigurer.this.type

  def trustStorePath():String

  def trustStorePath(path: String):ClientConfigurer.this.type

}