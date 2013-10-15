/*
 * Copyright 2011-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.vertx.scala.mods

import org.vertx.scala.core.json.JsonObject
import org.vertx.scala.core.json.JsonArray
import org.vertx.scala.core.logging.Logger
import org.vertx.scala.core.eventbus.Message
import org.vertx.scala.core.eventbus.EventBus
import org.vertx.scala.core._
import org.vertx.scala.platform.Verticle
import scala.concurrent.Promise

/**
 * @author swilliams
 * @author <a href="http://www.campudus.com/">Joern Bernhardt</a>
 */
trait ModuleBase extends Verticle {

  var eb: EventBus = null

  var config: JsonObject = null

  override final def start(): Unit = {
    eb = vertx.eventBus
    config = container.config()
    startMod()
  }

  def startMod(): Unit

  def sendOK(message: Message[JsonObject]): Unit = {
    sendOK(message, null)
  }

  def sendStatus(status: String, message: Message[JsonObject], reply: JsonObject = null): Unit = {
    reply.putString("status", status)
    message.reply(reply)
  }

  def sendOK(message: Message[JsonObject], reply: JsonObject = null): Unit = {
    sendStatus("ok", message, reply)
  }

  def sendError(message: Message[JsonObject], error: String): Unit = {
    sendError(message, error, null)
  }

  def sendError(message: Message[JsonObject], error: String, e: Exception): Unit = {
    logger.error(error, e)
    var json = new JsonObject().putString("status", "error").putString("message", error)
    message.reply(json)
  }

  def getMandatoryString(fieldName: String, message: Message[JsonObject]): String = {
    var obj = message.body.getString(fieldName)
    if (obj == null) {
      sendError(message, fieldName + " must be specified")
    }
    obj
  }

  def getMandatoryObject(fieldName: String, message: Message[JsonObject]): JsonObject = {
    var obj = message.body.getObject(fieldName)
    if (obj == null) {
      sendError(message, fieldName + " must be specified")
    }
    obj
  }

  def getOptionalBooleanConfig(fieldName: String, defaultValue: Boolean): Boolean =
    config.getBoolean(fieldName, defaultValue)

  def getOptionalStringConfig(fieldName: String, defaultValue: String): String =
    config.getString(fieldName, defaultValue)

  def getOptionalIntConfig(fieldName: String, defaultValue: Int): Int =
    config.getInteger(fieldName, defaultValue)

  def getOptionalLongConfig(fieldName: String, defaultValue: Long): Long =
    config.getLong(fieldName, defaultValue)

  def getOptionalObjectConfig(fieldName: String, defaultValue: JsonObject): JsonObject =
    config.getObject(fieldName, defaultValue)

  def getOptionalArrayConfig(fieldName: String, defaultValue: JsonArray): JsonArray =
    config.getArray(fieldName, defaultValue)

  private def mandatoryField[T](fieldName: String, getter: => T): T = {
    val x = getter
    if (x == null) {
      throw new IllegalArgumentException(fieldName + " must be specified in config for busmod")
    }
    x
  }

  def getMandatoryBooleanConfig(fieldName: String): Boolean =
    mandatoryField(fieldName, config.getBoolean(fieldName))

  def getMandatoryStringConfig(fieldName: String): String =
    mandatoryField(fieldName, config.getString(fieldName))

  def getMandatoryIntConfig(fieldName: String): Int =
    mandatoryField(fieldName, config.getInteger(fieldName))

  def getMandatoryLongConfig(fieldName: String): Long =
    mandatoryField(fieldName, config.getLong(fieldName))

}