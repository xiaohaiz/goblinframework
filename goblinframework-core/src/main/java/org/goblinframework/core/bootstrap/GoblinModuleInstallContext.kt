package org.goblinframework.core.bootstrap

import org.goblinframework.core.module.spi.RegisterManagementController
import org.goblinframework.core.module.spi.RegisterMonitorPoint
import org.goblinframework.core.monitor.MonitorPoint
import org.goblinframework.core.util.ServiceInstaller

class GoblinModuleInstallContext private constructor() : GoblinModuleContext() {

  companion object {
    @JvmField val INSTANCE = GoblinModuleInstallContext()
  }

  private val registerManagementController: RegisterManagementController?
  private val registerMonitorPoint: RegisterMonitorPoint?

  init {
    registerManagementController = ServiceInstaller.installedFirst(RegisterManagementController::class.java)
    registerMonitorPoint = ServiceInstaller.installedFirst(RegisterMonitorPoint::class.java)
  }

  fun registerManagementController(controller: Any) {
    registerManagementController?.register(controller)
  }

  fun registerMonitorPoint(monitorPoint: MonitorPoint) {
    registerMonitorPoint?.register(monitorPoint)
  }
}