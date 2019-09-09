package org.goblinframework.core.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.util.ConcurrentReferenceHashMap;

import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * GOBLIN specified service loader through standard {@link java.util.ServiceLoader} mechanism.
 */
final public class GoblinServiceLoader {

  private static final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
  private static final Map<Class<?>, Object> buffer = new ConcurrentReferenceHashMap<>(64);

  @NotNull
  @SuppressWarnings("unchecked")
  public static <E> List<E> installedList(@NotNull Class<E> serviceType) {
    if (!serviceType.isInterface()) {
      throw new IllegalArgumentException("Service type must be interface");
    }
    lock.readLock().lock();
    try {
      Object cached = buffer.get(serviceType);
      if (cached != null) {
        return (List<E>) cached;
      }
    } finally {
      lock.readLock().unlock();
    }

    lock.writeLock().lock();
    try {
      Object cached = buffer.get(serviceType);
      if (cached != null) {
        return (List<E>) cached;
      }
      List<E> installed = new LinkedList<>();
      ClassLoader classLoader = ClassUtils.getDefaultClassLoader();
      ServiceLoader.load(serviceType, classLoader).forEach(installed::add);
      installed.sort(Comparator.comparingInt(ObjectUtils::calculateOrder));
      installed = Collections.unmodifiableList(installed);
      buffer.put(serviceType, installed);
      return installed;
    } finally {
      lock.writeLock().unlock();
    }
  }

  @Nullable
  public static <E> E installedFirst(@NotNull Class<E> serviceType) {
    return installedList(serviceType).stream().findFirst().orElse(null);
  }
}
