package org.goblinframework.test.runner

import org.goblinframework.core.bootstrap.GoblinBootstrap
import org.goblinframework.core.container.StandaloneSpringContainer
import org.junit.runners.model.InitializationError
import org.springframework.context.ApplicationContext
import org.springframework.test.context.MergedContextConfiguration
import org.springframework.test.context.TestContextManager
import org.springframework.test.context.cache.DefaultCacheAwareContextLoaderDelegate
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.test.context.support.DefaultBootstrapContext
import org.springframework.test.context.support.DefaultTestContextBootstrapper

class GoblinTestRunner
@Throws(InitializationError::class) constructor(clazz: Class<*>)
  : SpringJUnit4ClassRunner(clazz) {

  companion object {
    init {
      GoblinBootstrap.initialize()
      Runtime.getRuntime().addShutdownHook(object : Thread("GoblinTestRunnerShutdownHook") {
        override fun run() {
          GoblinBootstrap.close()
        }
      })
    }
  }

  override fun createTestContextManager(clazz: Class<*>): TestContextManager {
    val delegate = object : DefaultCacheAwareContextLoaderDelegate() {
      override fun loadContextInternal(mergedContextConfiguration: MergedContextConfiguration): ApplicationContext {
        return StandaloneSpringContainer(*mergedContextConfiguration.locations)
      }
    }
    val context = DefaultBootstrapContext(clazz, delegate)
    val bootstrapper = DefaultTestContextBootstrapper()
    bootstrapper.bootstrapContext = context
    return TestContextManager(bootstrapper)
  }
}
