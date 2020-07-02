package org.goblinframework.core.system;

import org.goblinframework.api.annotation.Internal;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Internal
public interface ISubModule {

  @NotNull
  GoblinSubModule id();

  @Nullable
  default String managementEntrance() {
    return null;
  }

  default void install(@NotNull ModuleInstallContext ctx) {
  }

  default void initialize(@NotNull ModuleInitializeContext ctx) {
  }

  default void finalize(@NotNull ModuleFinalizeContext ctx) {
  }
}
