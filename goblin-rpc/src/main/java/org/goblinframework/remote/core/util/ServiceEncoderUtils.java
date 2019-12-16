package org.goblinframework.remote.core.util;

import org.goblinframework.api.core.SerializerMode;
import org.goblinframework.api.rpc.ServiceEncoder;
import org.goblinframework.rpc.service._ServiceEncoderKt;
import org.jetbrains.annotations.NotNull;

final public class ServiceEncoderUtils {

  @NotNull
  public static SerializerMode calculateServiceEncoder(@NotNull Class<?> interfaceClass, @NotNull SerializerMode defaultSerializer) {
    ServiceEncoder annotation = interfaceClass.getAnnotation(ServiceEncoder.class);
    SerializerMode serializer = _ServiceEncoderKt.calculateServiceEncoder(annotation);
    return serializer != null ? serializer : defaultSerializer;
  }

}
