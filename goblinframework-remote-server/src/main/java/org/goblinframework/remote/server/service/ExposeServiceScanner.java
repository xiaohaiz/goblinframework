package org.goblinframework.remote.server.service;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.goblinframework.api.common.GoblinInterruptedException;
import org.goblinframework.core.container.ContainerManagedBean;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

final public class ExposeServiceScanner {

  public static void scan(@NotNull ApplicationContext applicationContext) {
    List<ImmutablePair<ContainerManagedBean, List<ExposeServiceId>>> candidates = new ArrayList<>();
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
      candidates.add(ImmutablePair.of(new ContainerManagedBean(beanName, applicationContext), ids));
    }
    CountDownLatch latch = new CountDownLatch(candidates.size());
    ExecutorService executorService = Executors.newFixedThreadPool(8);
    candidates.forEach(c -> executorService.submit(() -> {
      try {
        RemoteServiceManager.INSTANCE.createManagedService(c.left, c.right);
      } finally {
        latch.countDown();
      }
    }));
    try {
      latch.await();
      executorService.shutdown();
      executorService.awaitTermination(1, TimeUnit.SECONDS);
    } catch (InterruptedException ex) {
      throw new GoblinInterruptedException(ex);
    }
  }
}
