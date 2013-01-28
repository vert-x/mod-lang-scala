/*
 * Copyright 2012 the original author or authors.
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
package org.vertx.test.http;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.vertx.java.core.Handler;
import org.vertx.java.core.buffer.Buffer;
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
  @TestVerticle(main="http/HelloWorldHttpServer.scala", urls={"build/resources/test"})
  public void testHelloWorld() {

    CountDownLatchHandler<HttpClientResponse> handler = new CountDownLatchHandler<>(1);

    client.setHost("localhost").setPort(8080).connect("/foo", handler).end();

    boolean await = handler.waitForMillis(5000L);
    Assert.assertTrue(await);

    HttpClientResponse res = handler.poll();
    Assert.assertNotNull(res);
    Assert.assertEquals(200, res.statusCode);
  }

  @Test
  @TestVerticle(main="http/SendFileHttpServer.scala", urls={"build/resources/test"})
  public void testSendFile() {

    CountDownLatchHandler<HttpClientResponse> handler = new CountDownLatchHandler<>(1);

    client.setHost("localhost").setPort(8080).connect("/foo", handler).end();

    boolean await = handler.waitForMillis(5000L);
    Assert.assertTrue(await);

    HttpClientResponse res = handler.poll();
    Assert.assertNotNull(res);
    Assert.assertEquals(200, res.statusCode);
  }

}
