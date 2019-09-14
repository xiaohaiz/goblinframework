package org.goblinframework.remote.core.protocol;

import java.io.Serializable;
import java.util.LinkedHashMap;

public class RemoteResponse implements Serializable {
  private static final long serialVersionUID = 6015472866822882653L;

  public byte code;
  public Object result;
  public LinkedHashMap<String, Object> extensions;

}
