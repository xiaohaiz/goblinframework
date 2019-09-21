package org.goblinframework.core.bootstrap

interface GoblinModule {

  fun name(): String

  fun managementEntrance(): String? {
    return null
  }

  fun install(ctx: GoblinModuleInstallContext) {}

  fun initialize(ctx: GoblinModuleInitializeContext) {}

  fun finalize(ctx: GoblinModuleFinalizeContext) {}

}