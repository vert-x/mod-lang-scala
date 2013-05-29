
package org.vertx.test

import org.junit.Test
import org.junit.Assert._
import org.vertx.testtools.TestVerticle
import org.vertx.scala.core.Vertx
import org.vertx.java.core.buffer.Buffer
import org.vertx.java.core.http.HttpClient
import org.vertx.java.core.http.HttpClientResponse
import org.vertx.java.core.Handler

import  org.vertx.testtools.VertxAssert.testComplete;




class ScalaHttpTest extends TestVerticle {


  @Test def testClientDefaults() {

    val client = vertx.createHttpClient();
    System.out.println("client: " + client );

//      client.getNow("/",{
//        resp => resp.bodyHandler({
//          data => {
//            System.out.println("DATA: " + data);     
//          }
//        });
//      });
    testComplete();
  }

}