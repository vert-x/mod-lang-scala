
package org.vertx.test

import org.junit.Test
import org.junit.Assert._
import org.vertx.scala.core.Vertx
import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.http.HttpClient;
import org.vertx.java.core.http.HttpClientResponse;
import org.vertx.java.core.Handler;




class ScalaHttpTest  {


  @Test def testClientDefaults() {

    val client = Vertx.newVertx(8080, "localhost").createHttpClient();
      client.getNow("/",{
        resp => resp.bodyHandler({
          data => {
            System.out.println("DATA: " + data);     
          }
        });
      });
  }


}