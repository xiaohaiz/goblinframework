package org.goblinframework.remote.core.util;

import org.goblinframework.api.remote.ServiceRetries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;

final public class ServiceRetriesUtils {

  public static int calculateServiceRetries(@NotNull Class<?> interfaceClass) {
    ServiceRetries annotation = interfaceClass.getAnnotation(ServiceRetries.class);
    Integer retries = calculateServiceRetries(annotation);
    return retries != null ? retries : 0;
  }

  @Nullable
  public static Integer calculateServiceRetries(@NotNull Method method) {
    ServiceRetries serviceRetries = method.getAnnotation(ServiceRetries.class);
    return calculateServiceRetries(serviceRetries);
  }

  @Nullable
  public static Integer calculateServiceRetries(@Nullable ServiceRetries annotation) {
    if (annotation == null || !annotation.enable()) {
      return null;
    }
    int retries = annotation.retries();
    return retries < 0 ? null : retries;
  }

}
