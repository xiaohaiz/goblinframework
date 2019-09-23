package org.goblinframework.monitor.instruction

import org.goblinframework.api.service.GoblinManagedBean
import org.goblinframework.api.service.GoblinManagedObject
import org.goblinframework.api.monitor.Instruction
import org.goblinframework.api.monitor.InstructionTranslator1

@GoblinManagedBean(type = "monitor", name = "InstructionTranslator")
class ManagedInstructionTranslator<E : Instruction>
internal constructor(private val delegator: InstructionTranslator1<E>)
  : GoblinManagedObject(), InstructionTranslatorMXBean, InstructionTranslator1<E> by delegator