package org.goblinframework.core.system;

import org.goblinframework.api.annotation.External;
import org.goblinframework.api.core.Ordered;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@External
public interface IExtModule extends Ordered {

  @Override
  default int getOrder() {
    return 0;
  }

  @NotNull
  String id();

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
