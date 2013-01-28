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

package org.vertx.test.http;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.vertx.java.core.Handler;
import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.http.HttpClientResponse;

public class HttpClientResponseHandler implements Handler<HttpClientResponse> {

  private CountDownLatch latch;

  private LinkedBlockingQueue<HttpClientResponse> queue;

  private LinkedBlockingQueue<String> queueBody;

  private long timeout;

  private TimeUnit timeUnit;

  public HttpClientResponseHandler(int size) {
    this(size, 1000L);
  }

  public HttpClientResponseHandler(int size, long timeout) {
    this(size, timeout, TimeUnit.MILLISECONDS);
  }

  public HttpClientResponseHandler(int size, long timeout, TimeUnit timeUnit) {
    this.latch = new CountDownLatch(size);
    this.queue = new LinkedBlockingQueue<>(size);
    this.queueBody = new LinkedBlockingQueue<>(size);
    this.timeout = timeout;
    this.timeUnit = timeUnit;
  }

  @Override
  public void handle(HttpClientResponse event) {
    event.bodyHandler(new Handler<Buffer>() {
      @Override
      public void handle(Buffer event) {
        queueBody.offer(event.toString("UTF-8"));
      }
    });
    try {
      queue.offer(event, timeout, timeUnit);
      latch.countDown();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  public void waitForever() {
    try {
      latch.await();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  public boolean waitFor() {
    boolean awaited = false;
    try {
      awaited = latch.await(timeout, timeUnit);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    return awaited;
  }

  public boolean waitForMillis(long timeout) {
    return waitFor(timeout, TimeUnit.MILLISECONDS);
  }

  public boolean waitFor(long timeout, TimeUnit timeUnit) {
    boolean awaited = false;
    try {
      awaited = latch.await(timeout, timeUnit);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    return awaited;
  }

  public boolean isEmpty() {
    return queue.isEmpty();
  }

  public int size() {
    return queue.size();
  }

  public String getBody() {
    try {
      return queueBody.poll(2000L, timeUnit);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    return null;
  }

  public HttpClientResponse poll(long timeout, TimeUnit unit) {
    try {
      return queue.poll(timeout, unit);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    return null;
  }

  public HttpClientResponse poll() {
    return queue.poll();
  }

  public HttpClientResponse peek() {
    return queue.peek();
  }

}
