package org.goblinframework.core.bootstrap

import org.goblinframework.core.module.spi.RegisterManagementController
import org.goblinframework.core.util.ServiceInstaller

class GoblinModuleInstallContext private constructor() : GoblinModuleContext() {

  companion object {
    @JvmField val INSTANCE = GoblinModuleInstallContext()
  }

  fun registerManagementController(controller: Any?) {
    controller?.run {
      val c = this
      ServiceInstaller.installedFirst(RegisterManagementController::class.java)?.run {
        this.register(c)
      }
    }
  }

}