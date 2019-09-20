package org.goblinframework.monitor.module.test

import org.goblinframework.api.annotation.Install
import org.goblinframework.api.common.Ordered
import org.goblinframework.api.test.TestContext
import org.goblinframework.api.test.TestExecutionListener

@Install
class UnitTestFlightRecorder : TestExecutionListener, Ordered {

  override fun getOrder(): Int {
    return Ordered.HIGHEST_PRECEDENCE
  }

  override fun beforeTestMethod(testContext: TestContext?) {
  }

  override fun afterTestMethod(testContext: TestContext?) {
  }
}