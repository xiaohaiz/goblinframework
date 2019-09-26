package org.goblinframework.test.runner

import org.goblinframework.api.core.ApplicationContextProvider
import org.goblinframework.api.core.ISpringContainerManager
import org.goblinframework.api.system.GoblinSystem
import org.goblinframework.test.listener.TestExecutionListenerManager
import org.springframework.context.ApplicationContext
import org.springframework.test.context.MergedContextConfiguration
import org.springframework.test.context.TestContextManager
import org.springframework.test.context.cache.DefaultCacheAwareContextLoaderDelegate
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.test.context.support.DefaultBootstrapContext
import org.springframework.test.context.support.DefaultTestContextBootstrapper
import kotlin.concurrent.thread

class GoblinTestRunner(clazz: Class<*>) : SpringJUnit4ClassRunner(clazz) {

  companion object {
    init {
      GoblinSystem.install()
      Runtime.getRuntime().addShutdownHook(thread(start = false, name = "GoblinTestRunnerShutdownHook") {
        GoblinSystem.uninstall()
      })
    }
  }

  init {
    val listeners = TestExecutionListenerManager.INSTANCE.asList()
    if (listeners.isNotEmpty()) {
      testContextManager.registerTestExecutionListeners(listeners)
    }
  }

  override fun createTestContextManager(clazz: Class<*>): TestContextManager {
    val delegate = object : DefaultCacheAwareContextLoaderDelegate() {
      override fun loadContextInternal(mergedContextConfiguration: MergedContextConfiguration): ApplicationContext {
        val container = ISpringContainerManager.instance()
            .createStandaloneContainer(*mergedContextConfiguration.locations)
        return (container as ApplicationContextProvider).applicationContext() as ApplicationContext
      }
    }
    val bootstrapper = DefaultTestContextBootstrapper()
    bootstrapper.bootstrapContext = DefaultBootstrapContext(clazz, delegate)
    return TestContextManager(bootstrapper)
  }
}
