package org.goblinframework.core.serialization.hessian;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import org.goblinframework.api.common.Singleton;
import org.goblinframework.core.exception.GoblinSerializationException;
import org.goblinframework.core.serialization.Serializer;
import org.goblinframework.core.serialization.SerializerMode;
import org.jetbrains.annotations.NotNull;

import java.io.*;

@Singleton
final public class Hessian2Serializer implements Serializer {

  public static final Hessian2Serializer INSTANCE = new Hessian2Serializer();

  private Hessian2Serializer() {
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
      ho = GoblinHessianFactory.INSTANCE.createHessian2Output(outStream);
      ho.writeObject(obj);
      ho.flush();
    } catch (Exception ex) {
      throw new GoblinSerializationException(ex);
    } finally {
      GoblinHessianFactory.INSTANCE.freeHessian2Output(ho);
    }
  }

  @NotNull
  @Override
  public byte[] serialize(@NotNull Object obj) {
    if (!(obj instanceof Serializable)) {
      throw GoblinSerializationException.requiredSerializable(obj);
    }
    try (ByteArrayOutputStream bos = new ByteArrayOutputStream(512)) {
      serialize(obj, bos);
      return bos.toByteArray();
    } catch (IOException ex) {
      throw new GoblinSerializationException(ex);
    }
  }

  @NotNull
  @Override
  public Object deserialize(@NotNull InputStream inStream) {
    Hessian2Input hi = null;
    try {
      hi = GoblinHessianFactory.INSTANCE.createHessian2Input(inStream);
      return hi.readObject();
    } catch (Exception ex) {
      throw new GoblinSerializationException(ex);
    } finally {
      GoblinHessianFactory.INSTANCE.freeHessian2Input(hi);
    }
  }

  @NotNull
  @Override
  public Object deserialize(@NotNull byte[] bs) {
    try (ByteArrayInputStream bis = new ByteArrayInputStream(bs)) {
      return deserialize(bis);
    } catch (IOException ex) {
      throw new GoblinSerializationException(ex);
    }
  }
}
