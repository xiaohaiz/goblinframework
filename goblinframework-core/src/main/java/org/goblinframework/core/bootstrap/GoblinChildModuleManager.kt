package org.goblinframework.core.bootstrap

import org.springframework.util.LinkedMultiValueMap
import java.util.concurrent.atomic.AtomicInteger

class GoblinChildModuleManager(private val parent: String) {

  private val stage = AtomicInteger()
  private val modules = LinkedMultiValueMap<Int, List<String>>()

  fun next(): GoblinChildModuleManager {
    stage.incrementAndGet()
    return this
  }

  fun module(vararg names: String): GoblinChildModuleManager {
    if (names.isEmpty()) return this
    modules.add(stage.get(), names.toList())
    return this
  }

  fun initialize(ctx: GoblinModuleInitializeContext) {
    if (modules.isEmpty()) {
      return
    }
    for (i in 0..stage.get()) {
      val namesList = modules[i] ?: continue
      for (names in namesList) {
        val childModules = names.mapNotNull {
          GoblinChildModuleLoader.INSTANCE.getGoblinChildModule(parent, it)
        }.toList()
        if (childModules.isEmpty()) {
          continue
        }

      }
    }
  }
}