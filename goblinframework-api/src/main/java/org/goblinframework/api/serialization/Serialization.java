package org.goblinframework.api.serialization;

import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.io.OutputStream;

public interface Serialization {

  @NotNull
  Serializer getSerializer();

  void serialize(@NotNull Object obj, @NotNull OutputStream outStream);

  @NotNull
  Object deserialize(@NotNull InputStream inStream);

}
