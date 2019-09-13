package org.goblinframework.core.transcoder;

import java.io.Serializable;

public class DecodedObject implements Serializable {
  private static final long serialVersionUID = 1697647414761808900L;

  public Object decoded;
  public byte serializer;

  public DecodedObject() {
  }

  public DecodedObject(Object decoded, byte serializer) {
    this.decoded = decoded;
    this.serializer = serializer;
  }
}
