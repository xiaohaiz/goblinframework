package org.goblinframework.api.spi;

import org.goblinframework.api.function.Block0;
import org.goblinframework.api.test.TestExecutionListener;
import org.jetbrains.annotations.NotNull;

public interface ExtModuleInstallContext extends ExtModuleContext {

  void registerPriorFinalizationTask(@NotNull Block0 action);

  void registerTestExecutionListener(@NotNull TestExecutionListener listener);

  void registerManagementController(@NotNull Object controller);

}
