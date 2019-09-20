package org.goblinframework.monitor.module

import org.goblinframework.api.annotation.Install
import org.goblinframework.core.bootstrap.GoblinModule

@Install
class MonitorModule : GoblinModule {

  override fun name(): String {
    return "MONITOR"
  }
}