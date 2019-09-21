package org.goblinframework.dao.mysql.module.monitor.intruction;

import org.goblinframework.api.annotation.Install;
import org.goblinframework.core.monitor.InstructionTranslator;
import org.goblinframework.core.util.StringUtils;
import org.jetbrains.annotations.NotNull;

@Install
final public class MSQTranslator implements InstructionTranslator<MSQ> {

  @NotNull
  @Override
  public Class<MSQ> type() {
    return MSQ.class;
  }

  @NotNull
  @Override
  public String translate(@NotNull MSQ instruction, boolean pretty) {
    if (!pretty) {
      return instruction.asShortText();
    }
    if (instruction.sqlList == null || instruction.sqlList.isEmpty()) {
      return String.format("%s %s[%s] %s",
          instruction.asLongText(),
          instruction.name,
          instruction.mode,
          instruction.operation);
    } else {
      String sqlList = StringUtils.join(instruction.sqlList, "; ");
      return String.format("%s %s[%s] %s %s",
          instruction.asLongText(),
          instruction.name,
          instruction.mode,
          instruction.operation,
          sqlList);
    }
  }
}
