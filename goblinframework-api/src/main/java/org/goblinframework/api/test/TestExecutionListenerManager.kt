package org.goblinframework.api.test

import org.goblinframework.api.annotation.SPI

@SPI
interface TestExecutionListenerManager {

  fun register(listener: TestExecutionListener)

}
