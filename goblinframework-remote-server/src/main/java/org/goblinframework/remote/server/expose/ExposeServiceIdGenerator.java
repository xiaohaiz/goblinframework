package org.goblinframework.remote.server.expose;

import org.goblinframework.api.annotation.ExposeService;
import org.goblinframework.api.annotation.ExposeServices;
import org.goblinframework.core.exception.GoblinMalformedException;
import org.goblinframework.core.util.ClassUtils;
import org.jetbrains.annotations.NotNull;

import java.util.*;

abstract public class ExposeServiceIdGenerator {

  @NotNull
  public static List<ExposeServiceId> generate(@NotNull final Class<?> type) {
    Class<?> clazz = ClassUtils.filterCglibProxyClass(type);

    List<ExposeService> annotations = findExposeService(clazz);
    if (annotations.isEmpty()) {
      return Collections.emptyList();
    }
    Set<ExposeServiceId> idList = new LinkedHashSet<>();
    for (ExposeService annotation : annotations) {
      idList.add(generateExposeServiceId(clazz, annotation));
    }
    return new ArrayList<>(idList);
  }

  private static List<ExposeService> findExposeService(Class<?> clazz) {
    List<ExposeService> annotations = new LinkedList<>();
    ExposeService s = clazz.getAnnotation(ExposeService.class);
    if (s != null && s.enable()) {
      annotations.add(s);
    }
    ExposeServices m = clazz.getAnnotation(ExposeServices.class);
    if (m != null && m.enable()) {
      Collections.addAll(annotations, m.value());
    }
    return annotations;
  }

  private static ExposeServiceId generateExposeServiceId(Class<?> clazz, ExposeService annotation) {
    Class<?> interfaceClass = annotation.interfaceClass();
    if (interfaceClass.isAssignableFrom(clazz)) {
      throw new GoblinMalformedException(clazz.getName() + " does not implement " + interfaceClass.getName());
    }
    return new ExposeServiceId(interfaceClass, "goblin", "1.0");
  }
}
