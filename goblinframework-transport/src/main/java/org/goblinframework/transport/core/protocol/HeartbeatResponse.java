package org.goblinframework.transport.core.protocol;

import java.io.Serializable;
import java.util.LinkedHashMap;

public class HeartbeatResponse implements Serializable {
  private static final long serialVersionUID = 9197786040080581527L;

  public String token;
  public LinkedHashMap<String, Object> extensions;

  public LinkedHashMap<String, Object> asMap() {
    LinkedHashMap<String, Object> map = new LinkedHashMap<>();
    map.put("_id", "HeartbeatResponse");
    if (token != null) {
      map.put("token", token);
    }
    if (extensions != null) {
      map.put("extensions", extensions);
    }
    return map;
  }
}
