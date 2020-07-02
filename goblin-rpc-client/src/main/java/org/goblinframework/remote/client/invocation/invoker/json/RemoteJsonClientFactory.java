package org.goblinframework.remote.client.invocation.invoker.json;

import org.goblinframework.api.annotation.ThreadSafe;
import org.goblinframework.rpc.service.RemoteServiceId;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@ThreadSafe
final public class RemoteJsonClientFactory {

  private static final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
  private static final Map<RemoteServiceId, RemoteJsonClient> buffer = new HashMap<>();

  @NotNull
  public static RemoteJsonClient getJsonClient(@NotNull RemoteServiceId serviceId) {
    lock.readLock().lock();
    try {
      RemoteJsonClient cached = buffer.get(serviceId);
      if (cached != null) return cached;
    } finally {
      lock.readLock().unlock();
    }
    lock.writeLock().lock();
    try {
      RemoteJsonClient cached = buffer.get(serviceId);
      if (cached != null) return cached;
      RemoteJsonClient client = new RemoteJsonClient(serviceId);
      buffer.put(serviceId, client);
      return client;
    } finally {
      lock.writeLock().unlock();
    }
  }
}
