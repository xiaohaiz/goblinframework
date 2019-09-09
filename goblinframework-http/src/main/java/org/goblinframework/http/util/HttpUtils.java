package org.goblinframework.http.util;

import org.goblinframework.core.util.StringUtils;
import org.jetbrains.annotations.Nullable;

abstract public class HttpUtils {

  public static boolean isMalformedPath(@Nullable final String path) {
    return path == null || path.contains("//") || path.contains("..");
  }

  @Nullable
  public static String compactContinuousSlashes(@Nullable final String path) {
    if (path == null) {
      return null;
    }
    String s = path;
    if (StringUtils.contains(s, "//")) {
      while (StringUtils.contains(s, "//")) {
        s = StringUtils.replace(s, "//", "/");
      }
    }
    if ("/".equals(s)) {
      return "/";
    }
    if (StringUtils.endsWith(s, "/")) {
      s = s.substring(0, s.length() - 1);
    }
    return s;
  }
}
