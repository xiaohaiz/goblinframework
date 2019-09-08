package org.goblinframework.core.util;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.util.regex.Pattern;

final public class NetworkUtils {

  private static final Pattern IP_PATTERN = Pattern.compile("\\d{1,3}(\\.\\d{1,3}){3,5}$");

  public static boolean isValidAddress(InetAddress address) {
    if (address == null || address.isLoopbackAddress())
      return false;
    if (address instanceof Inet6Address) {
      return false;
    }
    String name = address.getHostAddress();
    return (name != null
        && !"0.0.0.0".equals(name)
        && !"127.0.0.1".equals(name)
        && IP_PATTERN.matcher(name).matches());
  }
}
