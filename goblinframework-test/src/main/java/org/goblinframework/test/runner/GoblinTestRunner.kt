package org.goblinframework.test.runner

import org.goblinframework.api.system.GoblinSystem
import org.goblinframework.test.listener.TestExecutionListenerManager
import org.springframework.context.ApplicationContext
import org.springframework.test.context.MergedContextConfiguration
import org.springframework.test.context.TestContextManager
import org.springframework.test.context.cache.DefaultCacheAwareContextLoaderDelegate
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.test.context.support.DefaultBootstrapContext
import org.springframework.test.context.support.DefaultTestContextBootstrapper
import org.springframework.util.ClassUtils
import kotlin.concurrent.thread

class GoblinTestRunner(clazz: Class<*>) : SpringJUnit4ClassRunner(clazz) {

  companion object {
    init {
      GoblinSystem.install()
      Runtime.getRuntime().addShutdownHook(thread(start = false, name = "GoblinTestRunnerShutdownHook") {
        TestExecutionListenerManager.INSTANCE.dispose()
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
        val classLoader = ClassUtils.getDefaultClassLoader()!!
        return try {
          classLoader.loadClass("org.goblinframework.core.container.StandaloneSpringContainer")
        } catch (ex: ClassNotFoundException) {
          null
        }?.run {
          val constructor = this.getConstructor(Array<String>::class.java)
          constructor.newInstance(mergedContextConfiguration.locations) as ApplicationContext
        } ?: super.loadContextInternal(mergedContextConfiguration)
      }
    }
    val bootstrapper = DefaultTestContextBootstrapper()
    bootstrapper.bootstrapContext = DefaultBootstrapContext(clazz, delegate)
    return TestContextManager(bootstrapper)
  }
}
