package org.goblinframework.core.transcoder;

import java.io.Serializable;

public class DecodeResult implements Serializable {
  private static final long serialVersionUID = -3001135949780747801L;

  public Object result;     // should be raw bytes in case of no MAGIC number
  public boolean magic;     // if MAGIC number found
  public byte compressor;   // 0 means no compressor
  public byte serializer;   // 0 means UTF-8 encoded string

}
