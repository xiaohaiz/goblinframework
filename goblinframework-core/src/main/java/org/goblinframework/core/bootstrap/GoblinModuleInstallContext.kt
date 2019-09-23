package org.goblinframework.core.bootstrap

import org.goblinframework.api.service.ServiceInstaller
import org.goblinframework.api.test.ITestExecutionListenerManager
import org.goblinframework.api.test.TestExecutionListener
import org.goblinframework.core.cache.GoblinCacheBuilder
import org.goblinframework.core.cache.GoblinCacheSystem
import org.goblinframework.core.module.spi.RegisterGoblinCacheBuilder
import org.goblinframework.core.module.spi.RegisterInstructionTranslator
import org.goblinframework.core.module.spi.RegisterManagementController
import org.goblinframework.core.module.spi.RegisterMonitorPoint
import org.goblinframework.core.monitor.Instruction
import org.goblinframework.core.monitor.InstructionTranslator
import org.goblinframework.core.monitor.MonitorPoint

class GoblinModuleInstallContext private constructor() : GoblinModuleContext() {

  companion object {
    @JvmField val INSTANCE = GoblinModuleInstallContext()
  }

  private val registerManagementController: RegisterManagementController?
  private val registerMonitorPoint: RegisterMonitorPoint?
  private val registerInstructionTranslator: RegisterInstructionTranslator?
  private val registerGoblinCacheBuilder: RegisterGoblinCacheBuilder?

  init {
    registerManagementController = ServiceInstaller.firstOrNull(RegisterManagementController::class.java)
    registerMonitorPoint = ServiceInstaller.firstOrNull(RegisterMonitorPoint::class.java)
    registerInstructionTranslator = ServiceInstaller.firstOrNull(RegisterInstructionTranslator::class.java)
    registerGoblinCacheBuilder = ServiceInstaller.firstOrNull(RegisterGoblinCacheBuilder::class.java)
  }

  fun registerManagementController(controller: Any) {
    registerManagementController?.register(controller)
  }

  fun registerMonitorPoint(monitorPoint: MonitorPoint) {
    registerMonitorPoint?.register(monitorPoint)
  }

  fun <E : Instruction> registerInstructionTranslator(type: Class<E>, translator: InstructionTranslator<E>) {
    registerInstructionTranslator?.register(type, translator)
  }

  fun registerGoblinCacheBuilder(system: GoblinCacheSystem, builder: GoblinCacheBuilder) {
    registerGoblinCacheBuilder?.register(system, builder)
  }

  fun registerTestExecutionListener(listener: TestExecutionListener) {
    ITestExecutionListenerManager.instance()?.register(listener)
  }

}