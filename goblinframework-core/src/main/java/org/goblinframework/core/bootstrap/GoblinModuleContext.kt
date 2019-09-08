package org.goblinframework.core.bootstrap

abstract class GoblinModuleContext {

  fun createChildModuleManager(): GoblinChildModuleManager {
    return GoblinChildModuleManager()
  }

}
