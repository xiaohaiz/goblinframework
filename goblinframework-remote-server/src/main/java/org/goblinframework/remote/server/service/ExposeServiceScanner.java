package org.goblinframework.remote.server.service;

import org.goblinframework.api.common.GoblinInterruptedException;
import org.goblinframework.core.container.ContainerManagedBean;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationContext;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

final public class ExposeServiceScanner {

  public static void scan(@NotNull ApplicationContext applicationContext) {
    Semaphore semaphore = new Semaphore(8);
    ExecutorService executorService = Executors.newCachedThreadPool();
    String[] beanNames = applicationContext.getBeanNamesForType(Object.class, true, false);
    for (String beanName : beanNames) {
      Class<?> beanType = applicationContext.getType(beanName);
      if (beanType == null) {
        continue;
      }
      List<ExposeServiceId> ids = ExposeServiceIdGenerator.generate(beanType);
      if (ids.isEmpty()) {
        continue;
      }
      semaphore.acquireUninterruptibly();
      executorService.submit(() -> {
        try {
          ContainerManagedBean cmb = new ContainerManagedBean(beanName, applicationContext);
          RemoteServiceManager.INSTANCE.createManagedService(cmb, ids);
        } finally {
          semaphore.release();
        }
      });
    }
    executorService.shutdown();
    try {
      executorService.awaitTermination(3, TimeUnit.SECONDS);
    } catch (InterruptedException ex) {
      throw new GoblinInterruptedException(ex);
    }
  }
}
