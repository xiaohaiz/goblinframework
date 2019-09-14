package org.goblinframework.transport.core.protocol;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.LinkedHashMap;

public class HeartbeatRequest implements Serializable {
  private static final long serialVersionUID = 6701427952454997299L;

  @JsonProperty("_id")
  public byte id = 2;
  public String token;
  public LinkedHashMap<String, Object> extensions;

}
