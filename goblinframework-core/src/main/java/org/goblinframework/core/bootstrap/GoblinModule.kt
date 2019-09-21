package org.goblinframework.core.bootstrap

interface GoblinModule {

  fun name(): String

  fun managementEntrance(): String? {
    return null
  }

  fun install(ctx: GoblinModuleInstallContext) {}

  fun bootstrap(ctx: GoblinModuleBootstrapContext) {}

  fun finalize(ctx: GoblinModuleFinalizeContext) {}

}