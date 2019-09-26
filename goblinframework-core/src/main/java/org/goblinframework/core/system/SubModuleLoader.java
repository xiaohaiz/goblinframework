package org.goblinframework.core.system;

import org.goblinframework.api.core.ServiceInstaller;
import org.goblinframework.api.system.GoblinSubModule;
import org.goblinframework.api.system.GoblinSystemException;
import org.goblinframework.api.system.ISubModule;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;

final public class SubModuleLoader {

  private static final EnumMap<GoblinSubModule, ISubModule> subModules;

  static {
    subModules = new EnumMap<>(GoblinSubModule.class);
    ServiceInstaller.asList(ISubModule.class).forEach(e -> {
      GoblinSubModule id = e.id();
      if (subModules.putIfAbsent(id, e) != null) {
        throw new GoblinSystemException("Duplicated SubModule: " + id);
      }
    });
  }

  @Nullable
  public static ISubModule subModule(@NotNull GoblinSubModule id) {
    return subModules.get(id);
  }
}