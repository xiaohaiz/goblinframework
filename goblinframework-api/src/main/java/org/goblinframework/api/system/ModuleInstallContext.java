package org.goblinframework.api.system;

import org.goblinframework.api.event.GoblinEventListener;
import org.goblinframework.api.test.TestExecutionListener;
import org.jetbrains.annotations.NotNull;

public interface ModuleInstallContext extends ModuleContext {

  void subscribeEventListener(@NotNull GoblinEventListener listener);

  void registerTestExecutionListener(@NotNull TestExecutionListener listener);

  void registerManagementController(@NotNull Object controller);

}
