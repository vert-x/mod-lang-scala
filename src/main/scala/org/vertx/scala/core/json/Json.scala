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
package org.vertx.scala.core.json

import scala.annotation.implicitNotFound

/**
 * @author Edgar Chan
 */

@implicitNotFound(msg = "Cannot find add operations for type ${T}")
trait JsonElemOps[T]{
  def addToObj(o:JsonObject, key:String, v:T):JsonObject
  def addToArr(a:JsonArray, v:T):JsonArray
}

object JsonElemOps{
  implicit object JsonStringElem extends JsonElemOps[String]{
    def addToObj(o:JsonObject, key:String, v:String):JsonObject= o.putString(key,v)
    def addToArr(a:JsonArray, v:String):JsonArray = a.addString(v)
  }
  implicit object JsonIntElem extends JsonElemOps[Int]{
    def addToObj(o:JsonObject, key:String, v:Int):JsonObject= o.putNumber(key,v)
    def addToArr(a:JsonArray, v:Int):JsonArray = a.addNumber(v)
  }
  implicit object JsonBoolElem extends JsonElemOps[Boolean]{
    def addToObj(o:JsonObject, key:String, v:Boolean):JsonObject= o.putBoolean(key,v)
    def addToArr(a:JsonArray, v:Boolean):JsonArray = a.addBoolean(v)
  }
  implicit object JsonFloatElem extends JsonElemOps[Float]{
    def addToObj(o:JsonObject, key:String, v:Float):JsonObject= o.putNumber(key,v)
    def addToArr(a:JsonArray, v:Float):JsonArray = a.addNumber(v)
  }
  implicit object JsonJsObjectElem extends JsonElemOps[JsonObject]{
    def addToObj(o:JsonObject, key:String, v:JsonObject):JsonObject= o.putObject(key,v)
    def addToArr(a:JsonArray, v:JsonObject):JsonArray = a.addObject(v)
  }
  implicit object JsonJsArrayElem extends JsonElemOps[JsonArray]{
    def addToObj(o:JsonObject, key:String, v:JsonArray):JsonObject= o.putArray(key,v)
    def addToArr(a:JsonArray, v:JsonArray):JsonArray = a.addArray(v)
  }
  implicit object JsonJsElem extends JsonElemOps[JsonElement]{
    def addToObj(o:JsonObject, key:String, v:JsonElement):JsonObject= o.putElement(key,v)
    def addToArr(a:JsonArray, v:JsonElement):JsonArray = a.addElement(v)
  }
  implicit object JsonBinaryElem extends JsonElemOps[Array[Byte]]{
    def addToObj(o:JsonObject, key:String, v:Array[Byte]):JsonObject= o.putBinary(key,v)
    def addToArr(a:JsonArray, v:Array[Byte]):JsonArray = a.addBinary(v)
  }
  implicit object JsonAnyElem extends JsonElemOps[Any]{
    def addToObj(o:JsonObject, key:String, v:Any):JsonObject= o.putValue(key,v)
    def addToArr(a:JsonArray, v:Any):JsonArray = a.add(v)
  }
}

object Json {

  def apply(fields: (String, Any)*): JsonObject = obj(fields: _*)

  def apply(fields: Seq[Any]): JsonArray = arr(fields)

  def emptyObj(): JsonObject = new JsonObject()

  def emptyArr(): JsonArray = new JsonArray()

  def addToObject[T:JsonElemOps](o:JsonObject, fieldName:String, fieldValue:T) ={
    implicitly[JsonElemOps[T]].addToObj(o, fieldName, fieldValue)
  }

  def addToArray[T:JsonElemOps](a:JsonArray, fieldValue:T) ={
    implicitly[JsonElemOps[T]].addToArr(a, fieldValue)
  }

  def obj(fields: (String, Any)*): JsonObject = {
    val o = new JsonObject()
    fields.foreach(f => addToObject(o, f._1, f._2))
    o
  }

  def arr(fields: Seq[Any]): JsonArray = {
    val a = new JsonArray()
    fields.foreach(f => addToArray(a, f))
    a
  }

}