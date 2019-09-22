package org.goblinframework.test.listener

import org.goblinframework.api.annotation.Install
import org.goblinframework.api.annotation.Singleton
import org.goblinframework.api.annotation.ThreadSafe
import org.goblinframework.api.test.ITestExecutionListenerManager
import org.goblinframework.api.test.TestExecutionListener

/**
 * Default [ITestExecutionListenerManager] SPI implementation.
 * Register [TestExecutionListener] instance(s) here during
 * module installation.
 */
@Singleton
@ThreadSafe
class TestExecutionListenerManager private constructor() : ITestExecutionListenerManager {

  companion object {
    @JvmField val INSTANCE = TestExecutionListenerManager()
  }

  override fun register(listener: TestExecutionListener) {

  }

  @Install
  class Installer : ITestExecutionListenerManager by INSTANCE
}