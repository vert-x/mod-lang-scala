package org.vertx.java.deploy.impl.scala;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.vertx.java.test.TestVerticle;
import org.vertx.java.test.VertxTestBase;
import org.vertx.java.test.junit.VertxJUnit4ClassRunner;

@RunWith(VertxJUnit4ClassRunner.class)
@TestVerticle(main="deployer.js")
public class SimpleCompiledScalaTest extends VertxTestBase {

  @Test
  @TestVerticle(main="scala:org.vertx.test.SimpleVerticle")
  public void testSimple() {
    //
  }

}
