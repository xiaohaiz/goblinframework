package org.goblinframework.cache.bean;

import org.goblinframework.api.annotation.ThreadSafe;
import org.goblinframework.cache.exception.MalformedCacheBeanException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@ThreadSafe
final public class GoblinCacheDimension {

  private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
  private final IdentityHashMap<Class<?>, Values> buffer = new IdentityHashMap<>();

  private final Class<?> defaultType;
  private final GoblinCacheBean cacheBean;

  public GoblinCacheDimension(@NotNull Class<?> defaultType, @NotNull GoblinCacheBean cacheBean) {
    this.defaultType = defaultType;
    this.cacheBean = cacheBean;
  }

  @NotNull
  public Values get() {
    return get(defaultType);
  }

  @NotNull
  public Values get(@NotNull Class<?> type) {
    if (cacheBean.getGoblinCache(type) == null) {
      throw new MalformedCacheBeanException("(" + type.getName() + ") not found in @GoblinCacheBean(s)");
    }
    Values vs;
    lock.readLock().lock();
    try {
      vs = buffer.get(type);
      if (vs != null) return vs;
    } finally {
      lock.readLock().unlock();
    }
    lock.writeLock().lock();
    try {
      vs = buffer.get(type);
      if (vs != null) return vs;
      vs = new Values();
      buffer.put(type, vs);
      return vs;
    } finally {
      lock.writeLock().unlock();
    }
  }

  public Map<Class<?>, Set<String>> dump() {
    lock.readLock().lock();
    try {
      Map<Class<?>, Set<String>> map = new LinkedHashMap<>();
      buffer.forEach((type, values) -> map.put(type, values.toSet()));
      return map;
    } finally {
      lock.readLock().unlock();
    }
  }

  public void evict() {
    dump().forEach((type, keys) -> {
      GoblinCache gc = cacheBean.getGoblinCache(type);
      assert gc != null;
      gc.cache().deletes(keys);
    });
  }

  final public static class Values {

    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private final List<String> keys = new LinkedList<>();

    public void add(@Nullable String key) {
      if (key == null) return;
      lock.writeLock().lock();
      try {
        keys.add(key);
      } finally {
        lock.writeLock().unlock();
      }
    }

    @NotNull
    public Set<String> toSet() {
      lock.readLock().lock();
      try {
        return new LinkedHashSet<>(keys);
      } finally {
        lock.readLock().unlock();
      }
    }
  }
}
