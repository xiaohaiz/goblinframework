package org.goblinframework.core.bootstrap

abstract class GoblinModuleContext {

  fun createChildModuleManager(parent: String): GoblinChildModuleManager {
    return GoblinChildModuleManager(parent)
  }

}
