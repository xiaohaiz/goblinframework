package org.goblinframework.test.listener

import org.goblinframework.api.common.Ordered
import org.goblinframework.api.test.TestExecutionListener
import org.springframework.test.context.TestContext

class TestExecutionListenerAdapter
internal constructor(private val adapter: TestExecutionListener)
  : org.springframework.test.context.TestExecutionListener, Ordered {

  override fun getOrder(): Int {
    return adapter.order
  }

  override fun prepareTestInstance(testContext: TestContext) {
    adapter.prepareTestInstance(TestContextDelegator(testContext))
  }

  override fun beforeTestExecution(testContext: TestContext) {
    adapter.beforeTestExecution(TestContextDelegator(testContext))
  }

  override fun beforeTestClass(testContext: TestContext) {
    adapter.beforeTestClass(TestContextDelegator(testContext))
  }

  override fun beforeTestMethod(testContext: TestContext) {
    adapter.beforeTestMethod(TestContextDelegator(testContext))
  }

  override fun afterTestExecution(testContext: TestContext) {
    adapter.afterTestExecution(TestContextDelegator(testContext))
  }

  override fun afterTestClass(testContext: TestContext) {
    adapter.afterTestClass(TestContextDelegator(testContext))
  }

  override fun afterTestMethod(testContext: TestContext) {
    adapter.afterTestMethod(TestContextDelegator(testContext))
  }
}