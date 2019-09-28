package org.goblinframework.monitor.point

import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.core.service.GoblinManagedObject
import org.goblinframework.core.monitor.MonitorPoint

@GoblinManagedBean(type = "monitor", name = "MonitorPoint")
class ManagedMonitorPoint
internal constructor(private val delegator: MonitorPoint)
  : GoblinManagedObject(), MonitorPointMXBean, MonitorPoint by delegator