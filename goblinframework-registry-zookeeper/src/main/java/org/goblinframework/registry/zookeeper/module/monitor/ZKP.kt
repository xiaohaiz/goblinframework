package org.goblinframework.registry.zookeeper.module.monitor

import org.goblinframework.core.monitor.Instruction
import org.goblinframework.core.monitor.InstructionTranslator
import org.goblinframework.core.monitor.AbstractInstruction

class ZKP : AbstractInstruction(Instruction.Id.ZKP, Instruction.Mode.SYN, true) {

  var name: String? = null
  var operation: String? = null
  var path: String? = null
  var createMode: String? = null

  override fun translator(): InstructionTranslator {
    return InstructionTranslator {
      if (!it) {
        return@InstructionTranslator asShortText()
      }
      path?.run {
        val p = this
        createMode?.run {
          String.format("%s %s %s(%s) %s",
              asLongText(), name, operation, createMode, p)
        } ?: kotlin.run {
          String.format("%s %s %s %s",
              asLongText(), name, operation, p)
        }
      } ?: kotlin.run {
        String.format("%s %s %s",
            asLongText(), name, operation)
      }
    }
  }
}
