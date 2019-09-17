package org.goblinframework.cache.core.cache;

import org.jetbrains.annotations.Nullable;

import java.io.Serializable;

public class CacheValueWrapper implements Serializable {
  private static final long serialVersionUID = 7739061014095517956L;

  private final Object value;

  public CacheValueWrapper(@Nullable Object value) {
    this.value = value;
  }

  @Nullable
  public Object getValue() {
    return value;
  }
}
