package org.goblinframework.test.listener

import org.goblinframework.api.common.Install
import org.goblinframework.api.common.Singleton
import org.goblinframework.api.common.ThreadSafe
import org.goblinframework.api.test.ITestExecutionListenerManager
import org.goblinframework.api.test.TestExecutionListener
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write

@Singleton
@ThreadSafe
class TestExecutionListenerManager private constructor() : ITestExecutionListenerManager {

  companion object {
    @JvmField val INSTANCE = TestExecutionListenerManager()
  }

  private val lock = ReentrantReadWriteLock()
  private val listeners = mutableListOf<TestExecutionListenerAdapter>()

  override fun register(listener: TestExecutionListener) {
    lock.write { listeners.add(TestExecutionListenerAdapter(listener)) }
  }

  fun asList(): List<TestExecutionListenerAdapter> {
    return lock.read { listeners.sortedBy { it.order }.toList() }
  }

  @Install
  class Installer : ITestExecutionListenerManager by INSTANCE
}