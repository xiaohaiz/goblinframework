package org.goblinframework.core.system;

import org.goblinframework.api.service.ServiceInstaller;
import org.goblinframework.api.system.GoblinSystemException;
import org.goblinframework.api.system.IModule;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

final class ModuleLoader {

  private static final Map<String, IModule> modules = new HashMap<>();

  static {
    ServiceInstaller.asList(IModule.class).forEach(e -> {
      String name = e.name();
      if (modules.putIfAbsent(name, e) != null) {
        throw new GoblinSystemException("Duplicated Module: " + name);
      }
    });
  }

  @Nullable
  static IModule module(@NotNull String name) {
    return modules.get(name);
  }
}