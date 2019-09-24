package org.goblinframework.core.system

import org.goblinframework.api.common.Singleton
import org.goblinframework.api.config.ConfigListener
import org.goblinframework.api.config.ConfigParser
import org.goblinframework.api.config.IConfigManager
import org.goblinframework.api.event.EventBus
import org.goblinframework.api.event.GoblinEventListener
import org.goblinframework.api.management.IManagementControllerManager
import org.goblinframework.api.system.ModuleInstallContext
import org.goblinframework.api.test.ITestExecutionListenerManager
import org.goblinframework.api.test.TestExecutionListener

@Singleton
class ModuleInstallContextImpl private constructor() : ModuleContextImpl(), ModuleInstallContext {

  companion object {
    @JvmField val INSTANCE = ModuleInstallContextImpl()
  }

  override fun subscribeEventListener(listener: GoblinEventListener) {
    EventBus.subscribe(listener)
  }

  override fun registerTestExecutionListener(listener: TestExecutionListener) {
    ITestExecutionListenerManager.instance()?.register(listener)
  }

  override fun registerManagementController(controller: Any) {
    IManagementControllerManager.instance()?.register(controller)
  }

  override fun registerConfigParser(parser: ConfigParser) {
    IConfigManager.instance().registerConfigParser(parser)
  }

  override fun registerConfigListener(listener: ConfigListener) {
    IConfigManager.instance().registerConfigListener(listener)
  }
}