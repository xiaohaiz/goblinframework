package org.goblinframework.core.lifecycle

import org.goblinframework.core.util.GoblinServiceLoader

class GoblinChildModuleLoader {

  private val installedChildModules = mutableMapOf<Pair<String, String>, GoblinChildModule>()

  init {
    GoblinServiceLoader.installedList(GoblinChildModule::class.java).forEach {
      val parent = it.parent()
      val name = it.name()
      installedChildModules.put(Pair(parent, name), it)?.run {
        throw GoblinModuleException("Duplicated child module not allowed: $parent($name)")
      }
    }
  }

  fun getGoblinChildModule(parent: String, name: String): GoblinChildModule? {
    return installedChildModules[Pair(parent, name)]
  }
}