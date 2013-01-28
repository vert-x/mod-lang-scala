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

public class CountDownLatchHandler<T> implements Handler<T> {

  private CountDownLatch latch;

  private LinkedBlockingQueue<T> queue;

  private long timeout;

  private TimeUnit timeUnit;

  public CountDownLatchHandler(int size) {
    this(size, 1000L);
  }

  public CountDownLatchHandler(int size, long timeout) {
    this(size, timeout, TimeUnit.MILLISECONDS);
  }

  public CountDownLatchHandler(int size, long timeout, TimeUnit timeUnit) {
    this.latch = new CountDownLatch(size);
    this.queue = new LinkedBlockingQueue<>(size);
    this.timeout = timeout;
    this.timeUnit = timeUnit;
  }

  @Override
  public void handle(T event) {
    try {
      queue.offer(event, timeout, timeUnit);
      latch.countDown();
    } catch (InterruptedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public void waitForever() {
    try {
      latch.await();
    } catch (InterruptedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public boolean waitFor() {
    boolean awaited = false;
    try {
      awaited = latch.await(timeout, timeUnit);
    } catch (InterruptedException e) {
      // TODO Auto-generated catch block
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
      // TODO Auto-generated catch block
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

  public T poll(long timeout, TimeUnit unit) throws InterruptedException {
    return queue.poll(timeout, unit);
  }

  public T poll() {
    return queue.poll();
  }

  public T peek() {
    return queue.peek();
  }

}
