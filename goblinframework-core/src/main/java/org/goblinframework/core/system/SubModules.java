package org.goblinframework.core.system;

import org.jetbrains.annotations.NotNull;

public interface SubModules {

  @NotNull
  SubModules next();

  @NotNull
  SubModules module(@NotNull GoblinSubModule... ids);

  void install(@NotNull ModuleInstallContext ctx);

  void initialize(@NotNull ModuleInitializeContext ctx);

  void finalize(@NotNull ModuleFinalizeContext ctx);
}
