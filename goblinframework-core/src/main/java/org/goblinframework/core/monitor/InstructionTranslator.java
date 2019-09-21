package org.goblinframework.core.monitor;

import org.jetbrains.annotations.NotNull;

public interface InstructionTranslator<E extends Instruction> {

  @NotNull
  Class<E> type();

  @NotNull
  String translate(@NotNull E instruction, boolean pretty);

}
