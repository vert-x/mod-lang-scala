/*
 * Copyright 2011-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.vertx.scala.deploy;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.vertx.java.test.TestVerticle;
import org.vertx.java.test.VertxConfiguration;
import org.vertx.java.test.VertxTestBase;
import org.vertx.java.test.junit.VertxJUnit4ClassRunner;

@RunWith(VertxJUnit4ClassRunner.class)
@VertxConfiguration
public class SimpleCompiledScalaTest extends VertxTestBase {

  @Test
  @TestVerticle(main="scala:org.vertx.test.SimpleCompiledVerticle")
  public void testSimple() {
    //
    lightSleep(5000L);
  }

}
