package org.goblinframework.remote.server.dispatcher.request;

import org.goblinframework.core.service.GoblinManagedBean;
import org.goblinframework.core.service.GoblinManagedLogger;
import org.goblinframework.core.service.GoblinManagedObject;
import org.goblinframework.core.util.NamedDaemonThreadFactory;
import org.goblinframework.remote.core.protocol.RemoteResponseCode;
import org.goblinframework.remote.server.dispatcher.response.RemoteServerResponseDispatcher;
import org.goblinframework.remote.server.invocation.RemoteServerFilterManager;
import org.goblinframework.remote.server.invocation.RemoteServerInvocation;
import org.goblinframework.remote.server.invocation.RpcServerFilterChain;
import org.goblinframework.remote.server.module.config.RemoteServerConfig;
import org.goblinframework.remote.server.module.config.RemoteServerConfigManager;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Semaphore;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@GoblinManagedBean(type = "RemoteServer")
@GoblinManagedLogger(name = "goblin.remote.server.request")
public class RemoteServerRequestThreadPool extends GoblinManagedObject
    implements RemoteServerRequestThreadPoolMXBean {

  @NotNull private final RemoteServerConfig serverConfig;
  @NotNull private final Semaphore semaphore;
  @NotNull private final ThreadPoolExecutor executor;

  RemoteServerRequestThreadPool() {
    this.serverConfig = RemoteServerConfigManager.INSTANCE.getRemoteServerConfig();
    int maxPermits = serverConfig.getRequestConcurrent();
    this.semaphore = new Semaphore(maxPermits);
    int maximumPoolSize = maxPermits * 2;
    this.executor = new ThreadPoolExecutor(0, maximumPoolSize,
        60, TimeUnit.SECONDS,
        new SynchronousQueue<>(),
        NamedDaemonThreadFactory.getInstance("RemoteServerRequestThreadPool"));
  }

  void onInvocation(@NotNull RemoteServerInvocation invocation) {
    long timeoutInMillis = invocation.request.timeout;
    if (timeoutInMillis <= 0) {
      timeoutInMillis = serverConfig.getMaxTimeout();
    }
    boolean acquired = acquireSemaphore(semaphore, timeoutInMillis);
    if (!acquired) {
      logger.error("{SERVER_BACK_PRESSURE_ERROR} " +
              "Remote server thread pool exhausted, reject request from [{}]",
          invocation.context.asClientText());
      invocation.writeError(RemoteResponseCode.SERVER_BACK_PRESSURE_ERROR);
      RemoteServerResponseDispatcher.INSTANCE.onResponse(invocation);
      return;
    }
    executor.execute(() -> {
      try {
        RpcServerFilterChain chain = RemoteServerFilterManager.INSTANCE.createFilterChain();
        chain.filter(invocation);
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
  protected void disposeBean() {
    executor.shutdown();
    try {
      executor.awaitTermination(15, TimeUnit.SECONDS);
      logger.debug("[RemoteServerRequestThreadPool] terminated");
    } catch (InterruptedException ignore) {
    }
  }
}
