package org.goblinframework.core.bootstrap

import org.goblinframework.core.util.GoblinServiceLoader

class GoblinChildModuleLoader private constructor() {

  companion object {
    @JvmField val INSTANCE = GoblinChildModuleLoader()
  }

  private val installedChildModules = mutableMapOf<String, GoblinChildModule>()

  init {
    GoblinServiceLoader.installedList(GoblinChildModule::class.java).forEach {
      val name = it.name()
      installedChildModules.put(name, it)?.run {
        throw GoblinModuleException("Duplicated child module not allowed: $name")
      }
    }
  }

  fun getGoblinChildModule(name: String): GoblinChildModule? {
    return installedChildModules[name]
  }
}