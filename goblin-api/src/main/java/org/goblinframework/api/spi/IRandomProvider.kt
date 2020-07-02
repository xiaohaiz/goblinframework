package org.goblinframework.api.spi

import org.goblinframework.api.annotation.External
import java.util.*

@External
interface IRandomProvider {

  fun getRandom(): Random

}
