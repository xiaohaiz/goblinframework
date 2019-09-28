package org.goblinframework.core.system;

import org.goblinframework.core.service.ServiceInstaller;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;

final public class ModuleLoader {

  private static final EnumMap<GoblinModule, IModule> modules;

  static {
    modules = new EnumMap<>(GoblinModule.class);
    ServiceInstaller.asList(IModule.class).forEach(e -> {
      GoblinModule id = e.id();
      if (modules.putIfAbsent(id, e) != null) {
        throw new GoblinSystemException("Duplicated Module: " + id);
      }
    });
  }

  @Nullable
  public static IModule module(@NotNull GoblinModule id) {
    return modules.get(id);
  }

}