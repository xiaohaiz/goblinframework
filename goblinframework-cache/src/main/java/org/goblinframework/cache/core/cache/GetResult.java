package org.goblinframework.cache.core.cache;

import java.io.Serializable;

public class GetResult<T> implements Serializable {
  private static final long serialVersionUID = 4023608498489707013L;

  public long cas;
  public T value;
  public boolean hit;
  public boolean wrapper;
}
