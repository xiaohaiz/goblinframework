package org.goblinframework.api.spi;

import org.goblinframework.api.annotation.External;
import org.goblinframework.api.function.Ordered;
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

  default void install(@NotNull ExtModuleInstallContext ctx) {
  }

  default void initialize(@NotNull ExtModuleInitializeContext ctx) {
  }

  default void finalize(@NotNull ExtModuleFinalizeContext ctx) {
  }
}
