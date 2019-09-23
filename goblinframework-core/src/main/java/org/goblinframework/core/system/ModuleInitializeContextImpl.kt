package org.goblinframework.core.system

import org.goblinframework.api.common.Singleton
import org.goblinframework.api.system.ModuleInitializeContext

@Singleton
class ModuleInitializeContextImpl private constructor() : ModuleContextImpl(), ModuleInitializeContext {

  companion object {
    @JvmField val INSTANCE = ModuleInitializeContextImpl()
  }

}