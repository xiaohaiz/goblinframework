package org.goblinframework.remote.server.service;

import org.goblinframework.api.rpc.ExposeService;
import org.goblinframework.api.rpc.ExposeServices;
import org.goblinframework.core.util.ClassUtils;
import org.goblinframework.remote.core.util.ServiceVersionUtils;
import org.goblinframework.rpc.service.RemoteServiceId;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

final public class RemoteServiceIdGenerator {

  @NotNull
  public static List<RemoteServiceId> generate(@NotNull Class<?> beanType) {
    Class<?> clazz = ClassUtils.filterCglibProxyClass(beanType);
    List<ExposeService> annotations = new ArrayList<>();
    ExposeService exposeService = clazz.getAnnotation(ExposeService.class);
    if (exposeService != null) {
      annotations.add(exposeService);
    }
    ExposeServices exposeServices = clazz.getAnnotation(ExposeServices.class);
    if (exposeServices != null && exposeServices.enable()) {
      Collections.addAll(annotations, exposeServices.value());
    }
    annotations.removeIf(e -> !e.enable());
    if (annotations.isEmpty()) {
      return Collections.emptyList();
    }
    List<RemoteServiceId> serviceIds = new ArrayList<>();
    for (ExposeService annotation : annotations) {
      serviceIds.add(generate(clazz, annotation));
    }
    return serviceIds.stream().distinct().collect(Collectors.toList());
  }

  private static RemoteServiceId generate(Class<?> clazz, ExposeService annotation) {
    Class<?> interfaceClass = annotation.interfaceClass();
    if (!interfaceClass.isAssignableFrom(clazz)) {
      throw new UnsupportedOperationException(clazz.getName() + " does not implement " + interfaceClass.getName());
    }
    String version = ServiceVersionUtils.calculateServerVersion(annotation);
    return new RemoteServiceId(interfaceClass.getName(), version);
  }
}
