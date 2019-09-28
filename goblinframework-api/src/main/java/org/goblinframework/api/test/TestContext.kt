package org.goblinframework.api.test

import java.lang.reflect.Method

interface TestContext {

  fun getApplicationContext(): Any

  fun getTestClass(): Class<*>

  fun getTestInstance(): Any

  fun getTestMethod(): Method

  fun getTestException(): Throwable?

}
