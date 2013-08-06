package org.vertx.scala.tests.core.json

import org.junit.Test
import org.vertx.java.core.json.{DecodeException, JsonObject}
import scala.util.parsing.json.JSONFormat
import org.vertx.scala.core.json.JSON

/**
 * Vert.x and Scala JSON object conversion tests
 *
 * @author Galder Zamarreño
 */
class ScalaJsonTest {

  import JSON._

  @Test
  def jsonObjectToScalaTest() {
    new JsonObject().putString("foo", "bar").toString(JSONFormat.defaultFormatter)
  }

  @Test(expected = classOf[DecodeException])
  def jsonStringToScalaErrorTest() {
    "{1, one}".toString(JSONFormat.defaultFormatter)
  }

  @Test
  def jsonStringToScalaTest() {
    val json =
      """
        |{
        |  "webappconf" : {
        |          "port": 8080,
        |          "ssl": false,
        |          "bridge": true,
        |          "inbound_permitted": [
        |            {
        |              "address" : "acme.service",
        |              "match" : {
        |                "action" : "foo"
        |              }
        |            }
        |          ],
        |
        |          "outbound_permitted": [
        |            {}
        |          ]
        |        }
        |}
      """.stripMargin
    json.toString(JSONFormat.defaultFormatter)
  }


}
