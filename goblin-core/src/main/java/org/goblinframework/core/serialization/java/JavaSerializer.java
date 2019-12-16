package org.goblinframework.core.serialization.java;

import io.netty.buffer.*;
import io.netty.util.ReferenceCountUtil;
import org.apache.commons.lang3.SerializationUtils;
import org.goblinframework.api.annotation.Singleton;
import org.goblinframework.api.core.SerializerMode;
import org.goblinframework.core.serialization.GoblinSerializationException;
import org.goblinframework.core.serialization.Serializer;
import org.goblinframework.core.util.SystemUtils;
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
    if (SystemUtils.isNettyFound()) {
      ByteBuf buf = ByteBufAllocator.DEFAULT.buffer();
      try {
        try (ByteBufOutputStream bos = new ByteBufOutputStream(buf)) {
          SerializationUtils.serialize((Serializable) obj, bos);
        }
        return ByteBufUtil.getBytes(buf);
      } catch (Exception ex) {
        throw new GoblinSerializationException(ex);
      } finally {
        ReferenceCountUtil.release(buf);
      }
    } else {
      try {
        return SerializationUtils.serialize((Serializable) obj);
      } catch (Exception ex) {
        throw new GoblinSerializationException(ex);
      }
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
    if (SystemUtils.isNettyFound()) {
      ByteBuf buf = ByteBufAllocator.DEFAULT.buffer();
      try {
        buf.writeBytes(bs);
        try (ByteBufInputStream bis = new ByteBufInputStream(buf)) {
          return SerializationUtils.deserialize(bis);
        }
      } catch (Exception ex) {
        throw new GoblinSerializationException(ex);
      } finally {
        ReferenceCountUtil.release(buf);
      }
    } else {
      try {
        return SerializationUtils.deserialize(bs);
      } catch (Exception ex) {
        throw new GoblinSerializationException(ex);
      }
    }
  }
}
