package org.vertx.java.deploy.impl.scala;

import org.vertx.java.deploy.Verticle;
import org.vertx.java.deploy.VerticleFactory;
import org.vertx.java.deploy.impl.VerticleManager;

public class ScalaVerticleFactory implements VerticleFactory {

  private static final String LANGUAGE = "scalaj";

  private static final String PREFIX = "scalaj:";

  private static final String SUFFIX = ".scalaj";

  private VerticleManager manager;

  @Override
  public void init(VerticleManager manager) {
    this.manager = manager;
  }

  @Override
  public String getLanguage() {
    return LANGUAGE;
  }

  @Override
  public boolean isFactoryFor(String main) {
    if (main.startsWith(PREFIX)) {
      return true;
    }

    if (main.endsWith(SUFFIX)) {
      return true;
    }

    return false;
  }

  @Override
  public Verticle createVerticle(String main, ClassLoader parentCL)
      throws Exception {

    if (main.endsWith(SUFFIX)) {
      // do compile
    }

    if (main.startsWith(PREFIX)) {
      main = main.replaceFirst(PREFIX, "");
    }

    return new ScalaVerticle();
  }

  @Override
  public void reportException(Throwable t) {
    manager.getLogger().error("eek!", t);
  }

}
