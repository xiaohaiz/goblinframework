package org.goblinframework.core.system

import org.goblinframework.api.core.Block0
import org.goblinframework.api.core.Singleton
import org.goblinframework.api.event.EventBus
import org.goblinframework.api.event.GoblinEventListener
import org.goblinframework.api.management.IManagementControllerManager
import org.goblinframework.api.system.IGoblinSystemManager
import org.goblinframework.api.test.ITestExecutionListenerManager
import org.goblinframework.api.test.TestExecutionListener
import org.goblinframework.core.config.ConfigListener
import org.goblinframework.core.config.ConfigManager
import org.goblinframework.core.config.ConfigParser
import org.goblinframework.core.container.SpringContainerBeanPostProcessor
import org.goblinframework.core.container.SpringContainerManager

@Singleton
class ModuleInstallContextImpl private constructor() : ModuleContextImpl(), ModuleInstallContext {

  companion object {
    @JvmField val INSTANCE = ModuleInstallContextImpl()
  }

  override fun registerPriorFinalizationTask(action: Block0) {
    IGoblinSystemManager.instance().registerPriorFinalizationTask(action)
  }

  override fun registerEventChannel(channel: String, ringBufferSize: Int, workerHandlers: Int) {
    EventBus.register(channel, ringBufferSize, workerHandlers)
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
    ConfigManager.INSTANCE.registerConfigParser(parser)
  }

  override fun registerConfigListener(listener: ConfigListener) {
    ConfigManager.INSTANCE.registerConfigListener(listener)
  }

  override fun registerContainerBeanPostProcessor(processor: SpringContainerBeanPostProcessor) {
    SpringContainerManager.INSTANCE.registerBeanPostProcessor(processor)
  }
}