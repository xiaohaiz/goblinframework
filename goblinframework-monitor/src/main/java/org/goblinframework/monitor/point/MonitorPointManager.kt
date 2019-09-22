package org.goblinframework.monitor.point

import org.goblinframework.api.annotation.Install
import org.goblinframework.api.annotation.Singleton
import org.goblinframework.api.annotation.ThreadSafe
import org.goblinframework.api.service.GoblinManagedBean
import org.goblinframework.core.mbean.GoblinManagedObject
import org.goblinframework.core.module.spi.RegisterMonitorPoint
import org.goblinframework.core.monitor.MonitorPoint
import org.goblinframework.core.util.ServiceInstaller
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.write

@Singleton
@ThreadSafe
@GoblinManagedBean(type = "monitor")
class MonitorPointManager private constructor()
  : GoblinManagedObject(), MonitorPointManagerMXBean, RegisterMonitorPoint {

  companion object {
    @JvmField val INSTANCE = MonitorPointManager()
  }

  private val lock = ReentrantReadWriteLock()
  private val points: MutableList<ManagedMonitorPoint>

  init {
    points = ServiceInstaller.installedList(MonitorPoint::class.java)
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
  class Installer : RegisterMonitorPoint by INSTANCE
}
