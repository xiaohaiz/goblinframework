package org.goblinframework.cache.core.cache;

import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.lang.reflect.Field;

public class GetResult<T> implements Serializable {
  private static final long serialVersionUID = 4023608498489707013L;

  public String key;
  public long cas;
  public T value;
  public boolean hit;
  public boolean wrapper;

  public GetResult(@Nullable String key) {
    this.key = key;
  }

  public void uncheckedSetValue(@Nullable Object value) {
    try {
      Field field = GetResult.class.getField("value");
      field.set(this, value);
    } catch (Exception ex) {
      throw new UnsupportedOperationException();
    }
  }
}
