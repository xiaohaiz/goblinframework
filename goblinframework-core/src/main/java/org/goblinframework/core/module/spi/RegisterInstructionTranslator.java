package org.goblinframework.core.module.spi;

import org.goblinframework.api.monitor.Instruction;
import org.goblinframework.api.monitor.InstructionTranslator1;
import org.jetbrains.annotations.NotNull;

@Deprecated
public interface RegisterInstructionTranslator {

  <E extends Instruction> void register(@NotNull Class<E> type, @NotNull InstructionTranslator1<E> translator);

}
