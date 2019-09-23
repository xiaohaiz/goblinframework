package org.goblinframework.api.system;

import org.jetbrains.annotations.NotNull;

public interface ModuleContext {

  @NotNull
  SubModules createSubModules();

}
