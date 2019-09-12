package org.goblinframework.bootstrap.core

import org.goblinframework.core.bootstrap.GoblinBootstrap
import org.goblinframework.core.container.SpringContainer
import org.goblinframework.core.util.ThreadUtils

abstract class StandaloneServer {

  fun bootstrap(args: Array<String>) {
    GoblinBootstrap.initialize()
    val container = SpringContainerLoader.load(this)

    if (useShutdownHook()) {
      Runtime.getRuntime().addShutdownHook(object : Thread("StandaloneServerShutdownHook") {
        override fun run() {
          shutdown()
        }
      })
    }

    doService(container)

    if (runDaemonMode()) {
      ThreadUtils.joinCurrentThread()
    }
  }

  fun shutdown() {
    doShutdown()
    GoblinBootstrap.close()
  }

  open fun useShutdownHook(): Boolean {
    return true
  }

  open fun runDaemonMode(): Boolean {
    return true
  }

  open fun doShutdown() {}

  open fun doService(container: SpringContainer?) {
  }
}
