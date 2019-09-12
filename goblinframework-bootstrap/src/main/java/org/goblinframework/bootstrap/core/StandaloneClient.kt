package org.goblinframework.bootstrap.core

import org.goblinframework.core.bootstrap.GoblinBootstrap
import org.goblinframework.core.container.SpringContainer
import org.slf4j.LoggerFactory
import kotlin.system.exitProcess

abstract class StandaloneClient {

  fun bootstrap(args: Array<String>) {
    GoblinBootstrap.initialize()
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

  fun shutdown() {
    doShutdown()
    GoblinBootstrap.close()
  }

  open fun doShutdown() {}

  open fun doExecute(container: SpringContainer?) {
  }
}