package org.goblinframework.cache.core.cache;

import org.goblinframework.api.function.ValueWrapper;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;

public class CacheValueWrapper implements ValueWrapper<Object>, Serializable {
  private static final long serialVersionUID = 7739061014095517956L;

  @Nullable private final Object value;

  public CacheValueWrapper(@Nullable Object value) {
    this.value = value;
  }

  @Nullable
  @Override
  public Object getValue() {
    return value;
  }
}
