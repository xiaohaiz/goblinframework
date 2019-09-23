package org.goblinframework.api.monitor;

import org.jetbrains.annotations.NotNull;

@Deprecated
public interface InstructionTranslator1<E extends Instruction> {

  @NotNull
  String translate(@NotNull E instruction, boolean pretty);

}
