package org.vertx.scala.core

import org.vertx.java.core.net.NetSocket
import org.vertx.scala.deploy.FunctionHandler0
import org.vertx.scala.deploy.FunctionHandler1

class NetServer(internal: org.vertx.java.core.net.NetServer) {

  def close():Unit = {
    internal.close()
  }

  def close(handler: () => Unit):Unit = {
    internal.close(new FunctionHandler0(handler))
  }

  def listen(port: Int):NetServer = {
    internal.listen(port)
    this
  }

  def listen(port: Int, address: String):NetServer = {
    internal.listen(port, address)
    this
  }

  def connectHandler(handler: (NetSocket) => Unit):NetServer = {
    internal.connectHandler(new FunctionHandler1(handler))
    this
  }

  def acceptBacklog():java.lang.Integer = {
    internal.getAcceptBacklog()
  }

  def acceptBacklog(backlog: Int):NetServer = {
    internal.setAcceptBacklog(backlog)
    this
  }

  def keyStorePassword():String = {
    internal.getKeyStorePassword()
  }

  def keyStorePassword(keyStorePassword: String):NetServer = {
    internal.setKeyStorePassword(keyStorePassword)
    this
  }

  def keyStorePath():String = {
    internal.getKeyStorePath()
  }

  def keyStorePath(keyStorePath: String):NetServer = {
    internal.setKeyStorePath(keyStorePath)
    this
  }

  def receiveBufferSize():java.lang.Integer = {
    internal.getReceiveBufferSize()
  }

  def receiveBufferSize(receiveBufferSize: Int):NetServer = {
    internal.setReceiveBufferSize(receiveBufferSize)
    this
  }

  def sendBufferSize():java.lang.Integer = {
    internal.getSendBufferSize()
  }

  def sendBufferSize(sendBufferSize: Int):NetServer = {
    internal.setSendBufferSize(sendBufferSize)
    this
  }

  def trafficClass():java.lang.Integer = {
    internal.getTrafficClass()
  }

  def trafficClass(trafficClass: Int):NetServer = {
    internal.setTrafficClass(trafficClass)
    this
  }

  def trustStorePassword():String = {
    internal.getTrustStorePassword()
  }

  def trustStorePassword(password: String):NetServer = {
    internal.setTrustStorePassword(password)
    this
  }

  def trustStorePath():String = {
    internal.getTrustStorePath()
  }

  def trustStorePath(path: String):NetServer = {
    internal.setTrustStorePath(path)
    this
  }
}