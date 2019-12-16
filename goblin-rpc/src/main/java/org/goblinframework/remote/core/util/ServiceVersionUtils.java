package org.goblinframework.remote.core.util;

import org.goblinframework.api.rpc.ExposeService;
import org.goblinframework.api.rpc.ImportService;
import org.goblinframework.api.rpc.ServiceVersion;
import org.goblinframework.core.util.StringUtils;
import org.goblinframework.rpc.service._ServiceVersionKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;

final public class ServiceVersionUtils {

  @Nullable
  public static String calculateServerVersion(@NotNull Field field) {
    ServiceVersion serviceVersion = field.getAnnotation(ServiceVersion.class);
    String version = _ServiceVersionKt.calculateServerVersion(serviceVersion);
    if (version != null) {
      return version;
    }
    ImportService importService = field.getAnnotation(ImportService.class);
    return _ServiceVersionKt.calculateServerVersion(importService);
  }

  @NotNull
  public static String calculateServerVersion(@NotNull Class<?> interfaceClass) {
    ServiceVersion serviceVersion = interfaceClass.getAnnotation(ServiceVersion.class);
    String version = _ServiceVersionKt.calculateServerVersion(serviceVersion);
    return StringUtils.defaultString(version, ServiceVersion.DEFAULT_VERSION);
  }

  @NotNull
  public static String calculateServerVersion(@NotNull ExposeService annotation) {
    if (!annotation.enable()) {
      throw new IllegalArgumentException();
    }
    String version = _ServiceVersionKt.calculateServerVersion(annotation.version());
    if (version != null) {
      return version;
    }
    return calculateServerVersion(annotation.interfaceClass());
  }

}
