package org.goblinframework.registry.zookeeper.module.monitor

import org.goblinframework.api.monitor.Instruction
import org.goblinframework.api.monitor.InstructionTranslator
import org.goblinframework.core.monitor.AbstractInstruction

class ZKP : AbstractInstruction(Instruction.Id.ZKP, Instruction.Mode.SYN, true) {

  var name: String? = null
  var operation: String? = null
  var path: String? = null

  override fun translator(): InstructionTranslator {
    return InstructionTranslator {
      if (!it) {
        return@InstructionTranslator asShortText()
      }
      path?.run {
        String.format("%s %s %s %s",
            asLongText(), name, operation, this)
      } ?: kotlin.run {
        String.format("%s %s %s",
            asLongText(), name, operation)
      }
    }
  }
}
