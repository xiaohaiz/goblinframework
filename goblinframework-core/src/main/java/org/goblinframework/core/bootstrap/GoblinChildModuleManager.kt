package org.goblinframework.core.bootstrap

import org.goblinframework.core.event.EventBus
import org.goblinframework.core.event.GoblinEventFuture
import org.springframework.util.LinkedMultiValueMap
import java.util.concurrent.atomic.AtomicInteger

class GoblinChildModuleManager {

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
    execute(ctx)
  }

  fun bootstrap(ctx: GoblinModuleBootstrapContext) {
    execute(ctx)
  }

  fun finalize(ctx: GoblinModuleFinalizeContext) {
    execute(ctx)
  }

  private fun execute(ctx: Any) {
    if (modules.isEmpty()) {
      return
    }
    for (i in 0..stage.get()) {
      val namesList = modules[i] ?: continue
      val futures = mutableListOf<GoblinEventFuture>()
      for (names in namesList) {
        val childModules = names.mapNotNull {
          GoblinChildModuleLoader.INSTANCE.getGoblinChildModule(it)
        }.toList()
        if (childModules.isEmpty()) {
          continue
        }
        val event = GoblinChildModuleEvent(ctx, childModules)
        futures.add(EventBus.publish(event))
      }
      futures.forEach {
        try {
          it.awaitUninterruptibly()
        } catch (ex: Exception) {
          throw GoblinModuleException(ex)
        }
      }
    }
  }
}