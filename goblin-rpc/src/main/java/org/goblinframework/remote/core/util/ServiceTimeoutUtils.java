package org.goblinframework.remote.core.util;

import org.goblinframework.api.rpc.ServiceTimeout;
import org.goblinframework.rpc.service._ServiceTimeoutKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;

final public class ServiceTimeoutUtils {

  public static long calculateServiceTimeout(@NotNull Class<?> interfaceClass, long defaultTimeout) {
    ServiceTimeout annotation = interfaceClass.getAnnotation(ServiceTimeout.class);
    Long timeout = _ServiceTimeoutKt.calculateServiceTimeout(annotation);
    if (timeout == null) {
      return defaultTimeout;
    }
    return timeout <= 0 ? defaultTimeout : timeout;
  }

  @Nullable
  public static Long calculateServiceTimeout(@NotNull Method method) {
    ServiceTimeout serviceTimeout = method.getAnnotation(ServiceTimeout.class);
    return _ServiceTimeoutKt.calculateServiceTimeout(serviceTimeout);
  }


}
