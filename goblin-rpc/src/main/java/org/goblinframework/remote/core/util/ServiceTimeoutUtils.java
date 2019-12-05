package org.goblinframework.remote.core.util;

import org.goblinframework.api.rpc.ServiceTimeout;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;

final public class ServiceTimeoutUtils {

  public static long calculateServiceTimeout(@NotNull Class<?> interfaceClass, long defaultTimeout) {
    ServiceTimeout annotation = interfaceClass.getAnnotation(ServiceTimeout.class);
    Long timeout = calculateServiceTimeout(annotation);
    if (timeout == null) {
      return defaultTimeout;
    }
    return timeout <= 0 ? defaultTimeout : timeout;
  }

  @Nullable
  public static Long calculateServiceTimeout(@NotNull Method method) {
    ServiceTimeout serviceTimeout = method.getAnnotation(ServiceTimeout.class);
    return calculateServiceTimeout(serviceTimeout);
  }

  @Nullable
  public static Long calculateServiceTimeout(@Nullable ServiceTimeout annotation) {
    if (annotation == null || !annotation.enable()) {
      return null;
    }
    int t = annotation.timeout();
    if (t < 0) {
      return null;
    }
    long timeout = annotation.unit().toMillis(t);
    return timeout < 0 ? null : timeout;
  }

}
