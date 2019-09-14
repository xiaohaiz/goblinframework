package org.goblinframework.transport.core.protocol;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.LinkedHashMap;

public class HeartbeatResponse implements Serializable {
  private static final long serialVersionUID = 9197786040080581527L;

  @JsonProperty("_id")
  public byte id = -2;
  public String token;
  public LinkedHashMap<String, Object> extensions;

}
