package org.vertx.scala

import scala.collection.JavaConverters.mapAsJavaMapConverter
import scala.util.parsing.json.{JSON => sJSON}
import scala.util.parsing.json.JSONObject
import scala.util.parsing.json.JSONArray
import org.vertx.java.core.json.JsonObject
import org.vertx.java.core.json.JsonArray

//object JSON {
//  def apply() = 
//    new JSON()
//}

object JSON {

  implicit def convertJsonObjectToScala(json: JsonObject): JSONObject = {
    val data:String = if (json != null) json.encode() else ""
    sJSON.parseRaw(data).asInstanceOf[JSONObject]
  }

  implicit def convertScalaToJsonArray(json: JSONArray): JsonArray = {
    val data:String = if (json != null) json.toString else ""
    new JsonArray(data)
  }

  implicit def convertScalaToJsonObject(json: JSONObject): JsonObject = {
    val data:String = if (json != null) json.toString else ""
    new JsonObject(data)
  }

  implicit def convertStringToJsonObject(json: String): JsonObject = {
    val data = if (json != null) json else ""
    new JsonObject(data)
  }

  implicit def convertStringToJSONObject(json: String): JsonObject = {
    val data = if (json != null) json else ""
    new JSONObject(data)
  }

  implicit def convertMapToJsonObject(json: Map[String, Object]): JsonObject = {
    new JsonObject(mapAsJavaMapConverter(json).asJava)
  }

}