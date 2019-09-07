package org.goblinframework.core.module

import org.goblinframework.api.common.Ordered

interface GoblinExtensionModule : Ordered {

  fun name(): String

  fun managementEntrance(): String? {
    return null
  }

  override fun getOrder(): Int {
    return 0
  }

  fun initialize(ctx: GoblinModuleInitializeContext) {}

  fun bootstrap(ctx: GoblinModuleBootstrapContext) {}

  fun shutdown(ctx: GoblinModuleShutdownContext) {}

  fun finalize(ctx: GoblinModuleFinalizeContext) {}

}