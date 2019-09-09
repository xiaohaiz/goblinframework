package org.goblinframework.embedded.core.setting;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.goblinframework.core.util.StringUtils;
import org.jetbrains.annotations.Nullable;
import org.springframework.util.LinkedMultiValueMap;

import java.util.*;

public class HandlerSettings extends LinkedHashMap<String, HandlerSetting> {

  private final LinkedMultiValueMap<Integer, String> headers = new LinkedMultiValueMap<>();

  boolean addSetting(final String contextPath,
                     final HandlerSetting setting) {
    String using = contextPath;
    if (!"/".equals(using)) {
      using = using.endsWith("/") ? using : using + "/";
    }
    HandlerSetting previous = putIfAbsent(using, setting);
    if (previous != null) {
      return false;
    }
    int slashCount = StringUtils.countMatches(using, "/");
    headers.add(slashCount - 1, using);
    return true;
  }

  @Nullable
  public ImmutablePair<String, String> lookupContextPath(final String path) {
    if ("/".equals(path)) {
      return new ImmutablePair<>("/", "/");
    }
    if (size() == 1 && "/".equals(values().iterator().next().contextPath())) {
      return new ImmutablePair<>("/", path);
    }
    Set<Integer> sorted;
    if (headers.size() == 1) {
      sorted = Collections.singleton(headers.keySet().iterator().next());
    } else {
      sorted = new TreeSet<>(Comparator.reverseOrder());
      sorted.addAll(headers.keySet());
    }
    for (Integer slashCount : sorted) {
      int c = StringUtils.countMatches(path, "/");
      if (c < slashCount) {
        continue;
      }
      List<String> candidates = headers.get(slashCount);
      assert candidates != null;
      for (String header : candidates) {
        if (header.equals(path + "/") || path.startsWith(header)) {
          String p = "/" + StringUtils.substringAfter(path, header);
          return new ImmutablePair<>(header, p);
        }
      }
    }
    return null;
  }
}
