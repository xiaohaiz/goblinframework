package org.goblinframework.transport.core.protocol;

import java.util.HashMap;
import java.util.Map;

public enum TransportResponseCode {

  SERVER_ERROR((byte) 1),
  SUCCESS((byte) 0),
  CLIENT_ERROR((byte) -1);

  private final byte id;

  TransportResponseCode(byte id) {
    this.id = id;
  }

  public byte getId() {
    return id;
  }

  private static final Map<Byte, TransportResponseCode> mapping;

  static {
    mapping = new HashMap<>();
    for (TransportResponseCode it : values()) {
      mapping.put(it.id, it);
    }
  }

  public static TransportResponseCode parse(byte code) {
    return mapping.get(code);
  }

  public static boolean isSuccess(byte code) {
    return code == SUCCESS.getId();
  }
}
