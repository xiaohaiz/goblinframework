package org.goblinframework.transport.core.protocol;

import java.io.Serializable;
import java.util.LinkedHashMap;

public class HeartbeatRequest implements Serializable {
  private static final long serialVersionUID = 6701427952454997299L;

  public String token;
  public LinkedHashMap<String, Object> extensions;

  public LinkedHashMap<String, Object> asMap() {
    LinkedHashMap<String, Object> map = new LinkedHashMap<>();
    map.put("_id", "HeartbeatRequest");
    if (token != null) {
      map.put("token", token);
    }
    if (extensions != null) {
      map.put("extensions", extensions);
    }
    return map;
  }
}
