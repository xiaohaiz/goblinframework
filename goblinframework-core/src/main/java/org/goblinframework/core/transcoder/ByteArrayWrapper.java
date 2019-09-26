package org.goblinframework.core.transcoder;

import org.goblinframework.core.util.ValueWrapper;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

/**
 * 如果如果传入byte[]，Transcoder缺省行为是不做任何编码操作，而是直接写入到输出流中，甚至
 * 会同时忽略魔术字。如果有特殊的场景，需要把字节数组作为正常的负载数据，怎么办？那就需要用
 * 到这里的{@code ByteArrayWrapper}。在解码的时候，如果发现解码后的对象是wrapper对象，
 * 就会继续取出其中的字节数组。
 */
public class ByteArrayWrapper implements ValueWrapper<byte[]>, Serializable {
  private static final long serialVersionUID = 5538082034201587102L;

  @NotNull private final byte[] content;

  public ByteArrayWrapper(@NotNull byte[] content) {
    this.content = content;
  }

  @NotNull
  @Override
  public byte[] getValue() {
    return content;
  }
}
