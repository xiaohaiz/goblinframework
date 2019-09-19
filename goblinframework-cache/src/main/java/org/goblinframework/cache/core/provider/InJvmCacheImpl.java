package org.goblinframework.cache.core.provider;

import org.apache.commons.collections4.map.LRUMap;
import org.goblinframework.api.annotation.Singleton;
import org.goblinframework.api.annotation.ThreadSafe;
import org.goblinframework.cache.core.cache.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Singleton
@ThreadSafe
final public class InJvmCacheImpl extends AbstractGoblinCache {

  public static final InJvmCacheImpl INSTANCE = new InJvmCacheImpl();

  private InJvmCacheImpl() {
    super(new CacheSystemLocation(CacheSystem.JVM, "JVM"));
  }

  private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
  private final LRUMap<String, CacheItem> buffer = new LRUMap<>(65536);

  private static class CacheItem {
    private AtomicLong expireAt = new AtomicLong();
    private Object value;
    private final AtomicLong cas = new AtomicLong();

    private void touchExpirationInSeconds(int expirationInSeconds) {
      if (expirationInSeconds == 0) {
        expireAt.set(0);
      } else {
        expireAt.set(System.currentTimeMillis() + (expirationInSeconds * 1000));
      }
    }

    private boolean isExpired() {
      long e = expireAt.get();
      return e != 0 && System.currentTimeMillis() >= e;
    }
  }

  @Nullable
  private CacheItem retrieve(@NotNull String key) {
    CacheItem ci;
    lock.readLock().lock();
    try {
      ci = buffer.get(key);
    } finally {
      lock.readLock().unlock();
    }
    if (ci == null) return null;
    if (!ci.isExpired()) return ci;
    lock.writeLock().lock();
    try {
      buffer.remove(key);
    } finally {
      lock.writeLock().unlock();
    }
    return null;
  }

  @NotNull
  @Override
  public Object getNativeCache() {
    throw new UnsupportedOperationException();
  }

  @NotNull
  @Override
  public <T> GetResult<T> get(@Nullable String key) {
    if (key == null) {
      return new GetResult<>(null);
    }
    CacheItem ci = retrieve(key);
    if (ci == null) {
      return new GetResult<>(key);
    }
    GetResult<T> gr = new GetResult<>(key);
    gr.cas = ci.cas.get();
    gr.hit = true;
    if (ci.value instanceof CacheValueWrapper) {
      gr.wrapper = true;
      gr.uncheckedSetValue(((CacheValueWrapper) ci.value).getValue());
    } else {
      gr.uncheckedSetValue(ci.value);
    }
    return gr;
  }

  @Override
  public boolean delete(@Nullable String key) {
    if (key == null) {
      return false;
    }
    CacheItem ci;
    lock.writeLock().lock();
    try {
      ci = buffer.remove(key);
    } finally {
      lock.writeLock().unlock();
    }
    return ci != null && !ci.isExpired();
  }

  @Override
  public <T> boolean add(@Nullable String key, int expirationInSeconds, @Nullable T value) {
    if (key == null || expirationInSeconds < 0) {
      return false;
    }
    CacheItem ci = new CacheItem();
    ci.value = value;
    ci.touchExpirationInSeconds(expirationInSeconds);
    lock.writeLock().lock();
    try {
      return buffer.putIfAbsent(key, ci) == null;
    } finally {
      lock.writeLock().unlock();
    }
  }

  @Override
  public <T> boolean set(@Nullable String key, int expirationInSeconds, @Nullable T value) {
    if (key == null || expirationInSeconds < 0) {
      return false;
    }
    CacheItem ci = retrieve(key);
    if (ci != null) {
      ci.touchExpirationInSeconds(expirationInSeconds);
      ci.value = value;
      ci.cas.incrementAndGet();
      return true;
    }
    return add(key, expirationInSeconds, value);
  }

  @Override
  public <T> boolean replace(@Nullable String key, int expirationInSeconds, @Nullable T value) {
    if (key == null || expirationInSeconds < 0) {
      return false;
    }
    CacheItem ci = retrieve(key);
    if (ci == null) {
      return false;
    }
    ci.touchExpirationInSeconds(expirationInSeconds);
    ci.value = value;
    ci.cas.incrementAndGet();
    return true;
  }

  @Override
  public <T> boolean append(@Nullable String key, @Nullable T value) {
    if (key == null || !(value instanceof CharSequence)) {
      return false;
    }
    CacheItem ci = retrieve(key);
    if (ci == null || !(ci.value instanceof String)) {
      return false;
    }
    ci.value = ci.value.toString() + value.toString();
    ci.cas.incrementAndGet();
    return true;
  }

  @Override
  public boolean touch(@Nullable String key, int expirationInSeconds) {
    return false;
  }

  @Override
  public long ttl(@Nullable String key) {
    return 0;
  }

  @Override
  public long incr(@Nullable String key, long delta, long initialValue, int expirationInSeconds) {
    return 0;
  }

  @Override
  public long decr(@Nullable String key, long delta, long initialValue, int expirationInSeconds) {
    return 0;
  }

  @Override
  public <T> boolean cas(@Nullable String key, int expirationInSeconds, @Nullable GetResult<T> getResult, int maxTries, @Nullable CasOperation<T> casOperation) {
    return false;
  }
}
