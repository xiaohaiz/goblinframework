package org.goblinframework.core.util;

import org.jetbrains.annotations.NotNull;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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

  private static List<String> hostAddresses = null;

  @NotNull
  public synchronized static List<String> getHostAddresses() {
    if (hostAddresses == null) {
      try {
        hostAddresses = determineHostAddresses();
      } catch (Exception ex) {
        throw new GoblinNetworkException(ex);
      }
    }
    return hostAddresses;
  }

  @NotNull
  private static List<String> determineHostAddresses() throws Exception {
    List<InetAddress> list = new ArrayList<>();
    Enumeration<NetworkInterface> it = NetworkInterface.getNetworkInterfaces();
    if (it == null) {
      return Collections.emptyList();
    }
    while (it.hasMoreElements()) {
      Enumeration<InetAddress> addresses = it.nextElement().getInetAddresses();
      while (addresses.hasMoreElements()) {
        InetAddress address = addresses.nextElement();
        if (isValidAddress(address)) {
          list.add(address);
        }
      }
    }
    return list.stream()
        .map(InetAddress::getHostAddress)
        .distinct()
        .collect(Collectors.toList());
  }
}
