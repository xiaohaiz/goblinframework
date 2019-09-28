package org.goblinframework.core.system

import org.goblinframework.api.annotation.Singleton

@Singleton
class ModuleInitializeContextImpl private constructor() : ModuleContextImpl(), ModuleInitializeContext {

  companion object {
    @JvmField val INSTANCE = ModuleInitializeContextImpl()
  }

}