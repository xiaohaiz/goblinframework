package org.goblinframework.api.system;

import org.jetbrains.annotations.NotNull;

public interface SubModules {

  @NotNull
  SubModules next();

  @NotNull
  SubModules module(@NotNull String... names);

  void install(@NotNull ModuleInstallContext ctx);

  void initialize(@NotNull ModuleInitializeContext ctx);

  void finalize(@NotNull ModuleFinalizeContext ctx);
}
