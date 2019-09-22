package org.goblinframework.monitor.point

import org.goblinframework.api.service.GoblinManagedBean
import org.goblinframework.core.mbean.GoblinManagedObject
import org.goblinframework.core.monitor.MonitorPoint

@GoblinManagedBean(type = "monitor", name = "MonitorPoint")
class ManagedMonitorPoint
internal constructor(private val delegator: MonitorPoint)
  : GoblinManagedObject(), MonitorPointMXBean, MonitorPoint by delegator