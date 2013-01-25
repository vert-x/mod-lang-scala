package org.vertx.scala

import org.vertx.java.core.json.JsonObject
import scala.collection.JavaConverters.mapAsJavaMapConverter

class JSON {

  implicit def convertStringToJsonObject(json: String): JsonObject = {
    new JsonObject(json)
  }

  implicit def convertMapToJsonObject(json: Map[String, Object]): JsonObject = {
    new JsonObject(mapAsJavaMapConverter(json).asJava)
  }

}