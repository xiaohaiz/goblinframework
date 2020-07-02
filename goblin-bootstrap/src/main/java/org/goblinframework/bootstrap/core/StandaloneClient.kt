package org.goblinframework.bootstrap.core

import org.goblinframework.core.container.SpringContainer
import org.goblinframework.core.system.GoblinSystem
import org.slf4j.LoggerFactory
import kotlin.system.exitProcess

abstract class StandaloneClient {

  fun bootstrap(args: Array<String>) {
    if (useShutdownHook()) {
      Runtime.getRuntime().addShutdownHook(object : Thread("StandaloneServerShutdownHook") {
        override fun run() {
          shutdown()
        }
      })
    }

    var success = true
    try {
      GoblinSystem.install()
      val container = SpringContainerLoader.load(this)
      doExecute(container)
    } catch (ex: Throwable) {
      LoggerFactory.getLogger(javaClass).error("Exception raised when executing StandaloneClient", ex)
      success = false
    } finally {
      shutdown()
      val status = if (success) 0 else -1
      exitProcess(status)
    }
  }

  open fun useShutdownHook(): Boolean {
    return true
  }

  fun shutdown() {
    doShutdown()
    GoblinSystem.uninstall()
  }

  open fun doShutdown() {}

  open fun doExecute(container: SpringContainer?) {
  }
}