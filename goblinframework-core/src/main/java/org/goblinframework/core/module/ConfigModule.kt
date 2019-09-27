package org.goblinframework.core.module

import org.goblinframework.api.core.Install
import org.goblinframework.core.config.ConfigManager
import org.goblinframework.core.module.management.ConfigManagement
import org.goblinframework.core.system.*

@Install
class ConfigModule : IModule {

  override fun id(): GoblinModule {
    return GoblinModule.CONFIG
  }

  override fun managementEntrance(): String? {
    return "/goblin/config/index.do"
  }

  override fun install(ctx: ModuleInstallContext) {
    ConfigManager.INSTANCE.install()
    ctx.registerManagementController(ConfigManagement.INSTANCE)
  }

  override fun initialize(ctx: ModuleInitializeContext) {
    ConfigManager.INSTANCE.initialize()
  }

  override fun finalize(ctx: ModuleFinalizeContext) {
    ConfigManager.INSTANCE.dispose()
  }
}