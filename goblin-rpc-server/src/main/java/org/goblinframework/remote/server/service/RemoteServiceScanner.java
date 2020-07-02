package org.goblinframework.remote.server.service;

import org.goblinframework.api.annotation.Singleton;
import org.goblinframework.core.concurrent.GoblinInterruptedException;
import org.goblinframework.core.container.ContainerManagedBean;
import org.goblinframework.core.container.ContainerRefreshedEvent;
import org.goblinframework.core.event.GoblinEventChannel;
import org.goblinframework.core.event.GoblinEventContext;
import org.goblinframework.core.event.GoblinEventListener;
import org.goblinframework.core.util.SystemUtils;
import org.goblinframework.remote.server.module.exception.ServiceCreationException;
import org.goblinframework.rpc.service.RemoteServiceId;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Singleton
@GoblinEventChannel("/goblin/core")
final public class RemoteServiceScanner implements GoblinEventListener {

  private static final RemoteServiceScanner instance = new RemoteServiceScanner();

  @NotNull
  public static RemoteServiceScanner getInstance() {
    return instance;
  }

  @Override
  public boolean accept(@NotNull GoblinEventContext context) {
    return context.getEvent() instanceof ContainerRefreshedEvent;
  }

  @Override
  public void onEvent(@NotNull GoblinEventContext context) {
    ContainerRefreshedEvent event = (ContainerRefreshedEvent) context.getEvent();
    ApplicationContext applicationContext = event.getApplicationContext();
    scan(applicationContext);
  }

  private void scan(ApplicationContext applicationContext) {
    List<Candidate> candidates = new ArrayList<>();
    String[] beanNames = applicationContext.getBeanNamesForType(Object.class, true, false);
    for (String beanName : beanNames) {
      Class<?> beanType = applicationContext.getType(beanName);
      if (beanType == null) {
        continue;
      }
      List<RemoteServiceId> ids = RemoteServiceIdGenerator.generate(beanType);
      if (ids.isEmpty()) {
        continue;
      }
      ContainerManagedBean bean = new ContainerManagedBean(beanName, applicationContext);
      Candidate candidate = new Candidate();
      candidate.bean = bean;
      candidate.ids = ids;
      candidates.add(candidate);
    }
    if (candidates.isEmpty()) {
      return;
    }
    CountDownLatch latch = new CountDownLatch(candidates.size());
    List<Throwable> exceptionList = Collections.synchronizedList(new ArrayList<>());
    ExecutorService executorService = Executors.newFixedThreadPool(SystemUtils.availableProcessors());
    candidates.forEach(c -> executorService.submit(() -> {
      try {
        RemoteServiceManager.INSTANCE.createManagedServices(c.bean, c.ids);
      } catch (Exception ex) {
        exceptionList.add(ex);
      } finally {
        latch.countDown();
      }
    }));
    try {
      latch.await();
      executorService.shutdown();
      executorService.awaitTermination(3, TimeUnit.SECONDS);
    } catch (InterruptedException ex) {
      throw new GoblinInterruptedException(ex);
    }
    if (!exceptionList.isEmpty()) {
      Throwable error = exceptionList.iterator().next();
      ServiceCreationException.rethrow(error);
    }
  }

  private static class Candidate {
    private ContainerManagedBean bean;
    private List<RemoteServiceId> ids;
  }
}
