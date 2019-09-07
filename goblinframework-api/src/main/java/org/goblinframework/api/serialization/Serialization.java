package org.goblinframework.api.serialization;

import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.io.OutputStream;

@Deprecated
public interface Serialization {

  @NotNull
  Serializer0 getSerializer();

  void serialize(@NotNull Object obj, @NotNull OutputStream outStream);

  @NotNull
  Object deserialize(@NotNull InputStream inStream);

}
