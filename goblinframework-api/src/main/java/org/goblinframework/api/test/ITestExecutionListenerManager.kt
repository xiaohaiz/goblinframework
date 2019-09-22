package org.goblinframework.api.test

import org.goblinframework.api.annotation.SPI

@SPI
interface ITestExecutionListenerManager {

  fun register(listener: TestExecutionListener)

}
