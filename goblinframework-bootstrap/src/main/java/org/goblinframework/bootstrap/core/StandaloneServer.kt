package org.goblinframework.bootstrap.core

import org.goblinframework.core.container.SpringContainer
import org.goblinframework.core.system.GoblinSystem
import org.goblinframework.core.util.ThreadUtils
import org.slf4j.LoggerFactory
import kotlin.system.exitProcess

abstract class StandaloneServer {

  fun bootstrap(args: Array<String>) {
    if (useShutdownHook()) {
      Runtime.getRuntime().addShutdownHook(object : Thread("StandaloneServerShutdownHook") {
        override fun run() {
          shutdown()
        }
      })
    }

    try {
      GoblinSystem.install()
      val container = SpringContainerLoader.load(this)
      doService(container)
    } catch (ex: Throwable) {
      LoggerFactory.getLogger(javaClass).error("Exception raised when booting StandaloneServer", ex)
      exitProcess(-1)
    }

    if (runDaemonMode()) {
      LoggerFactory.getLogger(javaClass).info("WELCOME")
      ThreadUtils.joinCurrentThread()
    }
  }

  fun shutdown() {
    doShutdown()
    GoblinSystem.uninstall()
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
