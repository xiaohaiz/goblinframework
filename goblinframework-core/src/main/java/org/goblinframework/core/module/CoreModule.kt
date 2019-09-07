package org.goblinframework.core.module

import org.goblinframework.core.bootstrap.GoblinModule
import org.goblinframework.core.bootstrap.GoblinModuleFinalizeContext
import org.goblinframework.core.compression.CompressorManager

class CoreModule : GoblinModule {

  override fun name(): String {
    return "CORE"
  }

  override fun finalize(ctx: GoblinModuleFinalizeContext) {
    CompressorManager.INSTANCE.close()
  }
}