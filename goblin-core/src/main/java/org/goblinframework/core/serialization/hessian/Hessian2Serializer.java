package org.goblinframework.core.serialization.hessian;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import com.caucho.hessian.io.HessianFactory;
import io.netty.buffer.*;
import io.netty.util.ReferenceCountUtil;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.goblinframework.api.annotation.Singleton;
import org.goblinframework.api.core.SerializerMode;
import org.goblinframework.core.serialization.GoblinSerializationException;
import org.goblinframework.core.serialization.Serializer;
import org.goblinframework.core.util.SystemUtils;
import org.jetbrains.annotations.NotNull;

import java.io.*;

@Singleton
final public class Hessian2Serializer implements Serializer {

  public static final Hessian2Serializer INSTANCE = new Hessian2Serializer();

  private final HessianFactory hessianFactory;

  private Hessian2Serializer() {
    this.hessianFactory = _Hessian2FactoryKt.getHessianFactory();
  }

  @NotNull
  @Override
  public SerializerMode mode() {
    return SerializerMode.HESSIAN2;
  }

  @Override
  public void serialize(@NotNull Object obj, @NotNull OutputStream outStream) {
    if (!(obj instanceof Serializable)) {
      throw GoblinSerializationException.requiredSerializable(obj);
    }
    Hessian2Output ho = null;
    try {
      ho = hessianFactory.createHessian2Output(outStream);
      ho.writeObject(obj);
      ho.flush();
    } catch (Exception ex) {
      throw new GoblinSerializationException(ex);
    } finally {
      hessianFactory.freeHessian2Output(ho);
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
          serialize(obj, bos);
        }
        return ByteBufUtil.getBytes(buf);
      } catch (IOException ex) {
        throw new GoblinSerializationException(ex);
      } finally {
        ReferenceCountUtil.release(buf);
      }
    } else {
      try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
        serialize(obj, bos);
        return bos.toByteArray();
      } catch (IOException ex) {
        throw new GoblinSerializationException(ex);
      }
    }
  }

  @NotNull
  @Override
  public Object deserialize(@NotNull InputStream inStream) {
    Hessian2Input hi = null;
    try {
      hi = hessianFactory.createHessian2Input(inStream);
      return hi.readObject();
    } catch (Exception ex) {
      throw new GoblinSerializationException(ex);
    } finally {
      hessianFactory.freeHessian2Input(hi);
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
          return deserialize(bis);
        }
      } catch (IOException ex) {
        throw new GoblinSerializationException(ex);
      } finally {
        ReferenceCountUtil.release(buf);
      }
    } else {
      try (ByteArrayInputStream bis = new ByteArrayInputStream(bs)) {
        return deserialize(bis);
      } catch (IOException ex) {
        throw new GoblinSerializationException(ex);
      }
    }
  }
}
