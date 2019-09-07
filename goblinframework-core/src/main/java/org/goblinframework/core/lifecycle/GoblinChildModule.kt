package org.goblinframework.core.lifecycle

interface GoblinChildModule {

  fun parent(): String

  fun name(): String

  fun managementEntrance(): String? {
    return null
  }

  fun initialize(ctx: GoblinModuleInitializeContext) {}

  fun bootstrap(ctx: GoblinModuleBootstrapContext) {}

  fun shutdown(ctx: GoblinModuleShutdownContext) {}

  fun finalize(ctx: GoblinModuleFinalizeContext) {}

}