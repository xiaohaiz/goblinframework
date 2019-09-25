package org.goblinframework.api.system;

import org.goblinframework.api.config.ConfigListener;
import org.goblinframework.api.config.ConfigParser;
import org.goblinframework.api.container.SpringContainerBeanPostProcessor;
import org.goblinframework.api.event.GoblinEventListener;
import org.goblinframework.api.test.TestExecutionListener;
import org.jetbrains.annotations.NotNull;

public interface ModuleInstallContext extends ModuleContext {

  void registerEventChannel(@NotNull String channel, int ringBufferSize, int workerHandlers);

  void subscribeEventListener(@NotNull GoblinEventListener listener);

  void registerTestExecutionListener(@NotNull TestExecutionListener listener);

  void registerManagementController(@NotNull Object controller);

  void registerConfigParser(@NotNull ConfigParser parser);

  void registerConfigListener(@NotNull ConfigListener listener);

  void registerContainerBeanPostProcessor(@NotNull SpringContainerBeanPostProcessor processor);
}
