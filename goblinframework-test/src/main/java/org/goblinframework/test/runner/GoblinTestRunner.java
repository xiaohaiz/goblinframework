package org.goblinframework.test.runner;

import org.goblinframework.core.bootstrap.GoblinBootstrap;
import org.junit.runners.model.InitializationError;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

public class GoblinTestRunner extends SpringJUnit4ClassRunner {

  static {
    GoblinBootstrap.initialize();
    Thread shutdownHook = new Thread(GoblinBootstrap::close);
    shutdownHook.setDaemon(true);
    Runtime.getRuntime().addShutdownHook(shutdownHook);
  }

  public GoblinTestRunner(Class<?> clazz) throws InitializationError {
    super(clazz);
  }
}
