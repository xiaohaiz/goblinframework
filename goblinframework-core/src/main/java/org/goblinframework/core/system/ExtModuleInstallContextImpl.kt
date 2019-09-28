package org.goblinframework.core.system

import org.goblinframework.api.annotation.Singleton
import org.goblinframework.api.function.Block0
import org.goblinframework.api.spi.ExtModuleInstallContext
import org.goblinframework.api.test.ITestExecutionListenerManager
import org.goblinframework.api.test.TestExecutionListener
import org.goblinframework.core.management.IManagementControllerManager
import org.goblinframework.core.service.ServiceInstaller

@Singleton
class ExtModuleInstallContextImpl private constructor() : ExtModuleContextImpl(), ExtModuleInstallContext {

  companion object {
    @JvmField val INSTANCE = ExtModuleInstallContextImpl()
  }

  override fun registerPriorFinalizationTask(action: Block0) {
    val gsm = GoblinSystemManager.INSTANCE
    gsm.registerPriorFinalizationTask(action)
  }

  override fun registerTestExecutionListener(listener: TestExecutionListener) {
    ServiceInstaller.firstOrNull(ITestExecutionListenerManager::class.java)?.register(listener)
  }

  override fun registerManagementController(controller: Any) {
    ServiceInstaller.firstOrNull(IManagementControllerManager::class.java)?.register(controller)
  }

}