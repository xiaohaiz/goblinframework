package org.goblinframework.monitor.point

import org.goblinframework.api.core.*
import org.goblinframework.api.monitor.IMonitorPointManager
import org.goblinframework.api.monitor.MonitorPoint
import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.core.service.GoblinManagedObject
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.write

@Singleton
@ThreadSafe
@GoblinManagedBean(type = "monitor")
class MonitorPointManager private constructor()
  : GoblinManagedObject(), MonitorPointManagerMXBean, IMonitorPointManager {

  companion object {
    @JvmField val INSTANCE = MonitorPointManager()
  }

  private val lock = ReentrantReadWriteLock()
  private val points: MutableList<ManagedMonitorPoint>

  init {
    points = ServiceInstaller.asList(MonitorPoint::class.java)
        .map { ManagedMonitorPoint(it) }.toMutableList()
  }

  override fun register(monitorPoint: MonitorPoint) {
    lock.write { points.add(ManagedMonitorPoint(monitorPoint)) }
  }

  override fun disposeBean() {
    lock.write {
      points.forEach { it.dispose() }
      points.clear()
    }
  }

  @Install
  class Installer : IMonitorPointManager by INSTANCE
}
