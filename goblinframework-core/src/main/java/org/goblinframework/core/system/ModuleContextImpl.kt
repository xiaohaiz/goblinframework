package org.goblinframework.core.system

import org.goblinframework.api.system.ModuleContext
import org.goblinframework.api.system.SubModules

abstract class ModuleContextImpl : ModuleContext {

  override fun createSubModules(): SubModules {
    return SubModulesImpl()
  }
}