package org.goblinframework.core.transcoder;

public class DecodedObject {

  public Object object;
  public byte serializer;

  public DecodedObject() {
  }

  public DecodedObject(Object object, byte serializer) {
    this.object = object;
    this.serializer = serializer;
  }
}
