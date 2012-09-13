package org.vertx.scala.core

import org.vertx.java.core.buffer.Buffer
import org.vertx.java.core.json.JsonArray
import org.vertx.java.core.json.JsonObject
import org.vertx.scala.deploy.FunctionHandler1
import org.vertx.java.core.eventbus.Message
import org.vertx.scala.deploy.FunctionAsyncResultHandler0

class EventBus(internal: org.vertx.java.core.eventbus.EventBus) {

  def publish(address: String, payload: java.lang.Boolean):Unit = {
    internal.publish(address, payload)
  }

  def publish(address: String, payload: Buffer):Unit = {
    internal.publish(address, payload)
  }

  def publish(address: String, payload: java.lang.Byte):Unit = {
    internal.publish(address, payload)
  }

  def publish(address: String, payload: Array[Byte]):Unit = {
    internal.publish(address, payload)
  }

  def publish(address: String, payload: java.lang.Character):Unit = {
    internal.publish(address, payload)
  }

  def publish(address: String, payload: java.lang.Double):Unit = {
    internal.publish(address, payload)
  }

  def publish(address: String, payload: java.lang.Float):Unit = {
    internal.publish(address, payload)
  }

  def publish(address: String, payload: java.lang.Integer):Unit = {
    internal.publish(address, payload)
  }

  def publish(address: String, payload: JsonArray):Unit = {
    internal.publish(address, payload)
  }

  def publish(address: String, payload: JsonObject):Unit = {
    internal.publish(address, payload)
  }

  def publish(address: String, payload: java.lang.Long):Unit = {
    internal.publish(address, payload)
  }

  def publish(address: String, payload: java.lang.Short):Unit = {
    internal.publish(address, payload)
  }

  def publish(address: String, payload: java.lang.String):Unit = {
    internal.publish(address, payload)
  }

  def registerHandler(handler: (Message[Any]) => Unit):String = {
    internal.registerHandler(new FunctionHandler1(handler))
  }

  def registerHandler(handler: (Message[Any]) => Unit, resultHandler: () => Unit):String = {
    internal.registerHandler(new FunctionHandler1(handler), new FunctionAsyncResultHandler0(resultHandler))
  }

  def registerHandler(address: String, handler: (Message[Any]) => Unit):String = {
    internal.registerHandler(address, new FunctionHandler1(handler))
  }

  def registerHandler(address: String, handler: (Message[Any]) => Unit, resultHandler: () => Unit):String = {
    internal.registerHandler(address, new FunctionHandler1(handler), new FunctionAsyncResultHandler0(resultHandler))
  }

  def registerLocalHandler(handler: (Message[Any]) => Unit):String = {
    internal.registerLocalHandler(new FunctionHandler1(handler))
  }

  def registerLocalHandler(address: String, handler: (Message[Any]) => Unit):String = {
    internal.registerLocalHandler(address, new FunctionHandler1(handler))
  }

  def send(address: String, payload: java.lang.Boolean):Unit = {
    internal.send(address, payload)
  }

  def send(address: String, payload: Buffer):Unit = {
    internal.send(address, payload)
  }

  def send(address: String, payload: java.lang.Byte):Unit = {
    internal.send(address, payload)
  }

  def send(address: String, payload: Array[Byte]):Unit = {
    internal.send(address, payload)
  }

  def send(address: String, payload: java.lang.Character):Unit = {
    internal.send(address, payload)
  }

  def send(address: String, payload: java.lang.Double):Unit = {
    internal.send(address, payload)
  }

  def send(address: String, payload: java.lang.Float):Unit = {
    internal.send(address, payload)
  }

  def send(address: String, payload: java.lang.Integer):Unit = {
    internal.send(address, payload)
  }

  def send(address: String, payload: JsonArray):Unit = {
    internal.send(address, payload)
  }

  def send(address: String, payload: JsonObject):Unit = {
    internal.send(address, payload)
  }

  def send(address: String, payload: java.lang.Long):Unit = {
    internal.send(address, payload)
  }

  def send(address: String, payload: java.lang.Short):Unit = {
    internal.send(address, payload)
  }

  def send(address: String, payload: java.lang.String):Unit = {
    internal.send(address, payload)
  }

  def send(address: String, payload: java.lang.Boolean, handler: (Message[java.lang.Boolean]) => Unit):Unit = {
    internal.send(address, payload, new FunctionHandler1(handler))
  }

  def send(address: String, payload: Buffer, handler: (Message[Buffer]) => Unit):Unit = {
    internal.send(address, payload, new FunctionHandler1(handler))
  }

  def send(address: String, payload: java.lang.Byte, handler: (Message[java.lang.Byte]) => Unit):Unit = {
    internal.send(address, payload, new FunctionHandler1(handler))
  }

  def send(address: String, payload: Array[Byte], handler: (Message[Array[Byte]]) => Unit):Unit = {
    internal.send(address, payload, new FunctionHandler1(handler))
  }

  def send(address: String, payload: java.lang.Character, handler: (Message[java.lang.Character]) => Unit):Unit = {
    internal.send(address, payload, new FunctionHandler1(handler))
  }

  def send(address: String, payload: java.lang.Double, handler: (Message[java.lang.Double]) => Unit):Unit = {
    internal.send(address, payload, new FunctionHandler1(handler))
  }

  def send(address: String, payload: java.lang.Float, handler: (Message[java.lang.Float]) => Unit):Unit = {
    internal.send(address, payload, new FunctionHandler1(handler))
  }

  def send(address: String, payload: java.lang.Integer, handler: (Message[java.lang.Integer]) => Unit):Unit = {
    internal.send(address, payload, new FunctionHandler1(handler))
  }

  def send(address: String, payload: JsonArray, handler: (Message[JsonArray]) => Unit):Unit = {
    internal.send(address, payload, new FunctionHandler1(handler))
  }

  def send(address: String, payload: JsonObject, handler: (Message[JsonObject]) => Unit):Unit = {
    internal.send(address, payload, new FunctionHandler1(handler))
  }

  def send(address: String, payload: java.lang.Long, handler: (Message[java.lang.Long]) => Unit):Unit = {
    internal.send(address, payload, new FunctionHandler1(handler))
  }

  def send(address: String, payload: java.lang.Short, handler: (Message[java.lang.Short]) => Unit):Unit = {
    internal.send(address, payload, new FunctionHandler1(handler))
  }

  def send(address: String, payload: java.lang.String, handler: (Message[java.lang.String]) => Unit):Unit = {
    internal.send(address, payload, new FunctionHandler1(handler))
  }

  def unregisterHandlerId(address: String):Unit = {
    internal.unregisterHandler(address)
  }

  def unregisterHandlerId(address: String, resultHandler: () => Unit):Unit = {
    internal.unregisterHandler(address, new FunctionAsyncResultHandler0(resultHandler))
  }

// FIXME creating a new FunctionHandler here will likely result in a failure 
  def unregisterHandler(address: String, handler: (Message[Any]) => Unit):Unit = {
    internal.unregisterHandler(address, new FunctionHandler1(handler))
  }

// FIXME creating a new FunctionHandler here will likely result in a failure 
  def unregisterHandler(address: String, handler: (Message[Any]) => Unit, resultHandler: (Void) => Unit):Unit = {
    internal.unregisterHandler(address, new FunctionHandler1(handler))
  }

}