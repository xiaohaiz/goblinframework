package org.goblinframework.monitor.instruction

import org.goblinframework.api.service.GoblinManagedBean
import org.goblinframework.core.mbean.GoblinManagedObject
import org.goblinframework.core.monitor.Instruction
import org.goblinframework.core.monitor.InstructionTranslator

@GoblinManagedBean(type = "monitor", name = "InstructionTranslator")
class ManagedInstructionTranslator<E : Instruction>
internal constructor(private val delegator: InstructionTranslator<E>)
  : GoblinManagedObject(), InstructionTranslatorMXBean, InstructionTranslator<E> by delegator