package org.goblinframework.core.serialization.fst;

import org.goblinframework.api.annotation.Singleton;
import org.goblinframework.api.core.SerializerMode;
import org.goblinframework.core.exception.GoblinSerializationException;
import org.goblinframework.core.serialization.Serializer;
import org.jetbrains.annotations.NotNull;
import org.nustaq.serialization.FSTConfiguration;
import org.nustaq.serialization.FSTObjectInput;
import org.nustaq.serialization.FSTObjectOutput;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;

@Singleton
final public class FstSerializer implements Serializer {

  public static final FstSerializer INSTANCE = new FstSerializer();

  private FstSerializer() {
  }

  @NotNull
  @Override
  public SerializerMode mode() {
    return SerializerMode.FST;
  }

  @Override
  public void serialize(@NotNull Object obj, @NotNull OutputStream outStream) {
    if (!(obj instanceof Serializable)) {
      throw GoblinSerializationException.requiredSerializable(obj);
    }
    FSTConfiguration configuration = FstConfigurationFactory.INSTANCE.getConfiguration();
    try {
      FSTObjectOutput fo = configuration.getObjectOutput(outStream);
      fo.writeObject(obj);
      fo.flush();
    } catch (IOException ex) {
      throw new GoblinSerializationException(ex);
    }
  }

  @NotNull
  @Override
  public byte[] serialize(@NotNull Object obj) {
    if (!(obj instanceof Serializable)) {
      throw GoblinSerializationException.requiredSerializable(obj);
    }
    FSTConfiguration configuration = FstConfigurationFactory.INSTANCE.getConfiguration();
    try {
      FSTObjectOutput fo = configuration.getObjectOutput();
      fo.writeObject(obj);
      return fo.getCopyOfWrittenBuffer();
    } catch (Exception ex) {
      throw new GoblinSerializationException(ex);
    }
  }

  @NotNull
  @Override
  public Object deserialize(@NotNull InputStream inStream) {
    FSTConfiguration configuration = FstConfigurationFactory.INSTANCE.getConfiguration();
    try {
      FSTObjectInput fi = configuration.getObjectInput(inStream);
      return fi.readObject();
    } catch (Exception ex) {
      throw new GoblinSerializationException(ex);
    }
  }

  @NotNull
  @Override
  public Object deserialize(@NotNull byte[] bs) {
    FSTConfiguration configuration = FstConfigurationFactory.INSTANCE.getConfiguration();
    try {
      FSTObjectInput fi = configuration.getObjectInput(bs);
      return fi.readObject();
    } catch (Exception ex) {
      throw new GoblinSerializationException(ex);
    }
  }
}
