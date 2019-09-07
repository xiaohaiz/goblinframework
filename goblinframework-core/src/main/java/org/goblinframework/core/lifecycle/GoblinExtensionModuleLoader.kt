package org.goblinframework.core.lifecycle

import org.goblinframework.core.util.GoblinServiceLoader

class GoblinExtensionModuleLoader {

  private val installedExtensionModules = mutableMapOf<String, GoblinExtensionModule>()

  init {
    GoblinServiceLoader.installedList(GoblinExtensionModule::class.java).forEach {
      val name = it.name()
      installedExtensionModules.put(name, it)?.run {
        throw GoblinModuleException("Duplicated extension module not allowed: $name")
      }
    }
  }

  fun getGoblinExtensionModule(name: String): GoblinExtensionModule? {
    return installedExtensionModules[name]
  }
}