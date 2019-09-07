package org.goblinframework.core.module

interface GoblinChildModule {

  fun parent(): String

  fun name(): String

  fun managementEntrance(): String? {
    return null
  }

  fun initialize(ctx: InitializeContext) {}

  fun bootstrap(ctx: BootstrapContext) {}

  fun shutdown(ctx: ShutdownContext) {}

  fun finalize(ctx: FinalizeContext) {}

}