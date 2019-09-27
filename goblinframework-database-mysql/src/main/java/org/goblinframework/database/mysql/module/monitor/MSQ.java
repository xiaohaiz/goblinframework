package org.goblinframework.database.mysql.module.monitor;

import org.goblinframework.api.monitor.InstructionTranslator;
import org.goblinframework.core.monitor.AbstractInstruction;
import org.goblinframework.core.util.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.List;

final public class MSQ extends AbstractInstruction {

  public String name;
  public String mode;
  public String operation;
  public List<String> sqlList;

  public MSQ() {
    super(Id.MSQ, Mode.SYN, true);
  }

  @NotNull
  @Override
  public InstructionTranslator translator() {
    return pretty -> {
      if (!pretty) {
        return asShortText();
      }
      if (sqlList == null || sqlList.isEmpty()) {
        return String.format("%s %s[%s] %s",
            asLongText(),
            name,
            mode,
            operation);
      } else {
        return String.format("%s %s[%s] %s %s",
            asLongText(),
            name,
            mode,
            operation,
            StringUtils.join(sqlList, "; "));
      }
    };
  }
}
