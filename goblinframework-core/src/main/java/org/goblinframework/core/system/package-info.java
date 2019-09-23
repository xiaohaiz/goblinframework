package org.goblinframework.core.system;

import org.goblinframework.api.service.ServiceInstaller;
import org.goblinframework.api.system.GoblinSystemException;
import org.goblinframework.api.system.ISubModule;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

final class SubModuleLoader {

  private static final Map<String, ISubModule> subModules = new HashMap<>();

  static {
    ServiceInstaller.asList(ISubModule.class).forEach(e -> {
      String name = e.name();
      if (subModules.putIfAbsent(name, e) != null) {
        throw new GoblinSystemException("Duplicated SubModule: " + name);
      }
    });
  }

  @Nullable
  static ISubModule subModule(@NotNull String name) {
    return subModules.get(name);
  }
}