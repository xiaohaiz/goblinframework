package org.goblinframework.remote.core.protocol;

import java.io.Serializable;
import java.util.LinkedHashMap;

public class RemoteRequest implements Serializable {
  private static final long serialVersionUID = 1108921507796393958L;

  public String serviceInterface;
  public String serviceGroup;
  public String serviceVersion;
  public String methodName;
  public String[] parameterTypes;
  public String returnType;
  public Object[] arguments;
  public LinkedHashMap<String, Object> extensions;

}
