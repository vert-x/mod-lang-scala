package org.vertx.test.http;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.vertx.java.core.Handler;
import org.vertx.java.core.http.HttpClient;
import org.vertx.java.core.http.HttpClientResponse;
import org.vertx.java.test.TestVerticle;
import org.vertx.java.test.VertxConfiguration;
import org.vertx.java.test.VertxTestBase;
import org.vertx.java.test.junit.VertxJUnit4ClassRunner;


@RunWith(VertxJUnit4ClassRunner.class)
@VertxConfiguration
public class HttpServerTest extends VertxTestBase {

  private HttpClient client;

  @Before
  public void setup() {
    this.client = super.getVertx().createHttpClient();
    lightSleep(1000L);
  }

  @Test
  @TestVerticle(main="http/HelloWorldHttpServer.scala", urls={"src/test/resources"})
  public void testHelloWorld() {

    final CountDownLatch latch = new CountDownLatch(1);
    final LinkedBlockingQueue<HttpClientResponse> queue = new LinkedBlockingQueue<>();

    Handler<HttpClientResponse> handler = new Handler<HttpClientResponse>() {
      @Override
      public void handle(HttpClientResponse event) {
        queue.offer(event);
        latch.countDown();
      }
    };

    client.setHost("localhost").setPort(8080).connect("/foo", handler).end();

    try {
      boolean await = latch.await(5000L, TimeUnit.MILLISECONDS);
      Assert.assertTrue(await);

      HttpClientResponse res = queue.poll();
      Assert.assertNotNull(res);

      Assert.assertEquals(200, res.statusCode);

    } catch (InterruptedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  @Test
  @TestVerticle(main="http/SendFileHttpServer.scala", urls={"src/test/resources"})
  public void testSendFile() {

    final CountDownLatch latch = new CountDownLatch(1);
    final LinkedBlockingQueue<HttpClientResponse> queue = new LinkedBlockingQueue<>();

    Handler<HttpClientResponse> handler = new Handler<HttpClientResponse>() {
      @Override
      public void handle(HttpClientResponse event) {
        queue.offer(event);
        latch.countDown();
      }
    };

    client.setHost("localhost").setPort(8080).connect("/foo", handler).end();

    try {
      boolean await = latch.await(5000L, TimeUnit.MILLISECONDS);
      Assert.assertTrue(await);

      HttpClientResponse res = queue.poll();
      Assert.assertNotNull(res);

      Assert.assertEquals(200, res.statusCode);

    } catch (InterruptedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

}
