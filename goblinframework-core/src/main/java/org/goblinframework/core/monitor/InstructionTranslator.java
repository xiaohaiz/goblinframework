package org.goblinframework.core.monitor;

import org.jetbrains.annotations.NotNull;

public interface InstructionTranslator {

  @NotNull
  String translate(boolean pretty);

}
