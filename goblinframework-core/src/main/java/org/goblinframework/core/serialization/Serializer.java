package org.goblinframework.core.serialization;

import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.io.OutputStream;

public interface Serializer {

  byte JAVA = 1;
  byte FST = 2;
  byte HESSIAN2 = 3;

  byte id();

  void serialize(@NotNull Object obj, @NotNull OutputStream outStream);

  @NotNull
  byte[] serialize(@NotNull Object obj);

  @NotNull
  Object deserialize(@NotNull InputStream inStream);

  @NotNull
  Object deserialize(@NotNull byte[] bs);

}
