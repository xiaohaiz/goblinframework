package org.goblinframework.transport.core.protocol;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.LinkedHashMap;

public class TransportResponse implements Serializable, TransportPayload {
  private static final long serialVersionUID = 240065800082739072L;

  @JsonProperty("_id")
  public byte id = -4;
  public long requestId;                            // 唯一的请求id
  public long requestCreateTime;                    // 请求创建时间
  public long responseCreateTime;                   // 响应创建时间
  public byte code;                                 // 响应code
  public byte compressor;                           // 是否使用了压缩，0表示无
  public byte serializer;                           // 是否使用了序列化，0表示是字符串对应的字节数组
  public byte[] payload;                            // 负载数据
  public boolean hasPayload;                        // 是否有负载数据
  public boolean rawPayload;                        // 直接写入字节数组作为负载数据
  public LinkedHashMap<String, Object> extensions;

  @Nullable
  @Override
  public byte[] readPayload() {
    return payload;
  }

  @Override
  public void writePayload(@Nullable byte[] payload) {
    this.payload = payload;
  }
}
