package org.goblinframework.core.util;

import org.goblinframework.api.annotation.Install;
import org.goblinframework.api.annotation.Singleton;
import org.goblinframework.api.annotation.ThreadSafe;
import org.goblinframework.api.common.Ordered;
import org.goblinframework.api.service.GoblinServiceException;
import org.goblinframework.api.service.IServiceInstaller;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Singleton
@ThreadSafe
final public class ServiceInstaller implements IServiceInstaller {

  @NotNull
  public static <E> List<E> installedList(@NotNull Class<E> serviceType) {
    return INSTANCE.asList(serviceType);
  }

  @Nullable
  public static <E> E installedFirst(@NotNull Class<E> serviceType) {
    return INSTANCE.firstOrNull(serviceType);
  }

  private static final ServiceInstaller INSTANCE = new ServiceInstaller();

  private ServiceInstaller() {
  }

  private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
  private final Map<Class<?>, Object> buffer = new IdentityHashMap<>(64);

  @NotNull
  @Override
  @SuppressWarnings("unchecked")
  public <E> List<E> asList(@NotNull Class<E> serviceType) {
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
  @Override
  public <E> E firstOrNull(@NotNull Class<E> serviceType) {
    return asList(serviceType).stream().findFirst().orElse(null);
  }

  @Install
  final public static class Installer implements IServiceInstaller, Ordered {

    @Override
    public int getOrder() {
      return HIGHEST_PRECEDENCE;
    }

    @NotNull
    @Override
    public <E> List<E> asList(@NotNull Class<E> serviceType) {
      return INSTANCE.asList(serviceType);
    }

    @Nullable
    @Override
    public <E> E firstOrNull(@NotNull Class<E> serviceType) {
      return INSTANCE.firstOrNull(serviceType);
    }
  }
}
