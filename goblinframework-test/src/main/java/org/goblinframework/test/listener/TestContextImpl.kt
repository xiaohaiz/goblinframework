package org.goblinframework.test.listener

import org.goblinframework.api.test.TestContext
import java.lang.reflect.Method

class TestContextImpl
internal constructor(private val delegator: org.springframework.test.context.TestContext)
  : TestContext {

  override fun getApplicationContext(): Any {
    return delegator.applicationContext
  }

  override fun getTestClass(): Class<*> {
    return delegator.testClass
  }

  override fun getTestInstance(): Any {
    return delegator.testInstance
  }

  override fun getTestMethod(): Method {
    return delegator.testMethod
  }

  override fun getTestException(): Throwable? {
    return delegator.testException
  }
}