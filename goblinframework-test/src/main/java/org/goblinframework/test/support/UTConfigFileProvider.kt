package org.goblinframework.test.support

import org.goblinframework.api.annotation.Install
import org.goblinframework.api.common.Ordered
import org.goblinframework.api.spi.ConfigFileProvider

@Install
class UTConfigFileProvider : ConfigFileProvider, Ordered {

  override fun getOrder(): Int {
    return Ordered.HIGHEST_PRECEDENCE
  }

  override fun configFile(): String {
    return "goblin.ut.ini"
  }
}