package org.goblinframework.core.system

import org.goblinframework.api.annotation.Singleton
import org.goblinframework.api.management.IManagementControllerManager
import org.goblinframework.api.monitor.IInstructionManager
import org.goblinframework.api.monitor.Instruction
import org.goblinframework.api.system.ModuleInstallContext
import org.goblinframework.api.test.ITestExecutionListenerManager
import org.goblinframework.api.test.TestExecutionListener

@Singleton
class ModuleInstallContextImpl private constructor() : ModuleContextImpl(), ModuleInstallContext {

  companion object {
    @JvmField val INSTANCE = ModuleInstallContextImpl()
  }

  override fun registerTestExecutionListener(listener: TestExecutionListener) {
    ITestExecutionListenerManager.instance()?.register(listener)
  }

  override fun registerManagementController(controller: Any) {
    IManagementControllerManager.instance()?.register(controller)
  }

  override fun registerMonitorInstruction(instruction: Instruction) {
    IInstructionManager.instance()?.register(instruction)
  }
}