package org.vertx.scala.testtools;

import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.TestClass;
import org.vertx.testtools.JavaClassRunner;

import java.util.List;

/**
 * Scala class runner. This class used to be a Scala class, but due to the lack
 * of support for SBT project detection in JavaClassRunner, a static
 * initialization is needed to set the correct value for -Dvertx.mods system
 * property before super() constructor executes.
 */
public class ScalaClassRunner extends JavaClassRunner {

  static {
    if (System.getProperty("vertx.mods") == null) {
      // JavaClassRunner can currently only detect gradle/maven projects,
      // so force SBT expectations when it comes to modules location
      // if not pre-configured.
      System.setProperty("vertx.mods", "target/mods");
    }
  }

  public ScalaClassRunner(Class<?> clazz) throws InitializationError {
    super(clazz);
  }

  @Override
  protected List<FrameworkMethod> computeTestMethods() {
    Class<?> testClass = getTestClass().getJavaClass();
    if (!TestVerticle.class.isAssignableFrom(testClass)) {
      throw new IllegalArgumentException("Scala Test classes must extend org.vertx.scala.testtools.TestVerticle");
    }
    this.main = "scala:" + testClass.getName();
    return getTestMethods();
  }

}
