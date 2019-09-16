package org.goblinframework.core.module

import org.goblinframework.core.bootstrap.GoblinModule
import org.goblinframework.core.bootstrap.GoblinModuleFinalizeContext
import org.goblinframework.core.bootstrap.GoblinModuleInitializeContext
import org.goblinframework.core.compression.CompressorManager
import org.goblinframework.core.config.ConfigLoader
import org.goblinframework.core.module.management.CoreManagement
import org.goblinframework.core.serialization.SerializerManager

class CoreModule : GoblinModule {

  override fun name(): String {
    return "CORE"
  }

  override fun managementEntrance(): String? {
    return "/goblin/core/index.do"
  }

  override fun initialize(ctx: GoblinModuleInitializeContext) {
    ctx.registerManagementController(CoreManagement.INSTANCE)
  }

  override fun finalize(ctx: GoblinModuleFinalizeContext) {
    CompressorManager.INSTANCE.close()
    SerializerManager.INSTANCE.close()
    ConfigLoader.INSTANCE.close()
  }
}