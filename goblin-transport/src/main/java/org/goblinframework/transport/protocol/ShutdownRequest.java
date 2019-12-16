package org.goblinframework.transport.protocol;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.LinkedHashMap;

public class ShutdownRequest implements Serializable {
  private static final long serialVersionUID = -2381588255286959654L;

  @JsonProperty("_id")
  public byte id = 3;
  public String clientId;
  public LinkedHashMap<String, Object> extensions;

}
