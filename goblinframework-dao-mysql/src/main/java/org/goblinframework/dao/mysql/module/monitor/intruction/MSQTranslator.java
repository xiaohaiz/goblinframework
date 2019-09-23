package org.goblinframework.dao.mysql.module.monitor.intruction;

import org.goblinframework.api.annotation.Singleton;
import org.goblinframework.api.monitor.InstructionTranslator1;
import org.goblinframework.core.util.StringUtils;
import org.jetbrains.annotations.NotNull;

@Singleton
final public class MSQTranslator implements InstructionTranslator1<MSQ> {

  public static final MSQTranslator INSTANCE = new MSQTranslator();

  private MSQTranslator() {
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
