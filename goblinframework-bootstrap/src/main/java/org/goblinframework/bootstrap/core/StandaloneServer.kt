package org.goblinframework.bootstrap.core

import org.goblinframework.core.bootstrap.GoblinBootstrap
import org.goblinframework.core.util.ThreadUtils
import org.springframework.context.ApplicationContext

abstract class StandaloneServer {

  fun bootstrap(args: Array<String>) {
    GoblinBootstrap.initialize()
    val ctx = SpringContainerLoader.load(this)

    if (useShutdownHook()) {
      Runtime.getRuntime().addShutdownHook(object : Thread("StandaloneServerShutdownHook") {
        override fun run() {
          shutdown()
        }
      })
    }

    if (runDaemonMode()) {
      ThreadUtils.joinCurrentThread()
    }
  }

  fun shutdown() {
    GoblinBootstrap.close()
  }

  protected fun useShutdownHook(): Boolean {
    return true
  }

  protected fun runDaemonMode(): Boolean {
    return true
  }

  protected abstract fun doService(ctx: ApplicationContext?)
}
