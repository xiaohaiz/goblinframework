package org.goblinframework.remote.client.dispatcher.request;

import org.goblinframework.core.service.GoblinManagedBean;
import org.goblinframework.core.service.GoblinManagedLogger;
import org.goblinframework.core.service.GoblinManagedObject;
import org.goblinframework.core.util.NamedDaemonThreadFactory;
import org.goblinframework.remote.client.invocation.RemoteClientInvocation;
import org.goblinframework.remote.client.module.config.RemoteClientConfig;
import org.goblinframework.remote.client.module.config.RemoteClientConfigManager;
import org.goblinframework.remote.client.module.exception.ClientBackPressureException;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Semaphore;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@GoblinManagedBean(type = "RemoteClient")
@GoblinManagedLogger(name = "goblin.remote.client.request")
public class RemoteClientRequestThreadPool extends GoblinManagedObject
    implements RemoteClientRequestThreadPoolMXBean {

  @NotNull private final RemoteClientConfig clientConfig;
  @NotNull private final Semaphore semaphore;
  @NotNull private final ThreadPoolExecutor executor;

  RemoteClientRequestThreadPool() {
    this.clientConfig = RemoteClientConfigManager.INSTANCE.getRemoteClientConfig();
    int maxPermits = clientConfig.getRequestConcurrent();
    this.semaphore = new Semaphore(maxPermits);
    int maximumPoolSize = maxPermits * 2;
    this.executor = new ThreadPoolExecutor(0, maximumPoolSize,
        60, TimeUnit.SECONDS,
        new SynchronousQueue<>(),
        NamedDaemonThreadFactory.getInstance("RemoteClientRequestThreadPool"));
  }

  void onInvocation(@NotNull RemoteClientInvocation invocation) {
    long timeoutInMillis = invocation.timeout();
    if (timeoutInMillis <= 0) {
      timeoutInMillis = clientConfig.getMaxTimeout();
    }
    boolean acquired = acquireSemaphore(semaphore, timeoutInMillis);
    if (!acquired) {
      logger.error("{CLIENT_BACK_PRESSURE_ERROR} " +
              "Remote client thread pool exhausted, reject request [service={}]",
          invocation.serviceId.asText());
      invocation.complete(null, new ClientBackPressureException());
      return;
    }
    executor.submit(() -> {
      try {
        System.out.println("!");
      } finally {
        semaphore.release();
      }
    });
  }

  private boolean acquireSemaphore(Semaphore semaphore, long timeoutInMillis) {
    try {
      return semaphore.tryAcquire(timeoutInMillis, TimeUnit.MILLISECONDS);
    } catch (InterruptedException ex) {
      return false;
    }
  }

  @Override
  public int getPoolSize() {
    return executor.getPoolSize();
  }

  @Override
  public int getActiveCount() {
    return executor.getActiveCount();
  }

  @Override
  public int getCorePoolSize() {
    return executor.getCorePoolSize();
  }

  @Override
  public int getMaximumPoolSize() {
    return executor.getMaximumPoolSize();
  }

  @Override
  public int getLargestPoolSize() {
    return executor.getLargestPoolSize();
  }

  @Override
  public long getTaskCount() {
    return executor.getTaskCount();
  }

  @Override
  public long getCompletedTaskCount() {
    return executor.getCompletedTaskCount();
  }

  @Override
  public boolean getShutdown() {
    return executor.isShutdown();
  }

  @Override
  public boolean getTerminated() {
    return executor.isTerminated();
  }

  @Override
  public boolean getTerminating() {
    return executor.isTerminating();
  }

  @Override
  protected void disposeBean() {
    executor.shutdown();
    try {
      executor.awaitTermination(15, TimeUnit.SECONDS);
      logger.debug("[RemoteClientRequestThreadPool] terminated");
    } catch (InterruptedException ignore) {
    }
  }
}
