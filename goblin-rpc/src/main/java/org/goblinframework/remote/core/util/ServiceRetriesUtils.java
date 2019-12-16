package org.goblinframework.remote.core.util;

import org.goblinframework.api.rpc.ServiceRetries;
import org.goblinframework.rpc.service._ServiceRetriesKt;
import org.jetbrains.annotations.NotNull;

final public class ServiceRetriesUtils {

  public static int calculateServiceRetries(@NotNull Class<?> interfaceClass) {
    ServiceRetries annotation = interfaceClass.getAnnotation(ServiceRetries.class);
    Integer retries = _ServiceRetriesKt.calculateServiceRetries(annotation);
    return retries != null ? retries : 0;
  }

}
