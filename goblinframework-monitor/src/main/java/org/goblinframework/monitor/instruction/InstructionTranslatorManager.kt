package org.goblinframework.monitor.instruction

import org.goblinframework.api.annotation.Install
import org.goblinframework.api.annotation.Singleton
import org.goblinframework.api.annotation.ThreadSafe
import org.goblinframework.core.exception.GoblinDuplicateException
import org.goblinframework.core.mbean.GoblinManagedBean
import org.goblinframework.core.mbean.GoblinManagedObject
import org.goblinframework.core.module.spi.RegisterInstructionTranslator
import org.goblinframework.core.monitor.Instruction
import org.goblinframework.core.monitor.InstructionTranslator
import java.util.*
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write

@Singleton
@ThreadSafe
@GoblinManagedBean("monitor")
class InstructionTranslatorManager private constructor()
  : GoblinManagedObject(), InstructionTranslatorManagerMXBean, RegisterInstructionTranslator {

  companion object {
    @JvmField val INSTANCE = InstructionTranslatorManager()
  }

  private val lock = ReentrantReadWriteLock()
  private val translators = IdentityHashMap<Class<out Instruction>, InstructionTranslator<out Instruction>>()

  override fun <E : Instruction> register(type: Class<E>, translator: InstructionTranslator<E>) {
    lock.write {
      translators.putIfAbsent(type, translator)?.run { throw GoblinDuplicateException() }
    }
  }

  fun getInstructionTranslator(type: Class<out Instruction>): InstructionTranslator<out Instruction>? {
    return lock.read { translators[type] }
  }

  @Install
  class Installer : RegisterInstructionTranslator by INSTANCE
}