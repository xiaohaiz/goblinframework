package org.goblinframework.test.runner

import org.springframework.context.ApplicationContext
import org.springframework.test.context.MergedContextConfiguration
import org.springframework.test.context.TestContextManager
import org.springframework.test.context.cache.DefaultCacheAwareContextLoaderDelegate
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.test.context.support.DefaultBootstrapContext
import org.springframework.test.context.support.DefaultTestContextBootstrapper
import org.springframework.util.ClassUtils

class GoblinTestRunner(clazz: Class<*>) : SpringJUnit4ClassRunner(clazz) {

  companion object {
    init {
      val classLoader = ClassUtils.getDefaultClassLoader()!!
      val goblinBootstrapClass = try {
        classLoader.loadClass("org.goblinframework.core.bootstrap.GoblinBootstrap")
      } catch (ex: ClassNotFoundException) {
        null
      }
      goblinBootstrapClass?.run {
        val clazz = this
        clazz.getMethod("initialize").invoke(null)
        Runtime.getRuntime().addShutdownHook(object : Thread("GoblinTestRunnerShutdownHook") {
          override fun run() {
            clazz.getMethod("close").invoke(null)
          }
        })
      }
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
    val context = DefaultBootstrapContext(clazz, delegate)
    val bootstrapper = DefaultTestContextBootstrapper()
    bootstrapper.bootstrapContext = context
    return TestContextManager(bootstrapper)
  }
}
