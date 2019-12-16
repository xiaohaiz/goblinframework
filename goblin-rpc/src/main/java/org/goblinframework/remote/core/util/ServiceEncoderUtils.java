package org.goblinframework.remote.core.util;

import org.goblinframework.api.core.SerializerMode;
import org.goblinframework.api.rpc.ServiceEncoder;
import org.goblinframework.rpc.service._ServiceEncoderKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;

final public class ServiceEncoderUtils {

  @NotNull
  public static SerializerMode calculateServiceEncoder(@NotNull Class<?> interfaceClass, @NotNull SerializerMode defaultSerializer) {
    ServiceEncoder annotation = interfaceClass.getAnnotation(ServiceEncoder.class);
    SerializerMode serializer = _ServiceEncoderKt.calculateServiceEncoder(annotation);
    return serializer != null ? serializer : defaultSerializer;
  }

  @Nullable
  public static SerializerMode calculateServiceEncoder(@NotNull Method method) {
    ServiceEncoder serviceEncoder = method.getAnnotation(ServiceEncoder.class);
    return _ServiceEncoderKt.calculateServiceEncoder(serviceEncoder);
  }

}
