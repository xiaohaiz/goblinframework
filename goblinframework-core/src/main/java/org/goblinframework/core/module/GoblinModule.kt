package org.goblinframework.core.module

interface GoblinModule {

  fun name(): String

  fun managementEntrance(): String? {
    return null
  }

  fun initialize(ctx: GoblinModuleInitializeContext) {}

  fun bootstrap(ctx: GoblinModuleBootstrapContext) {}

  fun shutdown(ctx: GoblinModuleShutdownContext) {}

  fun finalize(ctx: GoblinModuleFinalizeContext) {}

}