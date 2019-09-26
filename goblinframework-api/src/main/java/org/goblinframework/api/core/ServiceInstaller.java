package org.goblinframework.api.core;

import org.goblinframework.api.common.Internal;
import org.goblinframework.api.common.Ordered;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@ThreadSafe
final public class ServiceInstaller {

  private static final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
  private static final Map<Class<?>, Object> buffer = new IdentityHashMap<>(64);

  @NotNull
  @SuppressWarnings("unchecked")
  public static <E> List<E> asList(@NotNull Class<E> serviceType) {
    if (!serviceType.isInterface()) {
      throw new GoblinServiceException("Service type must be interface");
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
      ClassLoader classLoader = ServiceClassLoader.defaultClassLoader();
      ServiceLoader.load(serviceType, classLoader).forEach(installed::add);
      validateInternalService(serviceType, installed);
      installed.sort((o1, o2) -> {
        int p1 = 0, p2 = 0;
        if (o1 instanceof Ordered) {
          p1 = ((Ordered) o1).getOrder();
        }
        if (o2 instanceof Ordered) {
          p2 = ((Ordered) o2).getOrder();
        }
        return Integer.compare(p1, p2);
      });
      installed = Collections.unmodifiableList(installed);
      buffer.put(serviceType, installed);
      return installed;
    } finally {
      lock.writeLock().unlock();
    }
  }

  @Nullable
  public static <E> E firstOrNull(@NotNull Class<E> serviceType) {
    return asList(serviceType).stream().findFirst().orElse(null);
  }

  private static void validateInternalService(Class<?> serviceType, List<?> installed) {
    Internal annotation = serviceType.getAnnotation(Internal.class);
    if (annotation == null) {
      return;
    }
    if (annotation.installRequired() && installed.isEmpty()) {
      String errMsg = "%s not installed";
      errMsg = String.format(errMsg, serviceType.getName());
      throw new GoblinServiceException(errMsg);
    }
    if (annotation.uniqueInstance() && installed.size() > 1) {
      String errMsg = "%s requires unique instance, but %s installed";
      errMsg = String.format(errMsg, serviceType.getName(), installed.size());
      throw new GoblinServiceException(errMsg);
    }
  }
}
