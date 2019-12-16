package org.goblinframework.transport.protocol;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.LinkedHashMap;

/**
 * Transport handshake response data structure.
 *
 * @author Xiaohai Zhang
 * @serial
 * @since Sep 11, 2019
 */
public class HandshakeResponse implements Serializable {
  private static final long serialVersionUID = -3936150304695648862L;

  @JsonProperty("_id")
  public byte id = -1;
  public boolean success;
  public LinkedHashMap<String, Object> extensions;

}
