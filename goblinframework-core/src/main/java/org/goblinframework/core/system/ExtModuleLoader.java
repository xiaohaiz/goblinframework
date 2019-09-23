package org.goblinframework.core.system;

import org.goblinframework.api.service.ServiceInstaller;
import org.goblinframework.api.system.GoblinSystemException;
import org.goblinframework.api.system.IExtModule;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

final class ExtModuleLoader {

  private static final Map<String, IExtModule> extModules = new HashMap<>();

  static {
    ServiceInstaller.asList(IExtModule.class).forEach(e -> {
      String id = e.id();
      if (extModules.putIfAbsent(id, e) != null) {
        throw new GoblinSystemException("Duplicated ExtModule: " + id);
      }
    });
  }

  @NotNull
  static List<IExtModule> asList() {
    return extModules.values().stream()
        .sorted(Comparator.comparingInt(IExtModule::getOrder))
        .collect(Collectors.toList());
  }
}