package org.goblinframework.core.serialization.java;

import org.apache.commons.lang3.SerializationUtils;
import org.goblinframework.api.common.Singleton;
import org.goblinframework.core.exception.GoblinSerializationException;
import org.goblinframework.core.serialization.Serializer;
import org.goblinframework.core.serialization.SerializerMode;
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;

@Singleton
final public class JavaSerializer implements Serializer {

  public static final JavaSerializer INSTANCE = new JavaSerializer();

  private JavaSerializer() {
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
    try {
      SerializationUtils.serialize((Serializable) obj, outStream);
    } catch (Exception ex) {
      throw new GoblinSerializationException(ex);
    }
  }

  @NotNull
  @Override
  public byte[] serialize(@NotNull Object obj) {
    if (!(obj instanceof Serializable)) {
      throw GoblinSerializationException.requiredSerializable(obj);
    }
    try {
      return SerializationUtils.serialize((Serializable) obj);
    } catch (Exception ex) {
      throw new GoblinSerializationException(ex);
    }
  }

  @NotNull
  @Override
  public Object deserialize(@NotNull InputStream inStream) {
    try {
      return SerializationUtils.deserialize(inStream);
    } catch (Exception ex) {
      throw new GoblinSerializationException(ex);
    }
  }

  @NotNull
  @Override
  public Object deserialize(@NotNull byte[] bs) {
    try {
      return SerializationUtils.deserialize(bs);
    } catch (Exception ex) {
      throw new GoblinSerializationException(ex);
    }
  }
}
