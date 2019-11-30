package org.goblinframework.remote.server.service;

import org.goblinframework.api.remote.ExposeService;
import org.goblinframework.api.remote.ExposeServices;
import org.goblinframework.core.util.ClassUtils;
import org.goblinframework.remote.core.service.RemoteServiceId;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

final public class RemoteServiceIdGenerator {

  @NotNull
  public static List<RemoteServiceId> generate(@NotNull Class<?> beanType) {
    Class<?> clazz = ClassUtils.filterCglibProxyClass(beanType);
    List<ExposeService> annotations = new ArrayList<>();
    ExposeService exposeService = clazz.getAnnotation(ExposeService.class);
    if (exposeService != null && exposeService.enable()) {
      annotations.add(exposeService);
    }
    ExposeServices exposeServices = clazz.getAnnotation(ExposeServices.class);
    if (exposeServices != null && exposeServices.enable()) {
      for (ExposeService it : exposeServices.value()) {

      }
    }
    return null;
  }
}
