package org.goblinframework.core.module.spi

import java.util.*

interface RandomProvider {

  fun getRandom(): Random

}