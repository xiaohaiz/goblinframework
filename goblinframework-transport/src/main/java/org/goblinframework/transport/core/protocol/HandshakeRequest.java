package org.goblinframework.transport.core.protocol;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.LinkedHashMap;

public class HandshakeRequest implements Serializable {
  private static final long serialVersionUID = 2982940442592457140L;

  @JsonProperty("_id")
  public byte id = 1;
  public String serverId;
  public String clientId;
  public LinkedHashMap<String, Object> extensions;

}
