/*
 * Copyright 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.vertx.scala.core

import scala.collection.mutable.Map

/**
 * @author swilliams
 * @author Edgar Chan
 *
 */
package object json {

  type JsonElement = org.vertx.java.core.json.JsonElement
  type JsonArray = org.vertx.java.core.json.JsonArray
  type JsonObject = org.vertx.java.core.json.JsonObject

  import scala.language.implicitConversions

  implicit def toJsonObject(js: JsObject): JsonObject = js.internal

  implicit class JsObject(val internal: JsonObject) extends AnyVal {
    import scala.collection.JavaConverters._
    def asMap: Map[String, AnyRef] = internal.toMap.asScala
  }

}