package org.goblinframework.remote.core.util;

import org.goblinframework.api.remote.ExposeService;
import org.goblinframework.api.remote.ServiceVersion;
import org.goblinframework.core.util.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final public class ServiceVersionUtils {

  @NotNull
  public static String calculateServerVersion(@NotNull Class<?> interfaceClass) {
    ServiceVersion serviceVersion = interfaceClass.getAnnotation(ServiceVersion.class);
    String version = calculateServerVersion(serviceVersion);
    return StringUtils.defaultString(version, ServiceVersion.DEFAULT_VERSION);
  }

  @NotNull
  public static String calculateServerVersion(@NotNull ExposeService annotation) {
    if (!annotation.enable()) {
      throw new IllegalArgumentException();
    }
    String version = calculateServerVersion(annotation.version());
    if (version != null) {
      return version;
    }
    return calculateServerVersion(annotation.interfaceClass());
  }

  @Nullable
  public static String calculateServerVersion(@Nullable ServiceVersion annotation) {
    if (annotation == null || !annotation.enable()) {
      return null;
    }
    String s = annotation.version();
    return StringUtils.defaultIfBlank(s, (String) null);
  }
}
