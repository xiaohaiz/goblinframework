package org.goblinframework.transport.core.protocol;

import java.io.Serializable;
import java.util.LinkedHashMap;

public class TransportMessage implements Serializable {
  private static final long serialVersionUID = 4215017125923074620L;

  public Object message;
  public byte serializer;
  public LinkedHashMap<String, Object> extensions;

}
