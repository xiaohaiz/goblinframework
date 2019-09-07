package org.goblinframework.core.bootstrap

import java.util.concurrent.atomic.AtomicBoolean

class GoblinModuleManager private constructor() {

  companion object {
    @JvmField val INSTANCE = GoblinModuleManager()
  }

  private val initialize = AtomicBoolean()
  private val bootstrap = AtomicBoolean()
  private val shutdown = AtomicBoolean()
  private val finalize = AtomicBoolean()

  fun initialize(): GoblinModuleManager {
    if (!initialize.compareAndSet(false, true)) {
      return this
    }
    val ctx = GoblinModuleInitializeContext()

    return this
  }

}