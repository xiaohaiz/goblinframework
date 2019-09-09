package org.goblinframework.core.util;

import org.goblinframework.api.common.Ordered;
import org.jetbrains.annotations.NotNull;

abstract public class ObjectUtils extends org.apache.commons.lang3.ObjectUtils {
  /**
   * Calculate specified object's order with default value 0.
   */
  public static int calculateOrder(@NotNull Object obj) {
    if (obj instanceof Ordered) {
      return ((Ordered) obj).getOrder();
    }
    if (obj instanceof org.springframework.core.Ordered) {
      return ((org.springframework.core.Ordered) obj).getOrder();
    }
    return 0;
  }
}
