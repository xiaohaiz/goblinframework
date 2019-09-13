package org.goblinframework.transport.core.protocol;

import java.io.Serializable;
import java.util.LinkedHashMap;

public class HeartbeatResponse implements Serializable {
  private static final long serialVersionUID = 9197786040080581527L;

  public String token;
  public LinkedHashMap<String, Object> extensions;

}
