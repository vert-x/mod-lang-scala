
package org.vertx.test

import org.junit.Test
import org.junit.Assert._
import org.vertx.scala.core.Vertx



class ScalaHttpTest  {


  @Test def testClientDefaults() {


    	val client = Vertx.newVertx(8080, "localhost").createHttpClient();
    	System.out.println("Max Pool Size: " + client.maxPoolSize());




//    	        client.getNow("/", new Handler<HttpClientResponse>() {
//    	           public void handle(HttpClientResponse resp) {
//    	        	   
//    	        	   resp.bodyHandler(new Handler<Buffer>() {
//    	        	          public void handle(Buffer data) {
//    	        	            System.out.println(data);
//    	        	          }
//    	        	        });
//    	           }
//    	         });


  }


}