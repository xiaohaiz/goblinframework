package org.goblinframework.remote.client.invocation.invoker.java;

import org.goblinframework.api.annotation.ThreadSafe;
import org.goblinframework.api.rpc.ImportService;
import org.goblinframework.core.util.ProxyUtils;
import org.goblinframework.core.util.ReflectionUtils;
import org.goblinframework.remote.client.module.runtime.RemoteServiceInformation;
import org.goblinframework.remote.client.module.runtime.RemoteServiceInformationManager;
import org.goblinframework.rpc.service.RemoteServiceId;
import org.goblinframework.rpc.service._ServiceVersionKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@ThreadSafe
final public class RemoteJavaClientFactory {

  private static final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
  private static final Map<RemoteServiceId, Object> buffer = new HashMap<>();

  @NotNull
  public static <E> E createJavaClient(@NotNull Class<E> interfaceClass) {
    return createJavaClient(interfaceClass, null);
  }

  @SuppressWarnings("unchecked")
  @NotNull
  public static <E> E createJavaClient(@NotNull Field field) {
    ImportService importService = field.getAnnotation(ImportService.class);
    if (importService == null || !importService.enable()) {
      throw new IllegalArgumentException("No @ImportService presented on field: " + field);
    }
    Class<?> interfaceClass = importService.interfaceClass();
    if (!interfaceClass.isInterface()) {
      throw new IllegalArgumentException("Interface class is required");
    }
    if (!field.getType().isAssignableFrom(interfaceClass)) {
      throw new IllegalArgumentException("Field [" + field.getType().getName() + "] is not [" + interfaceClass.getName() + "]");
    }
    String version = _ServiceVersionKt.calculateServerVersion(field);
    return (E) createJavaClient(interfaceClass, version);
  }

  @SuppressWarnings("unchecked")
  @NotNull
  public static <E> E createJavaClient(@NotNull Class<E> interfaceClass,
                                       @Nullable String specifiedVersion) {
    if (!interfaceClass.isInterface()) {
      throw new UnsupportedOperationException("Interface class is required");
    }
    String serviceVersion = specifiedVersion;
    if (serviceVersion == null) {
      RemoteServiceInformationManager informationManager = RemoteServiceInformationManager.INSTANCE;
      RemoteServiceInformation serviceInformation = informationManager.getRemoteServiceInformation(interfaceClass);
      serviceVersion = serviceInformation.getServiceVersion();
    }
    RemoteServiceId serviceId = new RemoteServiceId(interfaceClass.getName(), serviceVersion);
    lock.readLock().lock();
    try {
      Object cached = buffer.get(serviceId);
      if (cached != null) return (E) cached;
    } finally {
      lock.readLock().unlock();
    }
    lock.writeLock().lock();
    try {
      Object cached = buffer.get(serviceId);
      if (cached != null) return (E) cached;
      RemoteJavaClientInterceptor interceptor = new RemoteJavaClientInterceptor(serviceId);
      E proxy = ProxyUtils.createInterfaceProxy(interfaceClass, interceptor);
      E client = ProxyUtils.createInterfaceProxy(interfaceClass, invocation -> {
        Method method = invocation.getMethod();
        if (ReflectionUtils.isToStringMethod(method)) {
          return proxy.toString();
        }
        Object[] arguments = invocation.getArguments();
        if (method.isDefault()) {
          return ReflectionUtils.invokeInterfaceDefaultMethod(proxy, method, arguments);
        } else {
          return ReflectionUtils.invoke(proxy, method, arguments);
        }
      });
      buffer.put(serviceId, client);
      return client;
    } finally {
      lock.writeLock().unlock();
    }
  }
}
