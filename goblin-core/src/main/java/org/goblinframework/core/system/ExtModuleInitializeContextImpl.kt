package org.goblinframework.core.system

import org.goblinframework.api.annotation.Singleton
import org.goblinframework.api.spi.ExtModuleInitializeContext

@Singleton
class ExtModuleInitializeContextImpl private constructor() : ExtModuleContextImpl(), ExtModuleInitializeContext {

  companion object {
    @JvmField val INSTANCE = ExtModuleInitializeContextImpl()
  }

}