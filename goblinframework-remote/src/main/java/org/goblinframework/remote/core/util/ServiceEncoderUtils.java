package org.goblinframework.remote.core.util;

import org.goblinframework.api.core.SerializerMode;
import org.goblinframework.api.remote.ServiceEncoder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;

final public class ServiceEncoderUtils {

  @NotNull
  public static SerializerMode calculateServiceEncoder(@NotNull Class<?> interfaceClass, @NotNull SerializerMode defaultSerializer) {
    ServiceEncoder annotation = interfaceClass.getAnnotation(ServiceEncoder.class);
    SerializerMode serializer = calculateServiceEncoder(annotation);
    return serializer != null ? serializer : defaultSerializer;
  }

  @Nullable
  public static SerializerMode calculateServiceEncoder(@NotNull Method method) {
    ServiceEncoder serviceEncoder = method.getAnnotation(ServiceEncoder.class);
    return calculateServiceEncoder(serviceEncoder);
  }

  @Nullable
  public static SerializerMode calculateServiceEncoder(@Nullable ServiceEncoder annotation) {
    if (annotation == null || !annotation.enable()) {
      return null;
    }
    return annotation.serializer();
  }

}
