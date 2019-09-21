package org.goblinframework.core.module

import org.goblinframework.core.bootstrap.GoblinModule
import org.goblinframework.core.bootstrap.GoblinModuleFinalizeContext
import org.goblinframework.core.bootstrap.GoblinModuleInstallContext
import org.goblinframework.core.compression.CompressorManager
import org.goblinframework.core.module.management.CoreManagement
import org.goblinframework.core.serialization.SerializerManager

class CoreModule : GoblinModule {

  override fun name(): String {
    return "CORE"
  }

  override fun managementEntrance(): String? {
    return "/goblin/core/index.do"
  }

  override fun install(ctx: GoblinModuleInstallContext) {
    ctx.registerManagementController(CoreManagement.INSTANCE)
  }

  override fun finalize(ctx: GoblinModuleFinalizeContext) {
    CompressorManager.INSTANCE.dispose()
    SerializerManager.INSTANCE.dispose()
  }
}