package org.goblinframework.remote.core.protocol;

import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public enum RemoteResponseCode {

  SUCCESS((byte) 0),
  SERVER_UNMARSHAL_REQUEST_ERROR((byte) 10),
  SERVER_MARSHAL_RESPONSE_ERROR((byte) 11),
  SERVER_BACK_PRESSURE_ERROR((byte) 30),
  SERVER_SERVICE_NOT_FOUND_ERROR((byte) 40),
  SERVER_METHOD_NOT_FOUND_ERROR((byte) 41),
  SERVER_SERVICE_EXECUTION_ERROR((byte) 50);

  private final byte id;

  RemoteResponseCode(byte id) {
    this.id = id;
  }

  public byte getId() {
    return id;
  }

  private static final Map<Byte, RemoteResponseCode> codes;

  static {
    codes = new HashMap<>();
    for (RemoteResponseCode code : values()) {
      codes.put(code.id, code);
    }
  }

  @Nullable
  public static RemoteResponseCode parse(byte id) {
    return codes.get(id);
  }
}
