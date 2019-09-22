package org.goblinframework.monitor.module

import org.goblinframework.api.annotation.Install
import org.goblinframework.core.bootstrap.GoblinModule
import org.goblinframework.core.bootstrap.GoblinModuleFinalizeContext
import org.goblinframework.core.bootstrap.GoblinModuleInstallContext
import org.goblinframework.monitor.instruction.InstructionTranslatorManager
import org.goblinframework.monitor.message.TimedTouchableMessageBufferManager
import org.goblinframework.monitor.module.monitor.instruction.DOT
import org.goblinframework.monitor.module.monitor.instruction.DOTTranslator
import org.goblinframework.monitor.module.test.UnitTestFlightRecorder
import org.goblinframework.monitor.point.MonitorPointManager

@Install
class MonitorModule : GoblinModule {

  override fun name(): String {
    return "MONITOR"
  }

  override fun install(ctx: GoblinModuleInstallContext) {
    ctx.registerInstructionTranslator(DOT::class.java, DOTTranslator.INSTANCE)
    ctx.registerTestExecutionListener(UnitTestFlightRecorder.INSTANCE)
  }

  override fun finalize(ctx: GoblinModuleFinalizeContext) {
    MonitorPointManager.INSTANCE.dispose()
    TimedTouchableMessageBufferManager.INSTANCE.dispose()
    InstructionTranslatorManager.INSTANCE.dispose()
  }
}