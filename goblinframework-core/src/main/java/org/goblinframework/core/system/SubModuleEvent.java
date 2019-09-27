package org.goblinframework.core.system;

import org.goblinframework.api.event.GoblinEvent;
import org.goblinframework.api.event.GoblinEventChannel;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@GoblinEventChannel("/goblin/core")
final public class SubModuleEvent extends GoblinEvent {

  @NotNull public ModuleContext ctx;
  @NotNull public List<ISubModule> subModules;

  SubModuleEvent(@NotNull ModuleContext ctx, @NotNull List<ISubModule> subModules) {
    this.ctx = ctx;
    this.subModules = subModules;
    if (this.ctx instanceof ModuleFinalizeContext) {
      setRaiseException(false);
    }
  }
}
