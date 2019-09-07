package org.goblinframework.core.bootstrap

import org.goblinframework.core.util.GoblinServiceLoader

class GoblinExtensionModuleLoader private constructor() {

  companion object {
    @JvmField val INSTANCE = GoblinExtensionModuleLoader()
  }

  private val installedExtensionModules = mutableMapOf<String, GoblinExtensionModule>()

  init {
    GoblinServiceLoader.installedList(GoblinExtensionModule::class.java).forEach {
      val name = it.name()
      installedExtensionModules.put(name, it)?.run {
        throw GoblinModuleException("Duplicated extension module not allowed: $name")
      }
    }
  }

  fun getGoblinExtensionModules(): List<GoblinExtensionModule> {
    return installedExtensionModules.values.sortedBy { it.order }.toList()
  }
}