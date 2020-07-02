package org.goblinframework.dao.ql;

import org.goblinframework.core.util.ArrayUtils;
import org.goblinframework.core.util.StringUtils;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Projection {

  private final Map<String, Boolean> projection = new ConcurrentHashMap<>();

  public Map<String, Boolean> getProjection() {
    return projection;
  }

  public boolean hasContent() {
    return !projection.isEmpty();
  }

  public Projection includes(String... fields) {
    if (ArrayUtils.isEmpty(fields)) {
      return this;
    }
    Arrays.stream(fields)
        .filter(StringUtils::isNotBlank)
        .forEach(f -> projection.put(f, true));
    return this;
  }

  public Projection excludes(String... fields) {
    if (ArrayUtils.isEmpty(fields)) {
      return this;
    }
    Arrays.asList(fields).stream()
        .filter(StringUtils::isNotBlank)
        .forEach(f -> projection.put(f, false));
    return this;
  }

}
