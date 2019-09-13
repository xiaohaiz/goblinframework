package org.goblinframework.core.transcoder;

public class DecodedObject {

  public Object decoded;
  public byte serializer;

  public DecodedObject() {
  }

  public DecodedObject(Object decoded, byte serializer) {
    this.decoded = decoded;
    this.serializer = serializer;
  }
}
