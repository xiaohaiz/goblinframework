package org.goblinframework.monitor.instruction

import org.goblinframework.api.annotation.Install
import org.goblinframework.api.annotation.Singleton
import org.goblinframework.api.annotation.ThreadSafe
import org.goblinframework.api.service.GoblinManagedBean
import org.goblinframework.api.service.GoblinManagedObject
import org.goblinframework.core.exception.GoblinDuplicateException
import org.goblinframework.core.module.spi.RegisterInstructionTranslator
import org.goblinframework.api.monitor.Instruction
import org.goblinframework.api.monitor.InstructionTranslator1
import java.util.*
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write

@Singleton
@ThreadSafe
@GoblinManagedBean(type = "monitor")
class InstructionTranslatorManager private constructor()
  : GoblinManagedObject(), InstructionTranslatorManagerMXBean, RegisterInstructionTranslator {

  companion object {
    @JvmField val INSTANCE = InstructionTranslatorManager()
  }

  private val lock = ReentrantReadWriteLock()
  private val translators = IdentityHashMap<Class<out Instruction>, InstructionTranslator1<out Instruction>>()
  private val defaultTranslator: InstructionTranslator1<Instruction>

  init {
    defaultTranslator = InstructionTranslator1 { instruction, pretty ->
      if (pretty) instruction.asLongText() else instruction.asShortText()
    }
  }

  override fun <E : Instruction> register(type: Class<E>, translator: InstructionTranslator1<E>) {
    lock.write {
      translators.putIfAbsent(type, translator)?.run { throw GoblinDuplicateException() }
    }
  }

  @Suppress("UNCHECKED_CAST")
  fun translator(instruction: Instruction): InstructionTranslator1<Instruction> {
    return lock.read {
      translators[instruction.javaClass]
    } as? InstructionTranslator1<Instruction> ?: defaultTranslator
  }

  @Install
  class Installer : RegisterInstructionTranslator by INSTANCE
}