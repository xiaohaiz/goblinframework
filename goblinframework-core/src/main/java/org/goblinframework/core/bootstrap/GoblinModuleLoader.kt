package org.goblinframework.core.bootstrap

import org.goblinframework.core.util.ServiceInstaller

class GoblinModuleLoader private constructor() {

  companion object {
    @JvmField val INSTANCE = GoblinModuleLoader()
  }

  private val installedModules = mutableMapOf<String, GoblinModule>()

  init {
    ServiceInstaller.installedList(GoblinModule::class.java).forEach {
      val name = it.name()
      installedModules.put(name, it)?.run {
        throw GoblinModuleException("Duplicated module not allowed: $name")
      }
    }
  }

  fun getGoblinModule(name: String): GoblinModule? {
    return installedModules[name]
  }
}