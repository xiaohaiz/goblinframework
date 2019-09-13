package org.goblinframework.core.serialization.java;

import org.apache.commons.lang3.SerializationUtils;
import org.goblinframework.core.exception.GoblinSerializationException;
import org.goblinframework.core.serialization.Serializer;
import org.goblinframework.core.serialization.SerializerMode;
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;

final public class JS implements Serializer {

  public static final JS INSTANCE = new JS();

  private JS() {
  }

  @NotNull
  @Override
  public SerializerMode mode() {
    return SerializerMode.JAVA;
  }

  @Override
  public void serialize(@NotNull Object obj, @NotNull OutputStream outStream) {
    if (!(obj instanceof Serializable)) {
      throw GoblinSerializationException.requiredSerializable(obj);
    }
    SerializationUtils.serialize((Serializable) obj, outStream);
  }

  @NotNull
  @Override
  public byte[] serialize(@NotNull Object obj) {
    if (!(obj instanceof Serializable)) {
      throw GoblinSerializationException.requiredSerializable(obj);
    }
    return SerializationUtils.serialize((Serializable) obj);
  }

  @NotNull
  @Override
  public Object deserialize(@NotNull InputStream inStream) {
    return SerializationUtils.deserialize(inStream);
  }

  @NotNull
  @Override
  public Object deserialize(@NotNull byte[] bs) {
    return SerializationUtils.deserialize(bs);
  }
}
