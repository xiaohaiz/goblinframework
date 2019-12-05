package org.goblinframework.core.system;

import org.goblinframework.api.function.Block0;
import org.goblinframework.api.test.TestExecutionListener;
import org.goblinframework.core.container.SpringContainerBeanPostProcessor;
import org.goblinframework.core.event.GoblinEventListener;
import org.jetbrains.annotations.NotNull;

public interface ModuleInstallContext extends ModuleContext {

  void registerPriorFinalizationTask(@NotNull Block0 action);

  void registerEventChannel(@NotNull String channel, int ringBufferSize, int workerHandlers);

  void subscribeEventListener(@NotNull GoblinEventListener listener);

  void registerTestExecutionListener(@NotNull TestExecutionListener listener);

  void registerManagementController(@NotNull Object controller);

  void registerContainerBeanPostProcessor(@NotNull SpringContainerBeanPostProcessor processor);
}
