package org.goblinframework.core.system

import org.goblinframework.api.annotation.Singleton
import org.goblinframework.api.spi.ExtModuleFinalizeContext

@Singleton
class ExtModuleFinalizeContextImpl private constructor() : ExtModuleContextImpl(), ExtModuleFinalizeContext {

  companion object {
    @JvmField val INSTANCE = ExtModuleFinalizeContextImpl()
  }
}