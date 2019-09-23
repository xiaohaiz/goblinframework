package org.goblinframework.api.monitor;

import org.goblinframework.api.annotation.Internal;
import org.goblinframework.api.service.ServiceInstaller;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Internal
public interface IInstructionManager {

  void register(@NotNull Instruction instruction);

  @Nullable
  static IInstructionManager instance() {
    return ServiceInstaller.firstOrNull(IInstructionManager.class);
  }
}
