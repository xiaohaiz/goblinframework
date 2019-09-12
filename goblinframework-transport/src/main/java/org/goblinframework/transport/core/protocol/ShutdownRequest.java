package org.goblinframework.transport.core.protocol;

import java.io.Serializable;
import java.util.LinkedHashMap;

public class ShutdownRequest implements Serializable {
  private static final long serialVersionUID = -2381588255286959654L;

  public String clientId;
  public LinkedHashMap<String, Object> extensions;

  public LinkedHashMap<String, Object> asMap() {
    LinkedHashMap<String, Object> map = new LinkedHashMap<>();
    map.put("_id", "ShutdownRequest");
    map.put("clientId", clientId);
    map.put("extensions", extensions);
    return map;
  }
}
