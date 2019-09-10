package org.goblinframework.test.runner

import org.goblinframework.core.bootstrap.GoblinBootstrap
import org.junit.runners.model.InitializationError
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

class GoblinTestRunner @Throws(InitializationError::class)
constructor(clazz: Class<*>) : SpringJUnit4ClassRunner(clazz) {
  companion object {
    init {
      GoblinBootstrap.initialize()
      Runtime.getRuntime().addShutdownHook(object : Thread("GoblinTestRunnerShutdownHook") {
        override fun run() {
          GoblinBootstrap.doShutdown()
          GoblinBootstrap.close()
        }
      })
    }
  }
}
