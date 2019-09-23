package org.goblinframework.bootstrap.core

import org.goblinframework.api.system.GoblinSystem
import org.goblinframework.core.container.SpringContainer
import org.slf4j.LoggerFactory
import kotlin.system.exitProcess

abstract class StandaloneClient {

  fun bootstrap(args: Array<String>) {
    GoblinSystem.install()
    if (useShutdownHook()) {
      Runtime.getRuntime().addShutdownHook(object : Thread("StandaloneServerShutdownHook") {
        override fun run() {
          shutdown()
        }
      })
    }
    val container = SpringContainerLoader.load(this)
    var success = true
    try {
      doExecute(container)
    } catch (ex: Exception) {
      LoggerFactory.getLogger(javaClass).error("Exception raised when executing", ex)
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