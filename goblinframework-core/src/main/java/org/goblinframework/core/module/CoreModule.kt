package org.goblinframework.core.module

import org.goblinframework.core.bootstrap.GoblinModule
import org.goblinframework.core.bootstrap.GoblinModuleFinalizeContext
import org.goblinframework.core.compression.CompressorManager
import org.goblinframework.core.serialization.SerializerManager

class CoreModule : GoblinModule {

  override fun name(): String {
    return "CORE"
  }

  override fun finalize(ctx: GoblinModuleFinalizeContext) {
    CompressorManager.INSTANCE.close()
    SerializerManager.INSTANCE.close()
  }
}