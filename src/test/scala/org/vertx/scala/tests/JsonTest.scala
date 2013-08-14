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
package org.vertx.scala.tests

import org.junit.Test
import org.junit.Assert.assertEquals
import org.vertx.scala.core.json._

/**
 * @author Edgar Chan
 */
class JsonTest {

  @Test
  def jsonObjectTest() {

    val enc = """{"foo":"foo text","bar":3.45,"baz":false,"myInt":2147483647}"""

    val obj: JsonObject =
      Json.obj(
        "foo" -> "foo text",
        "bar" -> 3.45d,
        "baz" -> false,
        "myInt" -> Integer.MAX_VALUE
      )

    assertEquals("foo text", obj.getString("foo"))
    assertEquals(3.45d, obj.getValue[Double]("bar"), 1e-15)
    assertEquals(false, obj.getBoolean("baz"))
    assertEquals(Integer.MAX_VALUE, obj.getInteger("myInt"))
    assertEquals(enc, obj.encode())


  }

  @Test
  def wrapperConversionTest() {

    val obj = Json.obj("foo" -> "foo text", "optional" -> true)

    val wrapped: JsObject = obj
    val scalaMapFields: scala.collection.Map[String, AnyRef] = wrapped.asMap

    //Get the original object
    val obj2:JsonObject =  wrapped

    assertEquals("foo text", scalaMapFields("foo"))
    assertEquals(true, obj2.getBoolean("optional"))

  }

  @Test
  def jsonArrayTest() {

    val enc = """["f",3,"b",7,35.4,true]"""
    val array = Json.arr(List("f", 3, "b", 7, 35.4f, true))

    assertEquals(6, array.size())
    assertEquals(enc, array.encode())
  }


  @Test
  def customObjTest(){
    import java.util.Date

    case class Custom(date:Date, other:Boolean)
    val info = Custom(new Date(), false)
    val obj1 =  Json.obj("custom" -> info)

    assertEquals(info, obj1.getValue[Custom]("custom"))
  }

  @Test
  def nestedObjectsTest(){
    val obj =
      Json.obj(
        "webappconf"  -> Json.obj(
             "port"   -> 8080,
             "ssl"    -> false,
             "bridge" -> true,
             "inbound_permitted" -> Json.arr(
                 Seq(Json.obj(
                   "address" -> "acme.bar",
                   "match"   -> Json.obj(
                       "action" -> "foo"
                    )),
                   Json.obj(
                    "address" -> "acme.baz",
                    "match"   -> Json.obj(
                        "action" -> "index"
                     ))
                 )
             ),
             "outbound_permitted"  -> Json.arr(
                 Seq(new JsonObject())
             )
         )
      )

    assertEquals(jsonString, obj.encode())
  }


  @Test
  def nestedObjectsShorterVersionTest(){
    val obj =
      Json(
        "webappconf"  -> Json(
             "port"   -> 8080,
             "ssl"    -> false,
             "bridge" -> true,
             "inbound_permitted" -> Json(
                 Seq(Json(
                   "address" -> "acme.bar",
                   "match"   -> Json(
                       "action" -> "foo"
                    )),
                   Json(
                    "address" -> "acme.baz",
                    "match"   -> Json(
                        "action" -> "index"
                     ))
                 )
             ),
             "outbound_permitted" -> Json(
                 Seq(new JsonObject())
             )
         )
      )

    assertEquals(jsonString, obj.encode())
  }


  private def jsonString = {
    """
      |{
      |  "webappconf" : {
      |          "port": 8080,
      |          "ssl": false,
      |          "bridge": true,
      |          "inbound_permitted": [
      |            {
      |              "address" : "acme.bar",
      |              "match" : {
      |                "action" : "foo"
      |              }
      |            },
      |
      |            {
      |              "address" : "acme.baz",
      |              "match" : {
      |                "action" : "index"
      |              }
      |            }
      |          ],
      |
      |          "outbound_permitted": [
      |            {}
      |          ]
      |        }
      |}
    """.stripMargin.replaceAll("\\s", "")
  }

}