package org.goblinframework.core.serialization;

import org.goblinframework.core.mbean.GoblinManagedBean;
import org.goblinframework.core.mbean.GoblinManagedObject;
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.io.OutputStream;

@GoblinManagedBean(type = "SERIALIZATION", name = "Serializer")
final class SerializerImpl extends GoblinManagedObject
    implements Serializer, SerializerMXBean {

  private final Serializer serializer;

  SerializerImpl(@NotNull Serializer serializer) {
    this.serializer = serializer;
  }

  @Override
  public byte id() {
    return serializer.id();
  }

  @Override
  public void serialize(@NotNull Object obj, @NotNull OutputStream outStream) {
    serializer.serialize(obj, outStream);
  }

  @NotNull
  @Override
  public byte[] serialize(@NotNull Object obj) {
    return serializer.serialize(obj);
  }

  @NotNull
  @Override
  public Object deserialize(@NotNull InputStream inStream) {
    return serializer.deserialize(inStream);
  }

  @NotNull
  @Override
  public Object deserialize(@NotNull byte[] bs) {
    return serializer.deserialize(bs);
  }

  @Override
  public byte getId() {
    return id();
  }
}
