package org.goblinframework.api.serializer;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.InputStream;
import java.io.OutputStream;

public interface Serializer {

  byte id();

  void serialize(@NotNull Object obj, @NotNull OutputStream outStream);

  @Nullable
  Object deserialize(@NotNull InputStream inStream);

}
