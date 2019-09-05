package org.goblinframework.core.util;

final public class StringUtils {

  public static <T extends CharSequence> T defaultIfBlank(final T str, final T defaultStr) {
    return org.apache.commons.lang3.StringUtils.defaultIfBlank(str, defaultStr);
  }
}
