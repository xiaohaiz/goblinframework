package org.goblinframework.core.bootstrap

import org.goblinframework.api.common.Ordered

interface GoblinExtensionModule : Ordered {

  fun name(): String

  fun managementEntrance(): String? {
    return null
  }

  override fun getOrder(): Int {
    return 0
  }

  fun install(ctx: GoblinModuleInstallContext) {}

  fun bootstrap(ctx: GoblinModuleBootstrapContext) {}

  fun finalize(ctx: GoblinModuleFinalizeContext) {}

}