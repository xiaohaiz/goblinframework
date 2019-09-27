package org.goblinframework.core.system

import org.goblinframework.api.core.Singleton

@Singleton
class ModuleInitializeContextImpl private constructor() : ModuleContextImpl(), ModuleInitializeContext {

  companion object {
    @JvmField val INSTANCE = ModuleInitializeContextImpl()
  }

}