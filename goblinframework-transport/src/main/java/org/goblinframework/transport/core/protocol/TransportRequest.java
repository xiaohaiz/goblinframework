package org.goblinframework.transport.core.protocol;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.LinkedHashMap;

public class TransportRequest implements Serializable {
  private static final long serialVersionUID = 1686136429375478570L;

  @JsonProperty("_id")
  public byte id = 4;
  public long requestId;                            // 唯一的请求id
  public long requestCreateTime;                    // 请求创建时间
  public boolean response;                          // 是否需要服务端响应
  public byte compressor;                           // 是否使用了压缩，0表示无
  public byte serializer;                           // 是否使用了序列化，0表示是字符串对应的字节数组
  public byte[] payload;                            // 负载数据
  public boolean hasPayload;                        // 是否有负载数据
  public boolean rawPayload;                        // 直接写入字节数组作为负载数据
  public LinkedHashMap<String, Object> extensions;

}
