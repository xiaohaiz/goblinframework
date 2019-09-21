package org.goblinframework.core.module.spi;

import org.goblinframework.core.monitor.Instruction;
import org.goblinframework.core.monitor.InstructionTranslator;
import org.jetbrains.annotations.NotNull;

public interface RegisterInstructionTranslator {

  <E extends Instruction> void register(@NotNull Class<E> type, @NotNull InstructionTranslator<E> translator);

}
