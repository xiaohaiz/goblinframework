package org.goblinframework.api.service;

import org.goblinframework.api.common.Ordered;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

final class ServiceInstallerLoader {

  static final IServiceInstaller installer;

  static {
    ClassLoader classLoader = ServiceClassLoader.defaultClassLoader();
    List<IServiceInstaller> list = new ArrayList<>();
    ServiceLoader.load(IServiceInstaller.class, classLoader).forEach(list::add);
    installer = list.stream()
        .min((o1, o2) -> {
          int p1 = 0, p2 = 0;
          if (o1 instanceof Ordered) {
            p1 = ((Ordered) o1).getOrder();
          }
          if (o2 instanceof Ordered) {
            p2 = ((Ordered) o2).getOrder();
          }
          return Integer.compare(p1, p2);
        })
        .orElse(null);
  }
}