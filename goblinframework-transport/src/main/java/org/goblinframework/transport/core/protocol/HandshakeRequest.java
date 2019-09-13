package org.goblinframework.transport.core.protocol;

import java.io.Serializable;
import java.util.LinkedHashMap;

public class HandshakeRequest implements Serializable {
  private static final long serialVersionUID = 2982940442592457140L;

  public byte id = 1;
  public String serverId;
  public String clientId;
  public LinkedHashMap<String, Object> extensions;

}
