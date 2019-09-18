package org.goblinframework.test.runner

import org.springframework.context.ApplicationContext
import org.springframework.test.context.MergedContextConfiguration
import org.springframework.test.context.TestContext
import org.springframework.test.context.TestContextManager
import org.springframework.test.context.TestExecutionListener
import org.springframework.test.context.cache.DefaultCacheAwareContextLoaderDelegate
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.test.context.support.DefaultBootstrapContext
import org.springframework.test.context.support.DefaultTestContextBootstrapper
import org.springframework.util.ClassUtils
import java.util.*

class GoblinTestRunner(clazz: Class<*>) : SpringJUnit4ClassRunner(clazz) {

  companion object {
    init {
      val classLoader = ClassUtils.getDefaultClassLoader()!!
      val goblinBootstrapClass = try {
        classLoader.loadClass("org.goblinframework.core.bootstrap.GoblinSystem")
      } catch (ex: ClassNotFoundException) {
        null
      }
      goblinBootstrapClass?.run {
        val clazz = this
        clazz.getMethod("install").invoke(null)
        Runtime.getRuntime().addShutdownHook(object : Thread("GoblinTestRunnerShutdownHook") {
          override fun run() {
            clazz.getMethod("uninstall").invoke(null)
          }
        })
      }
    }
  }

  init {
    val classLoader = ClassUtils.getDefaultClassLoader()!!
    val listenerList = ServiceLoader.load(org.goblinframework.api.test.TestExecutionListener::class.java, classLoader)
        .sortedBy { it.order }
        .map {
          object : TestExecutionListener {
            override fun prepareTestInstance(testContext: TestContext) {
              it.prepareTestInstance(TestContextDelegate(testContext))
            }

            override fun beforeTestExecution(testContext: TestContext) {
              it.beforeTestExecution(TestContextDelegate(testContext))
            }

            override fun beforeTestClass(testContext: TestContext) {
              it.beforeTestClass(TestContextDelegate(testContext))
            }

            override fun beforeTestMethod(testContext: TestContext) {
              it.beforeTestMethod(TestContextDelegate(testContext))
            }

            override fun afterTestExecution(testContext: TestContext) {
              it.afterTestExecution(TestContextDelegate(testContext))
            }

            override fun afterTestClass(testContext: TestContext) {
              it.afterTestClass(TestContextDelegate(testContext))
            }

            override fun afterTestMethod(testContext: TestContext) {
              it.afterTestMethod(TestContextDelegate(testContext))
            }
          }
        }
        .toList()
    testContextManager.registerTestExecutionListeners(listenerList)
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
