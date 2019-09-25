package org.goblinframework.core.module

import org.goblinframework.api.common.Install
import org.goblinframework.api.system.*
import org.goblinframework.core.config.ConfigManager
import org.goblinframework.core.module.management.ConfigManagement

@Install
class ConfigModule : IModule {

  override fun id(): GoblinModule {
    return GoblinModule.CONFIG
  }

  override fun managementEntrance(): String? {
    return "/goblin/config/index.do"
  }

  override fun install(ctx: ModuleInstallContext) {
    ConfigManager.INSTANCE.initialize()
    ctx.registerManagementController(ConfigManagement.INSTANCE)
  }

  override fun initialize(ctx: ModuleInitializeContext) {
    ConfigManager.INSTANCE.parseConfigs()
  }

  override fun finalize(ctx: ModuleFinalizeContext) {
    ConfigManager.INSTANCE.dispose()
  }
}