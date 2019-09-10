package org.goblinframework.core.bootstrap

interface GoblinChildModule {

  fun name(): String

  fun managementEntrance(): String? {
    return null
  }

  fun initialize(ctx: GoblinModuleInitializeContext) {}

  fun bootstrap(ctx: GoblinModuleBootstrapContext) {}

  fun finalize(ctx: GoblinModuleFinalizeContext) {}

}