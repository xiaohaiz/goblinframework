package org.goblinframework.cache.bean;

import org.apache.commons.lang3.mutable.MutableObject;
import org.goblinframework.core.util.ClassUtils;
import org.jetbrains.annotations.NotNull;

import java.util.IdentityHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class CacheBeanManager {

  private static final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
  private static final IdentityHashMap<Class<?>, MutableObject<CacheBean>> buffer = new IdentityHashMap<>();

  @NotNull
  public static CacheBean getGoblinCacheBean(@NotNull final Class<?> type) {
    Class<?> realClass = ClassUtils.filterCglibProxyClass(type);

    MutableObject<CacheBean> wrapper;
    lock.readLock().lock();
    try {
      wrapper = buffer.get(realClass);
    } finally {
      lock.readLock().unlock();
    }
    if (wrapper != null) {
      return wrapper.getValue();
    }

    lock.writeLock().lock();
    try {
      wrapper = buffer.get(realClass);
      if (wrapper != null) {
        return wrapper.getValue();
      }
      CacheBean capsule = CacheBeanBuilder.build(realClass);
      wrapper = new MutableObject<>(capsule);
      buffer.put(realClass, wrapper);
      return capsule;
    } finally {
      lock.writeLock().unlock();
    }

  }

}
