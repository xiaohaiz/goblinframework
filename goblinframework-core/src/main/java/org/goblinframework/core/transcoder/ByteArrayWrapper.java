package org.goblinframework.core.transcoder;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

public class ByteArrayWrapper implements Serializable {
  private static final long serialVersionUID = 5538082034201587102L;

  private final byte[] content;

  public ByteArrayWrapper(@NotNull byte[] content) {
    this.content = content;
  }

  @NotNull
  public byte[] getContent() {
    return content;
  }
}
