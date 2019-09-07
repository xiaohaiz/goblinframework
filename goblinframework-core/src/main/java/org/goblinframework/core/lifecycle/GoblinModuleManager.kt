package org.goblinframework.core.lifecycle

import org.goblinframework.core.util.GoblinServiceLoader
import java.util.concurrent.atomic.AtomicBoolean

class GoblinModuleManager private constructor() {

  companion object {
    @JvmField val INSTANCE = GoblinModuleManager()
  }

  private val installedModules = mutableMapOf<String, GoblinModule>()
  private val installedChildModules = mutableMapOf<Pair<String, String>, GoblinChildModule>()
  private val installedExtensionModules = mutableMapOf<String, GoblinExtensionModule>()

  private val initialize = AtomicBoolean()
  private val bootstrap = AtomicBoolean()
  private val shutdown = AtomicBoolean()
  private val finalize = AtomicBoolean()

  init {
    GoblinServiceLoader.installedList(GoblinModule::class.java).forEach {
      val name = it.name()
      installedModules.put(name, it)?.run {
        throw UnsupportedOperationException("Duplicated module not allowed: $name")
      }
    }
  }

  fun initialize(): GoblinModuleManager {
    if (!initialize.compareAndSet(false, true)) {
      return this
    }
    val ctx = GoblinModuleInitializeContext()

    return this
  }

}