package org.goblinframework.test.listener

import org.goblinframework.api.annotation.Install
import org.goblinframework.api.annotation.Singleton
import org.goblinframework.api.annotation.ThreadSafe
import org.goblinframework.api.test.ITestExecutionListenerManager
import org.goblinframework.api.test.TestExecutionListener
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write

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

  private val lock = ReentrantReadWriteLock()
  private val listeners = mutableListOf<TestExecutionListener>()

  override fun register(listener: TestExecutionListener) {
    lock.write { listeners.add(listener) }
  }

  fun asList(): List<TestExecutionListener> {
    return lock.read { listeners.sortedBy { it.order }.toList() }
  }

  @Install
  class Installer : ITestExecutionListenerManager by INSTANCE
}