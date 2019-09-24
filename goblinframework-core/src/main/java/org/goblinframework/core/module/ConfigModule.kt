package org.goblinframework.core.module

import org.goblinframework.api.common.Install
import org.goblinframework.api.system.*
import org.goblinframework.core.config.ConfigLoader
import org.goblinframework.core.config.ConfigParserManager

@Install
class ConfigModule : IModule {

  override fun id(): GoblinModule {
    return GoblinModule.CONFIG
  }

  override fun install(ctx: ModuleInstallContext) {
    ConfigLoader.INSTANCE.initialize()
  }

  override fun initialize(ctx: ModuleInitializeContext) {
    ConfigParserManager.INSTANCE.asList().forEach { it.initialize() }
  }

  override fun finalize(ctx: ModuleFinalizeContext) {
    ConfigLoader.INSTANCE.dispose()
  }
}