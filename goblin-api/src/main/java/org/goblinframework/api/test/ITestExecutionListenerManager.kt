package org.goblinframework.api.test

import org.goblinframework.api.annotation.Internal

@Internal(uniqueInstance = true)
interface ITestExecutionListenerManager {

  fun register(listener: TestExecutionListener)

}
