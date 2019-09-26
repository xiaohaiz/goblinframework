package org.goblinframework.monitor.point

import org.goblinframework.api.core.GoblinManagedBean
import org.goblinframework.api.core.GoblinManagedObject
import org.goblinframework.api.monitor.MonitorPoint

@GoblinManagedBean(type = "monitor", name = "MonitorPoint")
class ManagedMonitorPoint
internal constructor(private val delegator: MonitorPoint)
  : GoblinManagedObject(), MonitorPointMXBean, MonitorPoint by delegator