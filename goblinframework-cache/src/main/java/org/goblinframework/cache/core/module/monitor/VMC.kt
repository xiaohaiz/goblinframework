package org.goblinframework.cache.core.module.monitor

import org.goblinframework.api.monitor.Instruction
import org.goblinframework.api.monitor.InstructionTranslator
import org.goblinframework.core.monitor.InstructionImpl
import org.goblinframework.core.util.StringUtils

class VMC : InstructionImpl(Instruction.Id.VMC, Instruction.Mode.SYN, true) {

  var operation: String? = null
  var keys: List<String>? = null

  override fun translator(): InstructionTranslator {
    return InstructionTranslator { pretty ->
      if (!pretty) {
        return@InstructionTranslator asShortText()
      }
      val operation = StringUtils.defaultString(operation) { "" }
      if (keys.isNullOrEmpty()) {
        return@InstructionTranslator String.format("%s %s", asLongText(), operation)
      } else {
        val ks = StringUtils.join(keys, " ")
        return@InstructionTranslator String.format("%s %s %s", asLongText(), operation, ks)
      }
    }
  }
}
