package org.goblinframework.cache.core.module.monitor.instruction;

import org.goblinframework.api.annotation.Install;
import org.goblinframework.core.monitor.InstructionTranslator;
import org.goblinframework.core.util.StringUtils;
import org.jetbrains.annotations.NotNull;

@Install
final public class VMCTranslator implements InstructionTranslator<VMC> {

  @NotNull
  @Override
  public Class<VMC> type() {
    return VMC.class;
  }

  @NotNull
  @Override
  public String translate(@NotNull VMC instruction, boolean pretty) {
    if (!pretty) {
      return instruction.asShortText();
    }
    String operation = StringUtils.defaultString(instruction.operation);
    if (instruction.keys == null || instruction.keys.isEmpty()) {
      return String.format("%s %s", instruction.asLongText(), operation);
    } else {
      String ks = StringUtils.join(instruction.keys, " ");
      return String.format("%s %s %s", instruction.asLongText(), operation, ks);
    }
  }
}
