package org.goblinframework.transport.core.protocol;

import java.io.Serializable;
import java.util.LinkedHashMap;

public class HandshakeRequest implements Serializable {
  private static final long serialVersionUID = 2982940442592457140L;

  public String serverId;
  public String clientId;
  public LinkedHashMap<String, Object> extensions;

  public LinkedHashMap<String, Object> asMap() {
    LinkedHashMap<String, Object> map = new LinkedHashMap<>();
    map.put("_id", "HandshakeRequest");
    map.put("serverId", serverId);
    map.put("clientId", clientId);
    if (extensions != null) {
      map.put("extensions", extensions);
    }
    return map;
  }
}
