package org.goblinframework.api.system;

import org.goblinframework.api.monitor.Instruction;
import org.goblinframework.api.test.TestExecutionListener;
import org.jetbrains.annotations.NotNull;

public interface ModuleInstallContext extends ModuleContext {

  void registerTestExecutionListener(@NotNull TestExecutionListener listener);

  void registerManagementController(@NotNull Object controller);

  void registerMonitorInstruction(@NotNull Instruction instruction);

}
