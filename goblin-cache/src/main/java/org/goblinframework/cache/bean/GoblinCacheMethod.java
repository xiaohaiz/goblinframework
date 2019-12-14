package org.goblinframework.cache.bean;

import java.util.LinkedList;
import java.util.List;

public class GoblinCacheMethod {

  public Class<?> type;

  public final List<GoblinCacheMethodParameter> parameterList = new LinkedList<>();
  public int annotationCount = 0;
  public int multipleCount = 0;
  public int multipleIndex = -1;
  public int parameterCount = 0;

}
