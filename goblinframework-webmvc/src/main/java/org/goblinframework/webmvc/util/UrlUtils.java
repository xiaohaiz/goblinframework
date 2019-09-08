package org.goblinframework.webmvc.util;

import org.goblinframework.core.util.StringUtils;
import org.jetbrains.annotations.Nullable;

final public class UrlUtils {

  public static boolean isMalformedPath(@Nullable final String path) {
    return path == null || path.contains("//") || path.contains("..");
  }

  @Nullable
  public static String compactContinuousSlashes(@Nullable final String path) {
    return StringUtils.compactContinuousSlashes(path);
  }
}
