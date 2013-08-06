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

package org.vertx.scala.core.json
import scala.util.parsing.json.{ JSON => sJSON }
import scala.util.parsing.json.JSONObject
import scala.util.parsing.json.JSONArray
import org.vertx.java.core.json.JsonObject
import org.vertx.java.core.json.JsonArray
import scala.collection.JavaConversions._
import org.vertx.java.core.json.DecodeException

/**
 * @author swilliams
 *
 */
object JSON {

  implicit def convertJsonObjectToScala(json: JsonObject): JSONObject = {
    val data: String = if (json != null) json.encode() else "{}"
    parseRawJason(data)
  }

  implicit def convertScalaToJsonArray(json: JSONArray): JsonArray = {
    val data: String = if (json != null) json.toString else ""
    new JsonArray(data)
  }

  implicit def convertScalaToJsonObject(json: JSONObject): JsonObject = {
    val data: String = if (json != null) json.toString else ""
    new JsonObject(data)
  }

  implicit def convertStringToJsonObject(json: String): JsonObject = {
    new JsonObject(json)
  }

  implicit def convertStringToJSONObject(json: String): JSONObject =
    parseRawJason(json)

  implicit def convertMapToJsonObject(json: Map[String, Object]): JsonObject = {
    new JsonObject(json)
  }

  private def parseRawJason(json: String): JSONObject = {
    sJSON.parseRaw(json) match {
      case Some(x: JSONObject) => x
      case otherType => throw new DecodeException("Failed to decode to JSONObject: " + json)
    }
  }

}